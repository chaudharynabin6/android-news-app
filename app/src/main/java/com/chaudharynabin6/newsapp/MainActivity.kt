package com.chaudharynabin6.newsapp

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemListener {
    //    making adapter as private variable
    private lateinit var mAdapter: NewsAdapter

    private var mChromePackage = "com.android.chrome";
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
        val builder = CustomTabsIntent.Builder()

        // to set the toolbar color use CustomTabColorSchemeParams
        // since CustomTabsIntent.Builder().setToolBarColor() is deprecated

        val params = CustomTabColorSchemeParams.Builder()
        params.setToolbarColor(ContextCompat.getColor(this@MainActivity, com.google.android.material.R.color.material_blue_grey_800))
        builder.setDefaultColorSchemeParams(params.build())

        // shows the title of web-page in toolbar
        builder.setShowTitle(true)

        // setShareState(CustomTabsIntent.SHARE_STATE_ON) will add a menu to share the web-page
        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)

        // To modify the close button, use
        // builder.setCloseButtonIcon(bitmap)

        // to set weather instant apps is enabled for the custom tab or not, use
        builder.setInstantAppsEnabled(true)

        //  To use animations use -
        //  builder.setStartAnimations(this, android.R.anim.start_in_anim, android.R.anim.start_out_anim)
        //  builder.setExitAnimations(this, android.R.anim.exit_in_anim, android.R.anim.exit_out_anim)
        val customBuilder = builder.build()

        if (this.isPackageInstalled(mChromePackage)) {
            // if chrome is available use chrome custom tabs
            customBuilder.intent.setPackage(mChromePackage)
            customBuilder.launchUrl(this, Uri.parse(item.url))
        } else {
            // if not available use WebView to launch the url
        }
    }
}

fun Context.isPackageInstalled(packageName: String): Boolean {
    // check if chrome is installed or not
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}