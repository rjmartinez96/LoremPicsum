package com.example.lorempicsum

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lorempicsum.network.GetDetailsByIdResponse
import com.example.lorempicsum.network.NetworkLayer
import kotlinx.coroutines.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var date: TextView

    lateinit var recyclerView: RecyclerView
    lateinit var pictureDetails: MutableList<GetDetailsByIdResponse>
    lateinit var pictureAdapter: PictureAdapter

    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        date = findViewById(R.id.date)
        date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time))

        pictureDetails = mutableListOf()

        recyclerView = findViewById(R.id.recycler_view)
        pictureAdapter = PictureAdapter(this,pictureDetails) { position -> onListItemClick(position) }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = pictureAdapter

        getRandomPictures()
    }

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())
    private fun getRandomPictures(){
        val newIds = mutableListOf<Int>()
        ioScope.launch {
            val job = ArrayList<Job>()

            for (i in 1..3){
                job.add(async {
                    val response = NetworkLayer.okHttpClient.newCall(NetworkLayer.randomRequest).execute()
                    newIds.add(response.header("picsum-id")?.toInt() ?: 0)
                    Log.d("Pic id retrieved",response.header("picsum-id").toString())
                })
            }

            job.joinAll()
            Log.i("logtag", "All  calls have completed executing")
            runOnUiThread{loadPictures(newIds[0],newIds[1],newIds[2])}
        }
    }

    private fun loadPictures(id1: Int, id2: Int, id3: Int){
        viewModel.refreshDetails(id1,id2,id3)
        viewModel.detailsByLiveData.observe(this){ responseList ->
            responseList.forEachIndexed { index, response ->
                if(response == null){
                    Log.e("getPictureDetailsById:","FAILED")
                    return@observe
                }
                response.let {
                    Log.d("Pic loaded",response.id)
                    if(pictureDetails.size < 3){
                        pictureDetails.add(response)
                    } else pictureDetails.set(index, response)
                    pictureAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    private fun onListItemClick(position: Int) {
        Log.i("Picture clicked", position.toString())
        getRandomPictures()
    }

}