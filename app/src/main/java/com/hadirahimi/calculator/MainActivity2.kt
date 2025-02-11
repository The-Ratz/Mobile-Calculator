package com.hadirahimi.calculator

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val key = "5b6de8009178e5fdd4f103ae"
        val call = ApiClient.apiService.get(key, "USD")

        val spinner1: Spinner = findViewById(R.id.spinner1)
        val spinner2: Spinner = findViewById(R.id.spinner2)
        val button = findViewById<Button>(R.id.actionButton)
        val inputText = findViewById<EditText>(R.id.inputText)
        val outputText = findViewById<TextView>(R.id.outputText)

        call.enqueue(object : Callback<Coins> {
            override fun onResponse(call: Call<Coins>, response: Response<Coins>) {
                if (response.isSuccessful) {
                    val get = response.body()
                    val coins = get?.conversion_rates

                    val keysList = coins?.keys?.toList() ?: emptyList()

                    // Criando o adapter com as chaves
                    val adapter = ArrayAdapter(this@MainActivity2, R.layout.spinner_list, keysList)
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

                    spinner1.adapter = adapter
                    spinner2.adapter = adapter
                }
            }

            override fun onFailure(call: Call<Coins>, t: Throwable) {
                // Lidar com falha
            }
        })

        button.setOnClickListener {
            val input = inputText.text.toString().toDouble()
            val coin1 = spinner1.selectedItem?.toString() ?: ""
            val coin2 = spinner2.selectedItem?.toString() ?: ""

            val call2 = ApiClient.apiService.get(key, coin1)

            call2.enqueue(object : Callback<Coins> {
                override fun onResponse(call2: Call<Coins>, response: Response<Coins>) {
                    if (response.isSuccessful) {
                        val get = response.body()
                        val result = input * (get?.conversion_rates?.get(coin2) ?: 0.0)

                        outputText.text = result.toString()
                    }
                }

                override fun onFailure(call: Call<Coins>, t: Throwable) {
                    // Lidar com falha
                }
            })
        }

    }
}