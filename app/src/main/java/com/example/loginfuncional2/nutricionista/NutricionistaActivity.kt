package com.example.loginfuncional2.nutricionista

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginfuncional2.R
import com.example.loginfuncional2.nutricionista.GenerarHorariosActivity

class NutricionistaActivity : AppCompatActivity() {
    private lateinit var btnGenerarHorarios : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutricionista)

        btnGenerarHorarios = findViewById(R.id.btnGenerarHorarios)

        btnGenerarHorarios.setOnClickListener{
            val intent = Intent(this, GenerarHorariosActivity::class.java)
            startActivity(intent)
        }
    }
}