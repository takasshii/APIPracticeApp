package com.example.apipracticeapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.apipracticeapp.R
import com.example.apipracticeapp.data.Item

// 変更された部分だけ更新する
val diff_util = object : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}

class CustomAdapter(
    private val itemClickListener: OnItemClickListener
) : PagingDataAdapter<Item, CustomAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.repository_title_text)
        val stars: TextView = view.findViewById(R.id.star_number_text)
        val language: TextView = view.findViewById(R.id.language_text)
    }

    interface OnItemClickListener {
        fun itemClick(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        setRepositoryName(holder, item)

        // タップ処理を定義
        holder.itemView.setOnClickListener {
            item?.let {
                itemClickListener.itemClick(item)
            }
        }
    }

    private fun setRepositoryName(holder: ViewHolder, item: Item?) {
        holder.title.text = item?.name
        holder.stars.text = "${item?.stargazersCount}stars"
        holder.language.text = item?.language
    }
}