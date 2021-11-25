package app.cricketians.android.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.cricketians.android.R
import app.cricketians.android.activities.CreatePostActivity
import app.cricketians.android.adapter.IPostAdapter
import app.cricketians.android.adapter.PostAdapter
import app.cricketians.android.models.Post
import app.cricketians.android.models.PostDao
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment(), IPostAdapter {

    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize(view)

        fab.setOnClickListener{
            if(Firebase.auth.currentUser != null)
                startActivity(Intent(context, CreatePostActivity::class.java))
            else
                Toast.makeText(context,"Please sign in first !!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initialize(view: View) {
        fab = view.findViewById(R.id.fab)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,
            Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)
        postsRecyclerView.adapter = adapter
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        if(Firebase.auth.currentUser!= null)
            postDao.updateLikes(postId)
        else
            Toast.makeText(context, "Not allowed", Toast.LENGTH_SHORT).show()
    }
}