package com.example.loginfuncional2.nutricionista

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginfuncional2.R
import com.example.loginfuncional2.adapter.UsuarioAdapter
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListarPacientesActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: UsuarioAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_pacientes)
        listView = findViewById(R.id.listaUsuarios)
        cargarUsuarios()


    }

    private fun cargarUsuarios() {
        val db = AppDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()
        CoroutineScope(Dispatchers.IO).launch {
            usuarioDao.obtenerPacientes().collect { usuarios ->
                withContext(Dispatchers.Main) {
                    adapter = UsuarioAdapter(context = this@ListarPacientesActivity,
                        usuarios = usuarios,
                        onEliminarClick = {},
                        onEditarClick = {})
                    listView.adapter = adapter
                }
            }
        }
    }



}