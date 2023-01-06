package com.example.projectmwspbaru

import android.content.Context
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
import kotlin.math.max


class AdapterRecycleMember(val context : Context, private val datamember : ArrayList<dataRecycleMember>) : RecyclerView.Adapter<AdapterRecycleMember.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fetch_membergrid,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = datamember [position]

        val getId = currentItem.id
        val getImage = currentItem.image
        val getProdukname = currentItem.product_name
        val getOriginal = currentItem.original_price
        val getMemberprice = currentItem.member_price



        holder.namaitem1.text = currentItem.product_name
        holder.oriprice.text = currentItem.original_price.toString()
        holder.memberprice.text = currentItem.member_price.toString()

        Picasso.get()
            .load(getImage)
            .into(holder.imageitem1)



        //untuk button plus
        holder.plusss.setOnClickListener {
            var quantitynya = holder.totalmember.text.toString().toInt()
            holder.totalmember.text = (quantitynya + 1).toString()
            val getId = currentItem.id
            val jsonObject = JSONObject()
            var quantity = (quantitynya + 1)
            try {
                jsonObject.put("product_id", getId.toString().trim())
                jsonObject.put("quantity",quantity++)
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
                            Log.d("ini respon tambah ", response.toString())
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

        //untuk button minus
        holder.minesss.setOnClickListener {

            val quantitynya = holder.totalmember.text.toString().toInt()
            holder.totalmember.text = max(quantitynya - 1,0).toString()
            val getId = currentItem.id
            val jsonObject = JSONObject()
            var quantity = max(quantitynya - 1,0)
            try {
                jsonObject.put("product_id", getId.toString().trim())
                jsonObject.put("quantity",quantity--)

                if (holder.totalmember.text == "0"){
                    // Remove the item from the data source
                    datamember.removeAt(position)
                    // Notify the adapter that the item has been removed
                    notifyItemRemoved(position)
                    // Notify the adapter of the change in the data set
                    notifyItemRangeChanged(position, datamember.size)
                }

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
                            Log.d("ini respon kurang ", response.toString())
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

        //untuk button delate catalog
        holder.hapus.setOnClickListener {
            // Remove the item from the data source
            datamember.removeAt(position)
            // Notify the adapter that the item has been removed
            notifyItemRemoved(position)
            // Notify the adapter of the change in the data set
            notifyItemRangeChanged(position, datamember.size)

            Toast.makeText(context, "berhasil hapus", Toast.LENGTH_SHORT).show()
            val getId = currentItem.id
            val jsonObject = JSONObject()
            val quantity = 0
            try {
                jsonObject.put("product_id", getId.toString().trim())
                jsonObject.put("quantity",quantity)
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
                            Log.d("ini respon hapus ", response.toString())
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
        return datamember.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageitem1 : ImageView = itemView.findViewById(R.id.img_person1)
        val namaitem1 : TextView = itemView.findViewById(R.id.text1)
        val memberprice : TextView = itemView.findViewById(R.id.text2)
        val oriprice : TextView = itemView.findViewById(R.id.text3)
        val total1 : TextView = itemView.findViewById(R.id.text4)
        val plusss : Button = itemView.findViewById(R.id.pluss)
        val minesss : Button = itemView.findViewById(R.id.mines)
        val hapus : ImageView = itemView.findViewById(R.id.hapusmember)
        val totalmember : TextView = itemView.findViewById(R.id.totalmember)
    }

}