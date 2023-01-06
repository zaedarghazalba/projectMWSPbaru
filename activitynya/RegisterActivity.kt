package com.example.projectmwspbaru

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.log


class RegisterActivity : AppCompatActivity() {
    private lateinit var inputNama: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var textLogin: TextView
    private lateinit var buttonPindah : Button
    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // inisialisasi
        buttonPindah = findViewById(R.id.btnDaftar)
        inputNama = findViewById(R.id.edNamaRegister)
        inputEmail = findViewById(R.id.edEmailRegister)
        inputPassword = findViewById(R.id.edPassRegister)
        textLogin = findViewById(R.id.Logiiin)
        checkBox = findViewById(R.id.checkbox1)

        // fungsikan text on klick login
        textLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)}



        //kondisi
        buttonPindah.setOnClickListener {
            val cek = checkBox.isChecked
            val jsonObject = JSONObject()
            try {
                jsonObject.put("name", inputNama.text.toString().trim())
                jsonObject.put("email", inputEmail.text.toString().trim())
                jsonObject.put("password", inputPassword.text.toString().trim())
                jsonObject.put("terms", if (cek) 1 else 0)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            AndroidNetworking.post("https://grocery-api.tonsu.site/auth/register")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response
                        try {
                            Log.d("ini respon",response.toString())
                            if (response?.getString("success").equals("true")){
                                val getMessage = response?.getString("message")
                                Toast.makeText(this@RegisterActivity,getMessage,Toast.LENGTH_SHORT).show()
                            }

                        }catch (eror : JSONException){
                            Toast.makeText(this@RegisterActivity,eror.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(error: ANError) {
                        // handle error
                    }
                })
        }

    }
}