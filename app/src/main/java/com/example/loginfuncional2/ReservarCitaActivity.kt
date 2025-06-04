package com.example.loginfuncional2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.loginfuncional2.adapter.UsuarioAdapter
import com.example.loginfuncional2.dao.UsuarioDao
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Usuario
import com.example.loginfuncional2.utilidades.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReservarCitaActivity : AppCompatActivity() {

    private lateinit var spnNutricionistas: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reservar_cita)

        spnNutricionistas = findViewById(R.id.spnNutricionistas)

        lifecycleScope.launch {
            val usuarioDao = AppDatabase.getDatabase(this@ReservarCitaActivity).usuarioDao()
            usuarioDao.obtenerNutricionistas().collectLatest { listaNutricionistas ->
                val nombresNutricionistas = listaNutricionistas.map { it.nombre }
                val adapter = ArrayAdapter(
                    this@ReservarCitaActivity,
                    android.R.layout.simple_spinner_item,
                    nombresNutricionistas
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                spnNutricionistas.adapter = adapter
            }
        }

        findViewById<Button>(R.id.btnAtras).setOnClickListener{
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }

        findViewById<Button>(R.id.btnDesplegarTurnos).setOnClickListener{

        }
    }


}