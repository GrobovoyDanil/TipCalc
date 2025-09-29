package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tipcalc.ui.theme.TipCalcTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalcTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipCalculatorScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TipCalculatorScreen(modifier: Modifier = Modifier) {
    var billAmount by remember { mutableStateOf("") }
    var dishesCount by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableStateOf(0f) }
    var selectedDiscount by remember { mutableStateOf(0) } // 0- ниче, 1-3, 2-5, 3-7, 4-10

    val bill = billAmount.toDoubleOrNull() ?: 0.0 // на float ругаеться
    val dishes = dishesCount.toIntOrNull() ?: 0


    LaunchedEffect(dishesCount) { // выполняется при изменении кол-ва блюд
        selectedDiscount = when {
            dishes in 1..2 -> 1
            dishes in 3..5 -> 2
            dishes in 6..10 -> 3
            dishes > 10 -> 4
            else -> 0
        }
    }
    val discountPercentage = when (selectedDiscount) {
        1 -> 3.0
        2 -> 5.0
        3 -> 7.0
        4 -> 10.0
        else -> 0.0
    }

    val tipAmount = bill * (tipPercentage / 100)
    val discountAmount = bill * (discountPercentage / 100)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Сумма заказа:")
            TextField(
                value = billAmount,
                onValueChange = { billAmount = it },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Количество блюд:")
            TextField(
                value = dishesCount,
                onValueChange = { dishesCount = it },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Чаевые: ${tipPercentage.toInt()}%")
        Slider(
            value = tipPercentage,
            onValueChange = { tipPercentage = it },
            valueRange = 0f..25f,
            steps = 25,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(text = "Скидка:")
            RadioButton(selected = selectedDiscount == 1, onClick = null)
            Text("3%")
            RadioButton(selected = selectedDiscount == 2, onClick = null)
            Text("5%")
            RadioButton(selected = selectedDiscount == 3, onClick = null)
            Text("7%")
            RadioButton(selected = selectedDiscount == 4, onClick = null)
            Text("10%")
        }

        Spacer(modifier = Modifier.height(20.dp))

        var totalAmount = bill + tipAmount - discountAmount

        Text(text = "Сумма чаевых: $tipAmount")
        Text(text = "Сумма скидки: $discountAmount")
        Text(text = "Итоговая сумма: $totalAmount")
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalcTheme {
        TipCalculatorScreen()
    }
}