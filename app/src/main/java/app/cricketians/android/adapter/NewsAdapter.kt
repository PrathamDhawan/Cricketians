package app.cricketians.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.cricketians.android.R
import app.cricketians.android.models.News
import com.bumptech.glide.Glide
import kotlinx.coroutines.withContext

class NewsAdapter(private val listener: NewsItemClicked) : RecyclerView.Adapter<NewsHolder>() {

    private val NewsArray: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsHolder(view)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {

        holder.titleView.text = NewsArray[position].title
        Glide.with(holder.imageView.context).load(NewsArray[position].imageurl).into(holder.imageView)
        holder.cardViewLink.setOnClickListener {
            listener.onItemClicked(NewsArray[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
         return NewsArray.size
    }

    fun updateNews(updatedNews: ArrayList<News>){

        NewsArray.clear()
        NewsArray.addAll(updatedNews)

        notifyDataSetChanged()
    }
}

class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titleView: TextView = itemView.findViewById(R.id.tv_title)
    val imageView: ImageView = itemView.findViewById(R.id.iv_urlImage)
    val cardViewLink: CardView = itemView.findViewById(R.id.cardView)
}

interface NewsItemClicked{

    fun onItemClicked(item: News){}
}