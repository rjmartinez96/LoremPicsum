package com.example.lorempicsum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeImages()
        //Log.d("Date",D)
    }

    private fun initializeImages(){
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

        image1.setImageResource(R.drawable.image1test)
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