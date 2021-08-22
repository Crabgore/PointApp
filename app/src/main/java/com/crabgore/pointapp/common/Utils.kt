package com.crabgore.pointapp.common

import android.graphics.Rect
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.crabgore.pointapp.Const
import com.squareup.picasso.Picasso

fun loadImage(url: String, imageView: ImageView) {
    Picasso.get()
        .load(url)
        .into(imageView)
}

/**
 * добавляем расстояние между элементами в RecyclerView
 */
fun addDecoration(recyclerView: RecyclerView) {
    val spacing = Const.MyPreferences.DECORATION
    recyclerView.apply {
        setPadding(spacing, spacing, spacing, spacing)
        clipToPadding = false
        clipChildren = false
        addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, itemPosition: Int, parent: RecyclerView
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })
    }
}