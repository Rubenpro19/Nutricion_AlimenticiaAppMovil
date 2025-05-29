package com.example.loginfuncional2.nutricionista

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginfuncional2.MainActivity
import com.example.loginfuncional2.R
import com.example.loginfuncional2.utilidades.SessionManager

class NutricionistaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutricionista)

        // Obtener el nombre guardado en sesión
        val sessionManager = SessionManager(this)
        val nombreUsuario = sessionManager.getUserName() ?: "Usuario"
        findViewById<TextView>(R.id.tvNombreNutricionista).text = nombreUsuario

        val rolUsuario = sessionManager.getUserRole()
        findViewById<TextView>(R.id.tvEspecialidad).text = when(rolUsuario) {
            "Admin" -> "Administrador"
            "Nutricionista" -> "Nutricionista Certificado"
            else -> "Profesional de la salud"
        }

        val btnLogout = findViewById<Button>(R.id.btnCerrarSesion) //btnCerrarSesion
        btnLogout.setOnClickListener {
            logout()
        }

        val btnGenerarHorarios = findViewById<Button>(R.id.btnGenerarHorarios)
        btnGenerarHorarios.setOnClickListener {
            val intent = Intent(this, GenerarHorariosActivity::class.java)
            startActivity(intent)
        }
        val btnVerTurnos = findViewById<Button>(R.id.btnVerTurnos)
        btnVerTurnos.setOnClickListener {
            val i = Intent(this, VerTurnosActivity::class.java)
            startActivity(i)
            finish()
        }

        var btnListarPacientes = findViewById<Button>(R.id.btnListaPacientes)
        btnListarPacientes.setOnClickListener {
            val intent = Intent(this, ListarPacientesActivity::class.java)
            startActivity(intent)
        }


    }


    private fun logout() {
        val sessionManager = SessionManager(this)
        sessionManager.clearToken() // Elimina la sesión

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}