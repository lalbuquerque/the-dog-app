package com.github.lalbuquerque.dogapp.ui.dogfeed.recyclerview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.lalbuquerque.dogapp.R
import com.github.lalbuquerque.dogapp.ui.FullScreenImageActivity

class DogFeedAdapter(var context: Context, private var dogList: List<DogItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.dog_item, parent, false)

        return DogViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dogImageUrl = dogList[position].dogImage.url

        val viewHolder = holder as DogViewHolder

        Glide.with(context)
            .load(dogImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .override(500, 500)
            .into(viewHolder.imgDog);

        viewHolder.imgDog.setOnClickListener {
            val goToFullScreenImageActivity = Intent(context, FullScreenImageActivity::class.java)
            goToFullScreenImageActivity.putExtra(
                FullScreenImageActivity.EXTRA_IMAGE_RESOURCE,
                dogImageUrl
            )

            context.startActivity(goToFullScreenImageActivity)
        }
    }

    override fun getItemCount(): Int {
        return dogList.size
    }

    internal inner class DogViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imgDog: ImageView = itemView.findViewById(R.id.imgDog)
    }

}