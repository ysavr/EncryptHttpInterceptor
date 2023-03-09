package com.mobile.encrypthttpinterceptor

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.mobile.encrypthttpinterceptor.network.ApiCall.apiService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initApi()
    }

    private fun initApi() {
        val bodyParams = HashMap<String, String>()
        bodyParams["name"] = "robert"
        lifecycleScope.launch {
            try {
                val response = apiService().apiDemo(bodyParams)
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    Log.d("MainActivity", "response result: $data")
                }
            } catch (e: java.lang.Exception) {
                Log.e("MainActivity", "response error: ${e.message}")
            }
        }
    }
}