package com.example.loginfuncional2.nutricionista

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfuncional2.MainActivity
import com.example.loginfuncional2.R
import com.example.loginfuncional2.utilidades.SessionManager

class NutricionistaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutricionista)

        val sessionManager = SessionManager(this)
        val nombreUsuario = sessionManager.getUserName() ?: "Usuario"

        findViewById<TextView>(R.id.tvNombreNutricionista).text = nombreUsuario

        val rolUsuario = sessionManager.getUserRole()
        findViewById<TextView>(R.id.tvEspecialidad).text = when(rolUsuario) {
            "Admin" -> "Administrador"
            "Nutricionista" -> "Nutricionista Certificado"
            else -> "Profesional de la salud"
        }

        // Botón para cerrar sesión
        findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener { logout() }

        // Botón para Generar Horarios
        findViewById<Button>(R.id.btnGenerarHorarios).setOnClickListener {
            startActivity(Intent(this, GenerarHorariosActivity::class.java))
            finish()
        }

        // Botón para ver turnos
        findViewById<Button>(R.id.btnVerTurnos).setOnClickListener {
            startActivity(Intent(this, VerTurnosActivity::class.java))
            finish()
        }

        // Botón para ver pacientes
        findViewById<Button>(R.id.btnListaPacientes).setOnClickListener {
            startActivity(Intent(this, ListarPacientesActivity::class.java))
            finish()
        }
    }

    private fun logout() {
        SessionManager(this).clearToken() //Elimina la sesión
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //Con estas líneas se asegura que la actividad anterior no quede en el stack
        })
        finish()
    }
}