package com.example.lorempicsum

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lorempicsum.databinding.ItemPictureBinding
import com.squareup.picasso.Picasso
import java.text.DateFormat
import java.util.*

class PictureAdapter(
    val context: Context,
    val pictureData: MutableList<GetDetailsByIdResponse>
    ) : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    inner class PictureViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val imageDetails = view.findViewById<TextView>(R.id.image_details)
        val image = view.findViewById<ImageView>(R.id.image)
        val imageAuthor = view.findViewById<TextView>(R.id.image_author)
    }

    override fun getItemCount() = pictureData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_picture,
                parent,false))
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val pictureDetails = pictureData[position]
        holder.apply {
            imageDetails.text = "ID: ${pictureDetails.id},Width: ${pictureDetails.width}," +
                    "Height: ${pictureDetails.height},Time added: ${Calendar.getInstance().time.toString()}"
            Picasso.get().load(pictureDetails.downloadUrl).into(image)
            imageAuthor.text = pictureDetails.author
        }
    }
}