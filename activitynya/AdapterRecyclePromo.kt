package com.example.projectmwspbaru

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class AdapterRecyclePromo(val context : Context, val datapromo : ArrayList<dataRecyclePromo>) : RecyclerView.Adapter<AdapterRecyclePromo.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fetch_promo,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = datapromo [position]
//sesuaikan dengan class data
//        var image : String,
//        var product_name : String,
//        var unit : String,
//        var original_price : Int,
//        var member_price : Int,
//        var id : Int

        val getId = currentItem.id
        val getImage = currentItem.image
//        val getProdukname = currentItem.product_name
//        val getUnit = currentItem.unit
        val getOriginal = currentItem.original_price
        val getMemberprice = currentItem.member_price



        holder.product_name.text = currentItem.product_name
        holder.unit.text = currentItem.unit
        holder.hargaori.text = currentItem.original_price.toString()
        holder.hargamember.text = currentItem.member_price.toString()


        Picasso.get()
            .load(getImage)
            .into(holder.imageitem)

        holder.aktifkan.setOnClickListener {
            // Remove the item from the data source
            Toast.makeText(context, "berhasil aktifkan", Toast.LENGTH_SHORT).show()
            val jsonObject = JSONObject()
            try {
                jsonObject.put("product_id", getId.toString().trim())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            //get token
            val sharedPreferences = context.getSharedPreferences("GET_TOKEN", Context.MODE_PRIVATE)
            val getToken = sharedPreferences.getString("token", null)
            val editornya = sharedPreferences.edit()
            editornya.putString("token",getToken)
            editornya.commit()
            AndroidNetworking.post("https://grocery-api.tonsu.site/products/activate")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .addHeaders("token", "Bearer" + " " + getToken)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response
                        try {
                            Log.d("ini respon", response.toString())
                            if (response?.getString("success").equals("true")) {
                                val getMessage = response?.getString("message")
                            }
                        } catch (eror: JSONException) {
                            Toast.makeText(context, eror.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    override fun onError(error: ANError) {
                        // handle error
                    }
                })
        }

    }

    override fun getItemCount(): Int {
        return datapromo.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageitem : ImageView = itemView.findViewById(R.id.imageItemPromo)
        val product_name : TextView = itemView.findViewById(R.id.text1)
        val unit : TextView = itemView.findViewById(R.id.text2)
        val hargaori : TextView = itemView.findViewById(R.id.text3)
        val hargamember : TextView = itemView.findViewById(R.id.text4)
        val aktifkan : Button = itemView.findViewById(R.id.buttonAktifkan)
    }

}