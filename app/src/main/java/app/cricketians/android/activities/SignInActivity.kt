package app.cricketians.android.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import app.cricketians.android.R
import app.cricketians.android.models.User
import app.cricketians.android.models.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient =  GoogleSignIn.getClient(this, gso)

        signInGoogle.setOnClickListener {
           signinIntent()
            }
        }

    var launchIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignIn(task)
        }else{
            Toast.makeText(this, "Network error occured", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSignIn(task: Task<GoogleSignInAccount>){
        try{
            val account = task.getResult(ApiException::class.java)
            firebaseSignIn(account.idToken!!)
            progressBar.visibility = View.VISIBLE
            signInGoogle.visibility = View.GONE
        }catch (e: ApiException){
        }
    }

    private fun firebaseSignIn(idToken: String) {

        val credentials = GoogleAuthProvider.getCredential(idToken,null)
        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credentials).await()
            val user = auth.user
            withContext(Dispatchers.Main){
                updateUi(user)
            }
        }
    }

    private fun signinIntent(){
        val intent = googleSignInClient.signInIntent
        launchIntent.launch(intent)
    }

    fun updateUi(firebaseUser : FirebaseUser?){
        if(firebaseUser != null){

            val user = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.photoUrl.toString())
            val userDao = UserDao()
            userDao.addUser(user)

            startActivity(Intent(this, HomeActivity::class.java))
            finish()

        }else{
            progressBar.visibility = View.GONE
            signInGoogle.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUi(currentUser)
        }
    }
}