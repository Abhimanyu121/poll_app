package c.s.poll

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference

import kotlinx.android.synthetic.main.activity_register_activity.*
import com.google.firebase.firestore.FirebaseFirestore



/**
 * A login screen that offers login via email/password.
 */
class Register_activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activity)
       rbutton.setOnClickListener{
           create_user()
       }
    }
fun create_user(){
    val progress=ProgressDialog(this)
    progress.show()
    var mail=remail.text.toString()
    var name=rname.text.toString()
    var pass=rpassword.text.toString()
    var gp:String?=null
    if(rb1.isChecked){
        gp="a"
    }
    else if(rb2.isChecked){
        gp="b"
    }
    val db = FirebaseFirestore.getInstance()
    var user = HashMap<String,Any>()
    user.put("name",name)
    user.put("passoword",pass)
    user.put("group",gp!!)
    db.collection("users").document(mail).set(user).addOnSuccessListener {void:Void? ->
       Toast.makeText(this,"done!",Toast.LENGTH_SHORT).show()
        progress.dismiss()
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)

    }


}


}


