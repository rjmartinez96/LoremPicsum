package com.example.lorempicsum

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lorempicsum.network.GetDetailsByIdResponse
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class PictureAdapter(
    val context: Context,
    val pictureData: MutableList<GetDetailsByIdResponse>,
    private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    inner class PictureViewHolder(
        view: View,
        private val onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener{
        val imageDetails = view.findViewById<TextView>(R.id.image_details)
        val image = view.findViewById<ImageView>(R.id.image)
        val imageAuthor = view.findViewById<TextView>(R.id.image_author)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    override fun getItemCount() = pictureData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_picture,
                parent,false), onItemClicked)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val pictureDetails = pictureData[position]
        holder.apply {
            val dateFormat = SimpleDateFormat("hh:mm a", Locale.US)
            imageDetails.text = context.getString(R.string.details,pictureDetails.id,
                pictureDetails.width,pictureDetails.height,dateFormat.format(Calendar.getInstance().time))
            val newImage = "https://picsum.photos/id/"+pictureDetails.id+"/300/300"
            Picasso.get().load(newImage).into(image)
            imageAuthor.text = context.getString(R.string.author,pictureDetails.author)
        }
    }
}