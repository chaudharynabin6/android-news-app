package com.chaudharynabin6.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// for recycle view you need adapter
// primary constructor of news adapter has items and listener as input
// items contains list of string
// listener  contains the onClick handler on each  holder

// the adapter uses the generic argument of NewsViewHolder
class NewsAdapter(private val items: ArrayList<String>,private val listener:NewsItemListener) : RecyclerView.Adapter<NewsViewHolder>() {
//    onCreateViewHolder is called on each viewHolder Instantiation
//    parent : is viewGroup which is parent of holder
//    newsViewHolder instance is return after adding necessary functionality on view of view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
//    first item View is inflated from resource of view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
//    instance of view holder is created
        val viewHolder = NewsViewHolder(view)
//    onClick listener is added on item View
        view.setOnClickListener {
            this.listener.onClick(item = this.items[viewHolder.adapterPosition])
        }
//    finally viewHolder is returned after adding all the functionality on view
        return viewHolder
    }

//    onBindViewHolder is called when the data is need to bind on item View
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
//     position is position of current item View
        val currentItem = this.items[position]
//    setting the holder textView text with data of items from fetched data
        holder.textView.text = currentItem
    }

    override fun getItemCount(): Int {
        return this.items.size
    }


}
class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.title)
}

interface NewsItemListener {
    fun onClick(item: String)
}

