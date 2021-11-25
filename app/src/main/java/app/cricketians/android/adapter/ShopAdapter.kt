package app.cricketians.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.cricketians.android.R
import app.cricketians.android.models.Product
import com.bumptech.glide.Glide

class ShopAdapter : RecyclerView.Adapter<ShopViewHolder>() {

    private val productsArray = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {

    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {

        Glide.with(holder.productImage.context).load(productsArray[position].productImage).into(holder.productImage)
        holder.productTitle.text = productsArray[position].productTitle
        holder.productPrice.text = ("INR "+productsArray[position].productPrice)
    }

    override fun getItemCount(): Int {
        return productsArray.size
    }

    fun updateProduct(updatedList: ArrayList<Product>){

        productsArray.clear()
        productsArray.addAll(updatedList)

        notifyDataSetChanged()
    }
}

class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val productImage: ImageView = itemView.findViewById(R.id.productImage)
    val productTitle: TextView = itemView.findViewById(R.id.productTitle)
    val productPrice: TextView = itemView.findViewById(R.id.productPrice)
    val buyNow: Button = itemView.findViewById(R.id.buyNow)
}
