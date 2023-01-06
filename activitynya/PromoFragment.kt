package com.example.projectmwspbaru

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class PromoFragment : Fragment() {
    //variable untuk recycler view
    private lateinit var recyclerView: RecyclerView

    // variable untuk data userlist
    private lateinit var dataUserList: ArrayList<dataRecyclePromo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_promo, container, false)
        //inisialisasi recycler view
        recyclerView = view.findViewById(R.id.recyclePromo)

        //inisialisasi data user
        dataUserList = ArrayList<dataRecyclePromo>()

        //panggil function getdatahome()
        getdataPromo()

        return view
    }
    private fun getdataPromo() {
        AndroidNetworking.get("https://grocery-api.tonsu.site/products/promo")
//              .addJSONObjectBody(jsonObject) // posting json
            .setTag("test")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // do anything with response
                    try {
                        Log.d("ini respon", response.toString())
                        if (response?.getString("success").equals("true")) {
                            val getJSONArray = response.getJSONArray("data")
                            for (i in 0 until getJSONArray.length()) {
                                val getItem = getJSONArray.getJSONObject(i)
                                dataUserList.add(
                                    dataRecyclePromo(
                                        getItem.getString("image"),
                                        getItem.getString("product_name"),
                                        getItem.getString("unit"),
                                        getItem.getInt("original_price"),
                                        getItem.getInt("member_price"),
                                        getItem.getInt("id")
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
    private fun populateData(){
        val linearManager = LinearLayoutManager(activity)
        linearManager.reverseLayout = true
        linearManager.stackFromEnd = true
        recyclerView.layoutManager = linearManager
        val adp = activity?.let { AdapterRecyclePromo(it, dataUserList) }
        recyclerView.adapter = adp
    }

}

