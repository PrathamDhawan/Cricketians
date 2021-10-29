package app.cricketians.android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import app.cricketians.android.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth
        bt_SignIn.isEnabled = false

        tv_signIn.setOnClickListener{
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

        bt_SignIn.setOnClickListener {

            innerLinear.visibility = View.GONE
            progressBar.visibility = View.VISIBLE

            email = et_Email.text.toString()
            password = et_Password.text.toString()

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                signIn(email, password)
             }, 500)
        }

        et_Email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                bt_SignIn.isEnabled = isNotEmptyAndNotNull()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                bt_SignIn.isEnabled = isNotEmptyAndNotNull()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bt_SignIn.isEnabled = isNotEmptyAndNotNull()
            }
        })

        et_Password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                bt_SignIn.isEnabled = isNotEmptyAndNotNull()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                bt_SignIn.isEnabled = isNotEmptyAndNotNull()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bt_SignIn.isEnabled = isNotEmptyAndNotNull()
            }
        })
    }

    private fun isNotEmptyAndNotNull(): Boolean{

        return (et_Email.text.toString().isNotEmpty() &&
                et_Password.text.toString().isNotEmpty())
    }

    private fun signIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    updateUi(user)
                } else {
                        innerLinear.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUi(firebaseUser : FirebaseUser?){

        if(firebaseUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }else{
            innerLinear.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}