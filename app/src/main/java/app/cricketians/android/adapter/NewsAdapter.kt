package app.cricketians.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import app.cricketians.android.R

class NewsAdapter(private val listener: NewsItemClicked) : RecyclerView.Adapter<NewsHolder>() {

    private val NewsArray: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsHolder(view)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {

        holder.titleView.text = NewsArray[position].title
        holder.sourceView.text = ("source - " + NewsArray[position].source)
        holder.cardView.setOnClickListener {
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
    val sourceView: TextView = itemView.findViewById(R.id.tv_source)
    val cardView: CardView = itemView.findViewById(R.id.cardView)
}

interface NewsItemClicked{

    fun onItemClicked(item: News){}
}