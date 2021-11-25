package app.cricketians.android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.cricketians.android.R
import app.cricketians.android.models.PostDao
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {

    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        post_bt.setOnClickListener{
            postDao = PostDao()
            val input = content_et.text.toString().toString()
            if(input.isNotEmpty()){
                postDao.addPost(input)
                finish()
            }
        }
    }
}