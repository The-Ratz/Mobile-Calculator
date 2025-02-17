package com.hadirahimi.calculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        val sharedPreferences = getSharedPreferences("CalcHistory", Context.MODE_PRIVATE)
        val historyString = sharedPreferences.getString("history", "")
        val historyString2 = sharedPreferences.getString("history2", "")

        val historyList = if (historyString.isNullOrEmpty())
            mutableListOf<String>()
        else
            historyString.split(";").toMutableList()

        val historyList2 = if (historyString2.isNullOrEmpty())
            mutableListOf<String>()
        else
            historyString2.split(";").toMutableList()

        val listView = findViewById<ListView>(R.id.simpleListView)

        val combinedList = mutableListOf<Map<String, String>>()
        for (i in 0 until historyList.size)
            combinedList.add(mapOf("title" to historyList[i], "description" to historyList2.getOrElse(i) { "" }))

        // Configure o adapter
        val adapter = SimpleAdapter(this, combinedList, R.layout.list_text, arrayOf("title", "description"), intArrayOf(R.id.textTitle, R.id.textDescription))
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            // Pega do array ja criado os elementos da posição clicada na list view
            val item = combinedList[position]

            // Passe os dados para a main
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("resultado", item["title"])
                putExtra("formula", item["description"])
            }
            startActivity(intent)
        }

    }
}
