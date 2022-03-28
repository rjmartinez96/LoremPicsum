package com.example.lorempicsum

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import java.text.DateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var date: TextView

    lateinit var image1Details: TextView
    lateinit var image1: ImageView
    lateinit var image1Author: TextView

    lateinit var image2Details: TextView
    lateinit var image2: ImageView
    lateinit var image2Author: TextView

    lateinit var image3Details: TextView
    lateinit var image3: ImageView
    lateinit var image3Author: TextView

    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        date = findViewById(R.id.date)
        date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time))

        image1 = findViewById(R.id.image1)
        image2 = findViewById(R.id.image2)
        image3 = findViewById(R.id.image3)

        image1Author = findViewById(R.id.image1_author)
        image2Author = findViewById(R.id.image2_author)
        image3Author = findViewById(R.id.image3_author)

        image1Details = findViewById(R.id.image1_details)
        image2Details = findViewById(R.id.image2_details)
        image3Details = findViewById(R.id.image3_details)

        //Picasso.get().load("https://picsum.photos/id/237/400/400.jpg").into(image1);
        val nums = mutableListOf<Int>()
        for (i in 1..3){
            NetworkLayer.okHttpClient.newCall(NetworkLayer.randomRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        Log.d("Pic ID:",response.header("picsum-id").toString())
                        nums.add(response.header("picsum-id")?.toInt() ?: 0)
                        //id = response.header("picsum-id")?.toInt() ?: 0
                    }
                }
            })
        }
        Log.d("List",nums.toString())
        //initializeImage(45,image1,image1Author,image1Details)
        //initializeImage(67,image2,image2Author,image2Details)
        //initializeImage(200,image3,image3Author,image3Details)
    }


    private fun initializeImage(id: Int, image: ImageView, imageAuthor: TextView, imageDetails: TextView){
        viewModel.refreshDetails(id)
        viewModel.detailsByLiveData.observe(this){ response ->
            if(response == null){
                Log.i("getPictureDetailsById:","FAILED")
                return@observe
            }
            response.let {
                imageAuthor.text = it.author
                imageDetails.text = "${it.id},${it.width},${it.height},${DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time)}"
                Picasso.get().load("${it.downloadUrl}.jpg").into(image);
            }
        }
    }

//    private fun initializeImages(){
//        Picasso.get().load("https://picsum.photos/400/400.jpg").into(image1);
//        image2.setImageResource(R.drawable.image2test)
//        image3.setImageResource(R.drawable.image3test)
//
//        image1Author.setText(getString(R.string.author,"Dave Smith"))
//        image2Author.setText(getString(R.string.author,"Sarah Fisher"))
//        image3Author.setText(getString(R.string.author,"Jose Aldo"))
//
//        image1Details.setText(R.string.details)
//        image2Details.setText(R.string.details)
//        image3Details.setText(R.string.details)
//    }
}