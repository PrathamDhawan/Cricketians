package app.cricketians.android.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.cricketians.android.R
import app.cricketians.android.Singleton
import app.cricketians.android.adapter.ShopAdapter
import app.cricketians.android.models.Product
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import kotlinx.android.synthetic.main.fragment_shop.*

class ShopFragment : Fragment() {

    private lateinit var shopAdapter: ShopAdapter
    private val url = "https://cricketians.000webhostapp.com/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getProducts()
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar?.visibility = View.VISIBLE

        productRecyclerView.layoutManager = LinearLayoutManager(context)
        shopAdapter = ShopAdapter()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({

            progressBar?.visibility = View.GONE

        },900)

        productRecyclerView.adapter = shopAdapter
    }

    private fun getProducts(){

        val list = ArrayList<Product>()

        val request = JsonArrayRequest(Request.Method.GET, url, null, { response ->

                    for (products in 0 until response.length()) {

                        val product = response.getJSONObject(products)
                        list.add(
                            Product(product.getString("url"),
                                product.getString("title"),
                                product.getInt("price")
                        ))
                    }
                    shopAdapter.updateProduct(list)
           },
            { Toast.makeText(requireContext(), "Sorry", Toast.LENGTH_SHORT).show() })

        context?.let { Singleton.getInstance(it).addToRequestQueue(request) }
    }
}