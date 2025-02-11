package com.hadirahimi.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.hadirahimi.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var expression = StringBuilder()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bntCoin = findViewById<Button>(R.id.btnCoin)
        bntCoin.setOnClickListener {
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }

        // NoLimitScreen
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Inicializa os botões
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
                                expression.clear()
                                expression.append(result)
                                tvResult.text = result
                            } else {
                                tvResult.text = "Erro"
                            }
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
}











































