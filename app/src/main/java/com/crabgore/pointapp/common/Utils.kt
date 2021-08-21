package com.crabgore.pointapp.common

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun loadImage(url: String, imageView: ImageView) {
    Picasso.get()
        .load(url)
        .into(imageView)
}