package com.example.apipracticeapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apipracticeapp.R
import com.example.apipracticeapp.data.Content

// 変更された部分だけ更新する
val diff_util = object : DiffUtil.ItemCallback<Content>() {
    override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
        return oldItem == newItem
    }
}

class CustomAdapter(
    private val itemClickListener: OnItemClickListener
) : ListAdapter<Content, CustomAdapter.ViewHolder>(diff_util) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.repository_title_text)
        val stars: TextView = view.findViewById(R.id.star_number_text)
        val language: TextView = view.findViewById(R.id.language_text)
        val watchers: TextView = view.findViewById(R.id.watchers_text)
    }

    interface OnItemClickListener {
        fun itemClick(item: Content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        setRepositoryName(holder, item)

        // ここでランキングに値をセット
        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(item)
        }
    }

    private fun setRepositoryName(holder: ViewHolder, item: Content) {
        holder.title.text = item.name
        holder.stars.text = item.stargazersCount.toString()
        holder.language.text = item.language
        holder.watchers.text = item.watchersCount.toString()
    }
}