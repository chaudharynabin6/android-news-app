package com.chaudharynabin6.newsapp


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.system.ErrnoException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import java.io.IOException


// for recycle view you need adapter
// primary constructor of news adapter listener as input
// listener  contains the onClick handler on each  holder

// the adapter uses the generic argument of NewsViewHolder
class NewsAdapter(private val listener: NewsItemListener) : RecyclerView.Adapter<NewsViewHolder>() {
    //    making items as private variable
//    items holds all the data fetched from API
    private var items = ArrayList<NewsData>()

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
        holder.textView.text = currentItem.title
        holder.author.text = if (currentItem.author == "null") " " else currentItem.author


        Glide.with(holder.itemView.context).load(currentItem.urlToImage).listener(
            object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // log exception
//                            Log.e("ERROR", "Error loading image", e)
//                            Log.e("ROOT-CAUSES", e?.rootCauses.toString())

                    for (t in e!!.rootCauses) {
//                                if(t.cause is IOException || t.message.toString() == "open failed: ENOENT (No such file or directory)"){
                        if (t.cause is IOException) {

                            holder.imageView.visibility = View.GONE
                            val params = holder.imageView.layoutParams
                            params.height = 0
                            params.width = 0
                            holder.imageView.layoutParams = params
                            holder.imageView.requestLayout()
                            holder.imageView.forceLayout()
                        }
                        Log.e("cause", t.cause.toString())
                        Log.e("cause_message", t.message.toString())

                    }






                    return false// important to return false so the error placeholder can be placed

                }


                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }


            }
        )
            .into(holder.imageView)


    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    //    this method helps to update the fetched items list
    @SuppressLint("NotifyDataSetChanged")
    fun updateNews(updatedNews: ArrayList<NewsData>) {
//    clearing the previous items on list
        this.items.clear()
//    adding all the items fetched from API
        this.items.addAll(updatedNews)
//    notifying the fetched items are updated to adapter
        this.notifyDataSetChanged()
    }


}


class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.findViewById(R.id.title)
    val imageView: ImageView = itemView.findViewById(R.id.image)
    val author: TextView = itemView.findViewById(R.id.author)
}

interface NewsItemListener {
    fun onClick(item: NewsData)
}


