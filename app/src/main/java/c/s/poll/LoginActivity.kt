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
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        create.setOnClickListener(){
            register()
        }
        email_sign_in_button.setOnClickListener(){
            login()
        }

    }
    fun login(){

        val progress=ProgressDialog(this)

        progress.show()
        val mail=email.text.toString()
        val pass=password.text.toString()
        val db=FirebaseFirestore.getInstance().document("users/"+mail)
        try{

           db.addSnapshotListener({documentSnapshot,error ->
               if (error!=null){
                   Log.e("bleh",error.message)
               }
               else if (documentSnapshot!=null){
                       val pwd=documentSnapshot.getString("password").toString()
                       val name=documentSnapshot.getString("name").toString()
                       val group=documentSnapshot.getString("group").toString()
                       if(pwd.equals(pass)){
                           var preferences = PreferenceManager.getDefaultSharedPreferences(this)
                           val bool=preferences.getBoolean("status",false)
                           if(bool==false){
                               val intent=Intent(this,LoginActivity::class.java)
                               startActivity(intent)
                           }
                           var editor = preferences.edit()
                           editor.putBoolean("status",true)
                           editor.putString("name",name)
                           editor.putString("email",mail)
                           editor.putString("group",group)
                           editor.apply()
                           progress.dismiss()
                           val intent= Intent(this,MainActivity::class.java)
                           startActivity(intent)

                       }
                   else{
                           Toast.makeText(this,"Password or email incorrect",Toast.LENGTH_LONG).show()
                       }
                   }
               })


        }
        catch(e:Throwable){
            if(progress.isShowing()){
                progress.dismiss()
            }

            Log.e("bleh",e.message)

        }


    }
    fun register(){
        val intent=Intent(this,Register_activity::class.java)
        startActivity(intent)
    }






    }

