package com.example.lorempicsum

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
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

    val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        date = findViewById(R.id.date)
        date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time))

        initializeImages()

        viewModel.refreshDetails(0)
        viewModel.detailsByLiveData.observe(this){
            response ->
            if(response == null){
                Log.i("getPictureDetailsById:","FAILED")
                return@observe
            }
            response.let { Log.d("author:", it.author) }
        }

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://picsum.photos/400/400")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    println(response.header("picsum-id").toString())

                }
            }
        })
    }

    private fun initializeImages(){
        image1 = findViewById(R.id.image1)
        image2 = findViewById(R.id.image2)
        image3 = findViewById(R.id.image3)

        image1Author = findViewById(R.id.image1_author)
        image2Author = findViewById(R.id.image2_author)
        image3Author = findViewById(R.id.image3_author)

        image1Details = findViewById(R.id.image1_details)
        image2Details = findViewById(R.id.image2_details)
        image3Details = findViewById(R.id.image3_details)

        Picasso.get().load("https://picsum.photos/400/400.jpg").into(image1);
        image2.setImageResource(R.drawable.image2test)
        image3.setImageResource(R.drawable.image3test)

        image1Author.setText(getString(R.string.author,"Dave Smith"))
        image2Author.setText(getString(R.string.author,"Sarah Fisher"))
        image3Author.setText(getString(R.string.author,"Jose Aldo"))

        image1Details.setText(R.string.details)
        image2Details.setText(R.string.details)
        image3Details.setText(R.string.details)
    }
}