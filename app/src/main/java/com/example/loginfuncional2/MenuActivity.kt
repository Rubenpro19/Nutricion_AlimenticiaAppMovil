package com.example.loginfuncional2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginfuncional2.adapter.CitaAdapter
import com.example.loginfuncional2.adapter.UsuarioAdapter
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Cita
import com.example.loginfuncional2.utilidades.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val sessionManager = SessionManager(this)

        val tvNombreUsuario = findViewById<TextView>(R.id.tvNombrePaciente)
        tvNombreUsuario.text = sessionManager.getUserName()
        val tvEmailUsuario = findViewById<TextView>(R.id.tvEmailPaciente)
        tvEmailUsuario.text = "Email: ${sessionManager.getUserEmail()}"

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout() {
        val sessionManager = SessionManager(this)
        sessionManager.clearToken()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}