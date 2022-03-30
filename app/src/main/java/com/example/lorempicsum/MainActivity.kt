package com.example.lorempicsum

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lorempicsum.network.GetDetailsByIdResponse
import com.example.lorempicsum.network.NetworkLayer
import kotlinx.coroutines.*
import java.text.DateFormat
import java.util.*


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
                })
            }

            job.joinAll()
            runOnUiThread{loadPictures(newIds[0],newIds[1],newIds[2])}
        }
    }

    private fun loadPictures(id1: Int, id2: Int, id3: Int){
        viewModel.refreshDetails(id1,id2,id3)

        viewModel.detailsByLiveData.observe(this, object: Observer<List<GetDetailsByIdResponse?>> {
            override fun onChanged(responseList: List<GetDetailsByIdResponse?>) {
                responseList.forEachIndexed { index, response ->
                    if(response == null){
                        return
                    }
                    response.let {
                        if(pictureDetails.size < 3){
                            pictureDetails.add(response)
                        } else pictureDetails.set(index, response)
                        pictureAdapter.notifyItemChanged(index)
                    }
                }
            }
        })
    }

    private fun onListItemClick(position: Int) {
        getRandomPictures()
    }

}