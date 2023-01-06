package com.example.projectmwspbaru

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var buttonPindah: Button
    private lateinit var textPass: EditText
    private lateinit var textRegist: TextView
    private lateinit var textEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // inisialisasi
        buttonPindah = findViewById(R.id.btnLogin)
        textRegist = findViewById(R.id.Registrasi)
        textPass = findViewById(R.id.edPassLogin)
        textEmail = findViewById(R.id.edEmailLogin)

        // fungsikan text regist
        textRegist.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonPindah.setOnClickListener {
            val jsonObject = JSONObject()
            try {
                jsonObject.put("email", textEmail.text.toString().trim())
                jsonObject.put("password", textPass.text.toString().trim())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            AndroidNetworking.post("https://grocery-api.tonsu.site/auth/login")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response
                        try {
                            Log.d("ini respon", response.toString())
                            if (response?.getString("success").equals("true")) {
                                val getMessage = response?.getString("message")
                                Toast.makeText(this@LoginActivity, getMessage, Toast.LENGTH_SHORT)
                                    .show()
                                //get token
                                val getToken = response?.getString("token")
                                val preferenceShared =getSharedPreferences("GET_TOKEN",Context.MODE_PRIVATE)
                                val editornya = preferenceShared.edit()
                                editornya.putString("token",getToken)
                                editornya.putString("nama","zaedar")
                                editornya.commit()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                            }

                        } catch (eror: JSONException) {
                            Toast.makeText(this@LoginActivity, eror.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    override fun onError(error: ANError) {
                        // handle error
                    }
                })
        }
    }
}