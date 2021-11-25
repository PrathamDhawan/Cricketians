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
import app.cricketians.android.models.News
import app.cricketians.android.adapter.NewsAdapter
import app.cricketians.android.adapter.NewsItemClicked
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class NewsFragment : Fragment(), NewsItemClicked {

    private val NEWSURL =
        "https://newscatcher.p.rapidapi.com/v1/search_free?q=cricket&lang=en&media=True"
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

    private fun getNews(){

        val newsArray = ArrayList<News>()

        val request = object: JsonObjectRequest(Request.Method.GET, NEWSURL,null,{

            val newsJsonArray = it.getJSONArray("articles")
            val numberOfNews = newsJsonArray.length()

            for (news in 0 until numberOfNews) {

                val newsJsonObject = newsJsonArray.getJSONObject(news)
                val newsItem = News(
                    newsJsonObject.getString("title"),
                    newsJsonObject.getString("link"),
                    newsJsonObject.getString("media")
                )
                newsArray.add(newsItem)
            }
        newsAdapter.updateNews(newsArray) }, {
            Toast.makeText(requireContext(), "Sorry", Toast.LENGTH_SHORT).show()
        })
         {
          override fun getHeaders(): Map<String, String> {

                val headers = HashMap<String, String>()
                headers["x-rapidapi-host"] = "newscatcher.p.rapidapi.com"
                headers["x-rapidapi-key"] = "0edaaad673msh824b103581066fdp1eb63cjsn60d05d14c32a"

                return headers
            }
        }

        context?.let { Singleton.getInstance(it.applicationContext).addToRequestQueue(request) }
    }

    override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder()
        builder.setShowTitle(true)
        val build = builder.build()
        context?.let { build.launchUrl(it, Uri.parse(item.url)) }
    }
}

