package com.example.projectmwspbaru

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class MemberFragment : Fragment() {
    //variable untuk recycler view
    private lateinit var recyclerView: RecyclerView

    // variable untuk data userlist
    private lateinit var dataMemberList: ArrayList<dataRecycleMember>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_member, container, false)
//inisialisasi recycler view

        recyclerView = view.findViewById(R.id.recycleMember)

        //inisialisasi data user
        dataMemberList = ArrayList<dataRecycleMember>()

        getdataMember()
        return view
    }
    private fun getdataMember() {
        //get token
        val sharedPreferences = activity?.getSharedPreferences("GET_TOKEN", Context.MODE_PRIVATE)
        val getToken = sharedPreferences?.getString("token", null)
        val editornya = sharedPreferences?.edit()
        if (editornya != null) {
            editornya.putString("token",getToken)
        }
        if (editornya != null) {
            editornya.commit()
        }
        AndroidNetworking.get("https://grocery-api.tonsu.site/products/cart")
            //      .addJSONObjectBody(jsonObject) // posting json
            .setTag("test")
            .addHeaders("token", "Bearer" + " " + getToken)
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // do anything with response
                    try {
                        Log.d("ini responkkk", response.toString())
                        if (response?.getString("success").equals("true")) {
                            val getJSONArray = response.getJSONArray("data")
                            for (i in 0 until getJSONArray.length()) {
                                val getItem = getJSONArray.getJSONObject(i)
                                dataMemberList.add(
                                    dataRecycleMember(
                                        getItem.getInt("product_id"),
                                        getItem.getString("image"),
                                        getItem.getString("product_name"),
                                        getItem.getInt("member_price"),
                                        getItem.getInt("original_price"),
                                        getItem.getString("discount_price")
                                )
                                )
                            }
                            //panggil function populateddata() disini
                            populateData()
                        }

                    } catch (eror: JSONException) {
                        Toast.makeText(activity, eror.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onError(error: ANError) {
                    // handle error
                }
            })
    }

    private fun populateData() {
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = layoutManager
        val adp = activity?.let {  AdapterRecycleMember(it, dataMemberList) }
        recyclerView.adapter = adp
    }

}