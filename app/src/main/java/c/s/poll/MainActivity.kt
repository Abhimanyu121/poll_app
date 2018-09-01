package c.s.poll


import android.app.FragmentManager
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import android.preference.PreferenceManager
import android.content.SharedPreferences



class MainActivity : AppCompatActivity() {
    private var id:String?=null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                var newFrag: Fragment?=null
                val fm=supportFragmentManager
                val ft=fm.beginTransaction()
                val oldfragment=fm.findFragmentByTag(""+id)
                if(oldfragment!=null)
                    ft.hide(oldfragment)
                newFrag=fm.findFragmentByTag("1")
                if(newFrag==null){
                    newFrag=poll()
                    ft.add(R.id.container,newFrag,"1")
                }
                ft.show( newFrag );
                ft.commit();
                id= 1.toString()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                var newFrag: Fragment?=null
                val fm=supportFragmentManager
                val ft=fm.beginTransaction()
                val oldfragment=fm.findFragmentByTag(""+id)
                if(oldfragment!=null)
                    ft.hide(oldfragment)
                newFrag=fm.findFragmentByTag("2")
                if(newFrag==null){
                    newFrag=create()
                    ft.add(R.id.container,newFrag,"2")
                }
                ft.show( newFrag );
                ft.commit();
                id= 2.toString()
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val status=preferences.getBoolean("status",false)
        if(status==false){
        val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        val fm=supportFragmentManager
        val ft=fm.beginTransaction()
        val fragment=poll()
        id= 1.toString()
        ft.replace(R.id.container,fragment,id).addToBackStack(null).commit();
        id= 1.toString()
    }
}
