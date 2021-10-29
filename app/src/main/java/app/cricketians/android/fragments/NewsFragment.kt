package app.cricketians.android.fragments

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cricketians.android.*
import app.cricketians.android.adapter.News
import app.cricketians.android.adapter.NewsAdapter
import app.cricketians.android.adapter.NewsItemClicked
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest

class NewsFragment : Fragment(), NewsItemClicked {

    private val NEWSURL =
        "https://t20-cricket-news.p.rapidapi.com/news/?rapidapi-key=0edaaad673msh824b103581066fdp1eb63cjsn60d05d14c32a"
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getNews()
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView? = view.findViewById(R.id.recyclerView)
        val bar: ProgressBar? = view.findViewById(R.id.progressBar)

        bar?.visibility = View.VISIBLE

        recyclerView?.layoutManager = LinearLayoutManager(context)
        newsAdapter = NewsAdapter(this)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            bar?.visibility = View.GONE

        },900)

        recyclerView?.adapter = newsAdapter

    }

    private fun getNews(): ArrayList<News> {

        val newsArray = ArrayList<News>()

        val request = JsonArrayRequest(Request.Method.GET, NEWSURL, null, {
            val numberOfNews = it.length()

            for (news in 0 until numberOfNews) {

                val newsJsonObject = it.getJSONObject(news)
                val news = News(
                    newsJsonObject.getString("title"),
                    newsJsonObject.getString("url"),
                    newsJsonObject.getString("source")
                )

                newsArray.add(news)
            }

        newsAdapter.updateNews(newsArray)
                                                                          }, {
            Toast.makeText(requireContext(), "Sorry", Toast.LENGTH_SHORT).show()
        })

        context?.let { Singleton.getInstance(it.applicationContext).addToRequestQueue(request) }
        return newsArray
    }

    override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder()
        builder.setShowTitle(true)
        val build = builder.build()
        context?.let { build.launchUrl(it, Uri.parse(item.url)) }
    }
}

