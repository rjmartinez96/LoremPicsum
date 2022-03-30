package com.example.lorempicsum

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lorempicsum.network.GetDetailsByIdResponse
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
        date.text = DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().time)

        pictureDetails = mutableListOf()

        recyclerView = findViewById(R.id.recycler_view)
        pictureAdapter = PictureAdapter(this,pictureDetails) { position -> onListItemClick(position) }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = pictureAdapter

        viewModel.getRandomIds(this)
    }

    fun loadPictures(newIds: MutableList<Int>){
        viewModel.refreshDetails(newIds[0],newIds[1],newIds[2])

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
        viewModel.getRandomIds(this)
    }

}