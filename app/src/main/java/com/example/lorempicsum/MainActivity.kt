package com.example.lorempicsum

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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
        pictureAdapter = PictureAdapter(this,pictureDetails)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = pictureAdapter


        //pictureDetails.add(GetDetailsByIdResponse("rj","https://picsum.photos/id/0/300/300",0,"0","",0))
        //pictureDetails.add(GetDetailsByIdResponse("rj","https://picsum.photos/id/0/300/300",0,"1","",0))
        //pictureDetails.add(GetDetailsByIdResponse("rj","https://picsum.photos/id/0/300/300",0,"2","",0))

        loadPictures()

//        viewModel.detailsByLiveData.observe(this){ responseList ->
//            responseList.forEachIndexed { index, response ->
//                if(response == null){
//                    Log.i("getPictureDetailsById:","FAILED")
//                    return@observe
//                }
//                response.let {
//                    Log.d("Pic author:",response.author)
//                    val newDownloadUrl = "https://picsum.photos/id/"+response.id+"/300/300"
//                    pictureDetails.add(index, GetDetailsByIdResponse(response.author,newDownloadUrl,
//                        response.height,response.id,response.url,response.width))
//                }
//            }
//        }
    }

    private fun loadPictures(){
        viewModel.refreshDetails(167,160,0)
        viewModel.detailsByLiveData.observe(this){ responseList ->
            responseList.forEachIndexed { index, response ->
                if(response == null){
                    Log.i("getPictureDetailsById:","FAILED")
                    return@observe
                }
                response.let {
                    Log.d("Pic author:",response.author)
                    pictureDetails.add(index, response)
                    pictureAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private val ioScope = CoroutineScope(Dispatchers.IO + Job() )
    fun fireAndForgetNetworkCall() {
        Log.i("logtag", "-----Async network calls without error handling-----")
        ioScope.launch {
            val job = ArrayList<Job>()

            Log.i("logtag", "Making 3 asynchronous network calls")
            for (i in 1..3){
                job.add(launch {
                    Log.i("logtag", "Network Call ID: $i")
                    val response = NetworkLayer.okHttpClient.newCall(NetworkLayer.randomRequest).execute()
                    Log.d("Pic ID:",response.header("picsum-id").toString())
                })
            }

            job.joinAll()
            Log.i("logtag", "All Networks calls have completed executing")
        }
    }
}