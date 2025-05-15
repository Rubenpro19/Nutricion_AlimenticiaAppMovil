package com.example.loginfuncional2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.loginfuncional2.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {
    private lateinit var tvUsuarios: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        val tvGoMenu = findViewById<TextView>(R.id.tv_go_to_atrasmenu)
        tvGoMenu.setOnClickListener {
            goToMenu()
        }
        tvUsuarios = findViewById(R.id.tvUsuarios)

        mostrarUsuarios()
    }

    private fun mostrarUsuarios(){
        val db = AppDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()

        lifecycleScope.launch {
            usuarioDao.obtenerUsuario().collect{ lista ->
                val texto = lista.joinToString("\n") {
                    "Nombre: ${it.nombre}, Email: ${it.email}, Contraseña: ${it.password} \n"
                }
                tvUsuarios.text = texto
            }
        }
    }
    private fun goToMenu() {
        val m = Intent(this, MainActivity::class.java)
        startActivity(m)
    }
}