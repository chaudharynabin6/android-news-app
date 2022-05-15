package com.chaudharynabin6.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(),NewsItemListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        getting instance of recycler view
        val recyclerView  : RecyclerView = findViewById(R.id.recycleView)
//        setting recycler view layout manager
        recyclerView.layoutManager  = LinearLayoutManager(this)
//        fetching data
        val data = fetchData()
//        creating instance of NewsAdapter
        val adapter = NewsAdapter(data,this)

//        setting the recycler view adapter as instance of
        recyclerView.adapter = adapter

    }
//    function to fetch data from api
    private fun fetchData(): ArrayList<String> {
        val list = ArrayList<String>()

        for (i in 0 until 100) {
            list.add("Item $i")
        }

        return list


    }
// overriding the NewsItemListener onClick method
    override fun onClick(item: String) {
        Toast.makeText(this,"$item clicked !!",Toast.LENGTH_SHORT).show()
    }
}