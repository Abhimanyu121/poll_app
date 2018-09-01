package c.s.poll

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_poll.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [poll.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [poll.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class poll : Fragment() {
    // TODO: Rename and change types of parameters
    var group:String?=null
    var linear:LinearLayout?=null
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    var count:Int?=null

    var mView:View?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val prefs=PreferenceManager.getDefaultSharedPreferences(context)
        group=prefs.getString("group","a")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView=inflater.inflate(R.layout.fragment_poll, container, false)
        getcount()
        Thread.sleep(10000)
        Log.e("bleh",count.toString())
       //
        linear=mView!!.findViewById(R.id.lpoll)



        return mView
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }
    fun getcount(){
        val db:DocumentReference

        if(group.equals("a")){
        db=FirebaseFirestore.getInstance().collection("count_a").document("counts")}
        else{
            db= FirebaseFirestore.getInstance().collection("count_b").document("counts")
        }
        db.get().addOnCompleteListener({task->

           if (task.isSuccessful)
            {
              count=Integer.parseInt(task.result.getString("value").toString())
                Log.e("stumble1",count.toString())
                create_view()
            }
        })

        }

    fun create_view(){
        Log.e("1","1")

    var rcount=count

        var tva:Array<CardView> = Array(rcount!!+1,{j -> CardView(context!!)})

        var i=0
        while(i<tva.lastIndex) {
            var f=i
            f++
            val db:DocumentReference
            val adb:DocumentReference
            Log.e("val",f.toString())
            val linearcv=LinearLayout(getContext())
            linearcv.orientation=LinearLayout.VERTICAL
            Log.e("group",group)
            if(group.equals("a")){
                db = FirebaseFirestore.getInstance().collection("polls_group_a").document(f.toString())
            adb=FirebaseFirestore.getInstance().collection("result_a").document(f.toString())}
            else{
                db = FirebaseFirestore.getInstance().collection("polls_group_b").document(i.toString())
                adb=FirebaseFirestore.getInstance().collection("result_b").document(f.toString())
              }
            try{
               adb.get().addOnCompleteListener(){ds ->
                   db.get().addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           val ocount = Integer.parseInt(task.result.getString("count"))
                           Log.e("ocount",ocount.toString())
                           val quest= task.result.getString("question")
                           Log.e("quest",quest)
                           val questionView= TextView(context)
                           questionView.text=quest
                           var l=ocount
                           linearcv.addView(questionView)
                           var res=Array<String>(l+1,{i ->""})
                           var k=1
                           var t=ocount
                           var rbg= RadioGroup(context)
                           var radios:Array<RadioButton> = Array(t+1,{j -> RadioButton(context!!)  })
                           while(k<=radios.lastIndex){
                               res[k]= ds.result.getString(k.toString())
                               radios[k]=RadioButton(context)
                               val opt=task.result.getString(k.toString())
                               Log.e("opt",opt)
                               radios[k].text=opt+"  Current vote ="+res[k]
                               var r=Integer.parseInt(res[k])


                               var u=1
                               radios[k].setOnClickListener(){
                                   val mBook = HashMap< String,Any>()
                                   while(u<=ocount){
                                       var r=Integer.parseInt(res[u])
                                       r=r+1
                                   mBook.put(u.toString(),r.toString())
                                   u++}
                                   adb.set(mBook)
                                   var preferences = PreferenceManager.getDefaultSharedPreferences(context)
                                   var editor = preferences.edit()
                                   editor.putBoolean(i.toString(),true)
                                   editor.apply()
                                   disableEnableControls(false,linearcv)
                               }
                               rbg.addView(radios[k])


                               k++

                           }
                           linearcv.addView(rbg)
                           val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                           val status=preferences.getBoolean(i.toString(),false)
                           if(status==true){
                               disableEnableControls(false,linearcv)
                               Log.e("disabled","hghj")
                           }
                       } else{
                           Log.e("task","task failed")
                       }
                   }
               }

            }catch(e:Throwable){
                Log.e("throw",e.message)
            }
            tva[i].addView(linearcv)
            linear!!.addView(tva[i])


            i++

        }




   }

    private fun disableEnableControls(enable: Boolean, vg: ViewGroup) {
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            child.isEnabled = enable
            if (child is ViewGroup) {
                disableEnableControls(enable, child)
            }
        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {

        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment poll.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                poll().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
