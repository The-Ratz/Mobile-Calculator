package com.hadirahimi.calculator

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

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

        lateinit var lineChart: LineChart
        lateinit var xValues: List<String>

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

            lineChart = findViewById(R.id.chart)

            // Oculta os rótulos do eixo direito
            lineChart.axisRight.setDrawLabels(false)

            // Valores do eixo X
            xValues = listOf("Nadun", "Kamal", "Jhon", "Jerry")

            val xAxis: XAxis = lineChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
            xAxis.labelCount = 4
            xAxis.granularity = 1f

            // Configuração do eixo Y
            val yAxis: YAxis = lineChart.axisLeft
            yAxis.axisMinimum = 0f
            yAxis.axisMaximum = 100f
            yAxis.axisLineWidth = 2f
            yAxis.axisLineColor = Color.WHITE
            yAxis.setLabelCount(10, false)

            // Dados para o conjunto "Maths"
            val entries1 = arrayListOf(
                Entry(0f, 60f),
                Entry(1f, 70f),
                Entry(2f, 85f),
                Entry(3f, 95f)
            )

            // Dados para o conjunto "Science"
            val entries2 = arrayListOf(
                Entry(0f, 50f),
                Entry(1f, 85f),
                Entry(2f, 65f),
                Entry(3f, 80f)
            )

            // Configurando os conjuntos de dados
            val dataSet1 = LineDataSet(entries1, "Maths").apply {
                color = Color.BLUE
            }
            val dataSet2 = LineDataSet(entries2, "Science").apply {
                color = Color.RED
            }

            // Configurando os dados do gráfico
            val lineData = LineData(dataSet1, dataSet2)
            lineChart.data = lineData

            // Atualiza o gráfico
            lineChart.invalidate()
        }

    }
}