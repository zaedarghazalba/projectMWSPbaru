package com.example.projectmwspbaru

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class SettingsFragment : Fragment() {
    private lateinit var mImageView1: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // inisialisasi
        val EditProfile = view.findViewById(R.id.btnUbahProfile) as CardView
        val RiwayatActive = view.findViewById(R.id.btnRiwayatActivate) as CardView
        val tentangKami = view.findViewById(R.id.btnTentangKami) as CardView
        val DetailRiwayat = view.findViewById(R.id.btnDetailRiwayat) as CardView
        val Keluar = view.findViewById(R.id.btnKeluar) as CardView


//        // fungsikan btn
//        EditProfile.setOnClickListener {
//            val intent = Intent(context, EditProfileActivity::class.java)
//            startActivity(intent)
//        }
//        tentangKami.setOnClickListener {
//            val intent = Intent(context, TentangkamiActivity::class.java)
//            startActivity(intent)
//        }
//        RiwayatActive.setOnClickListener {
//            val intent = Intent(context, RiwayatActiveActivity::class.java)
//            startActivity(intent)
//        }
//        DetailRiwayat.setOnClickListener {
//            val intent = Intent(context, RecycleDetailRiwayat::class.java)
//            startActivity(intent)
//        }
        Keluar.setOnClickListener {
            activity?.finishAffinity()
        }

        mImageView1 = view.findViewById(R.id.imageProfile)
        mImageView1.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        getDataSettings()
        return view
    }
    private fun getDataSettings() {
        val preferenceShared = activity?.getSharedPreferences("GET_TOKEN", Context.MODE_PRIVATE)
        val getToken = preferenceShared?.getString("token", null)
        AndroidNetworking.get("https://grocery-api.tonsu.site/members")
            .setTag("test")
            .addHeaders("token", "Bearer" + " " + getToken)
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // do anything with response
                    Log.d("Ini respon settings", response?.getJSONObject("data").toString())
                    val respon: JSONObject? = response?.getJSONObject("data")
                    try {
                        val namaSetting = view?.findViewById(R.id.namaSetting) as TextView
                        val emailSetting = view?.findViewById(R.id.emailSetting) as TextView
                        val imgSetting = view?.findViewById(R.id.imageProfile) as ImageView
                        namaSetting.text = respon?.getString("name")
                        emailSetting.text = respon?.getString("email")

                        val image = respon?.getString("photo_profile")
                        Log.d("Ini foto profil", image.toString())
                        Picasso.get()
                            .load(image)
                            .into(imgSetting)
                    } catch (e : JSONException) {
                        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onError(error: ANError) {
                    // handle error
                }
            })
    }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            mImageView1.setImageURI(imageUri)
        }
    }
}
