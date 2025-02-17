package com.hadirahimi.calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.hadirahimi.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var expression = StringBuilder()
    private val PREFS_NAME = "CalcHistory"
    private val KEY_HISTORY = "history"
    private val KEY_HISTORY2 = "history2"
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resultRecebido = intent.getStringExtra("resultado")
        val formulaRecebido = intent.getStringExtra("formula")

        if (resultRecebido != null)
            binding.tvResult.text = resultRecebido
        if (formulaRecebido != null)
            binding.tvFormula.text = formulaRecebido

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        val bntCoin = findViewById<Button>(R.id.btnCoin)
        bntCoin.setOnClickListener {
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }

        val btnHistory = findViewById<Button>(R.id.btnHistory)
        btnHistory.setOnClickListener {
            val intent = Intent(this,History::class.java)
            startActivity(intent)
        }

        // NoLimitScreen
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.apply {
            binding.layoutMain.children.filterIsInstance<Button>().forEach { button ->
                button.setOnClickListener {
                    val buttonText = button.text.toString()
                    when {
                        buttonText.matches(Regex("[0-9+\\-*/.]")) -> {
                            expression.append(buttonText)
                            tvResult.text = expression.toString()
                        }
                        buttonText == "=" -> {
                            val result = evaluateExpression(expression.toString())
                            if (result != "Erro") {
                                tvFormula.text = expression.toString()
                                expression.append(result)
                                tvResult.text = result
                                val history = getHistory()
                                val history2 = getHistory2()
                                history.add(result)
                                history2.add(expression.toString())
                                expression.clear()
                                sharedPreferences.edit().putString(KEY_HISTORY, history.joinToString(";")).putString(KEY_HISTORY2, history2.joinToString(";")).apply()
                            } else
                                tvResult.text = "Erro"

                        }
                        buttonText == "C" -> {
                            expression.clear()
                            tvResult.text = "0"
                            tvFormula.text = ""
                        }
                    }
                }
            }
        }
    }

    // Avaliação de Expressão usando exp4j
    private fun evaluateExpression(expr: String): String {
        return try {
            val result = ExpressionBuilder(expr).build().evaluate()
            result.toString()
        } catch (e: Exception) {
            "Erro"
        }
    }

    private fun getHistory(): MutableList<String> {
        val historyString = sharedPreferences.getString(KEY_HISTORY, "")
        return if (historyString.isNullOrEmpty())
            mutableListOf()
        else
            historyString.split(";").toMutableList()
    }

    private fun getHistory2(): MutableList<String> {
        val historyString = sharedPreferences.getString(KEY_HISTORY2, "") // Corrigir KEY
        return if (historyString.isNullOrEmpty())
            mutableListOf()
        else
            historyString.split(";").toMutableList()
    }
}