package com.example.projectmwspbaru

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AdapterRecyclePreviewHome(val context : Context, val datapreviewhome: ArrayList<dataRecyclePreviewHome>) : RecyclerView.Adapter<AdapterRecyclePreviewHome.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.fetch_preview_home,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = datapreviewhome [position]
        // Get ID dan Image
        val getId = currentItem.id
        val getImage = currentItem.images

        Picasso.get()
            .load(getImage)
            .into(holder.imagePreview)
        holder.imagePreview.setOnClickListener{
            val intent = Intent(context,DetailcardActivity ::class.java)
            intent.putExtra("image",getImage)
            context.startActivity(intent)
        }
        holder.button.setOnClickListener {
            // Remove the item from the data source
            datapreviewhome.removeAt(position)
            // Notify the adapter that the item has been removed
            notifyItemRemoved(position)

            // Notify the adapter of the change in the data set
            notifyItemRangeChanged(position, datapreviewhome.size)

        }
    }

    override fun getItemCount(): Int {
        return datapreviewhome.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imagePreview : ImageView = itemView.findViewById(R.id.photoPreview)
        val button: Button =itemView.findViewById(R.id.hapusPreview)

    }

}