package com.example.projectmwspbaru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso

class DetailcardActivity : AppCompatActivity() {
    private lateinit var imageKatalog : ImageView
    private lateinit var buttonpindah : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailcard)

        //inisialisasikan
        imageKatalog = findViewById(R.id.imagedetail)
        buttonpindah = findViewById(R.id.buttonAktifkan)

        val getImage = intent.getStringExtra("image")
        Picasso.get()
            .load(getImage)
            .into(imageKatalog)
        buttonpindah.setOnClickListener{
        Toast.makeText(this, "detail tampil", Toast.LENGTH_SHORT).show()
    }


    }
}