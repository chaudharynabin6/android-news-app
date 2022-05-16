package com.chaudharynabin6.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.GridLayout.HORIZONTAL
import android.widget.HorizontalScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemListener {
    //    making adapter as private variable
    private lateinit var mAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        getting instance of recycler view
        val recyclerView: RecyclerView = findViewById(R.id.recycleView)
//        setting recycler view layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)
//        fetching data
        fetchData()
//        creating instance of NewsAdapter
        this.mAdapter = NewsAdapter(this)

//        setting the recycler view adapter as instance of
        recyclerView.adapter = this.mAdapter

    }

    //    function to fetch data from api
    private fun fetchData() {
        // Get a RequestQueue
        val url =
            "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=90b2e898a0e44a49b2e9bb9c3c22d586"
//    https://stackoverflow.com/questions/67780323/e-volley-44820-network-utility-should-retry-exception-unexpected-response-c
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
//        SAM for response
            { response ->
                Log.d("DATA", response.toString(2))
//            getting array of articles from articles key
                val fetchedNewsArray = response.getJSONArray("articles")
//            creating temp array list for holding fetched news data
                val newsDataArray = ArrayList<NewsData>()
                for (i in 0 until fetchedNewsArray.length()) {
//                using loop to access fetched news
                    val fetchedNews = fetchedNewsArray.getJSONObject(i)
//                creating the object of news data
                    val newsData = NewsData(
                        author = fetchedNews.getString("author"),
                        title = fetchedNews.getString("title"),
                        description = fetchedNews.getString("description"),
                        url = fetchedNews.getString("url"),
                        urlToImage = fetchedNews.getString("urlToImage")
                    )
//                finally adding to the newsDataArray
                    newsDataArray.add(newsData)
                }
//            now finally updating the adapter item
                this.mAdapter.updateNews(newsDataArray)
            },
//        SAM for error
            { error ->
                // TODO: Handle error
                Log.d("FETCH ERROR", error.message.toString())
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }



        VolleyRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest)


    }

    // overriding the NewsItemListener onClick method
    override fun onClick(item: NewsData) {

    }
}