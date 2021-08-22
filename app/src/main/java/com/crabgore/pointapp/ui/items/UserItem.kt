package com.crabgore.pointapp.ui.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.crabgore.pointapp.R
import com.crabgore.pointapp.common.loadImage
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

class UserItem(
    val id: Int,
    private val userName: String,
    private val userIcon: String?
) : AbstractItem<UserItem.ViewHolder>() {
    /** defines the type defining this item. must be unique. preferably an id */
    override val type: Int
        get() = R.id.user_item

    /** defines the layout which will be used for this item in the list */
    override val layoutRes: Int
        get() = R.layout.user_item

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<UserItem>(view) {
        private val name: TextView = view.findViewById(R.id.name)
        private val icon: ImageView = view.findViewById(R.id.userIcon)

        override fun bindView(item: UserItem, payloads: List<Any>) {
            name.text = item.userName
            item.userIcon?.let { loadImage(it, icon) }
        }

        override fun unbindView(item: UserItem) {
            name.text = null
            icon.setImageDrawable(null)
        }
    }
}