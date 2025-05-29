package com.example.loginfuncional2.nutricionista

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginfuncional2.R
import com.example.loginfuncional2.adapter.TurnosAdapter
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Turno
import com.example.loginfuncional2.utilidades.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class VerTurnosActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TurnosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_turnos)

        recyclerView = findViewById(R.id.recyclerTurnos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa el adapter con una lista vacía
        adapter = TurnosAdapter(
            onEditarClickListener = { turno -> editarTurno(turno) },
            onEliminarClickListener = { turno -> eliminarTurno(turno) }
        )
        recyclerView.adapter = adapter

        // Luego carga los turnos desde la base de datos y actualiza el adapter
        val db = AppDatabase.getDatabase(this)
        val turnoDao = db.turnoDao()
        val sessionManager = SessionManager(this)
        val idNutricionista = sessionManager.getUserId()

        val formatoFecha = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
        val hoy = formatoFecha.format(Date())
        val horaActual = formatoHora.format(Date())

        CoroutineScope(Dispatchers.IO).launch {
            val listaTurnos = turnoDao.obtenerTurnosActualesPorNutricionista(idNutricionista, hoy, horaActual)
            runOnUiThread {
                adapter.actualizarTurnos(listaTurnos)
            }
        }
//        CoroutineScope(Dispatchers.IO).launch {
//            val listaTurnos = turnoDao.obtenerTurnosPorNutricionista(idNutricionista)
//            runOnUiThread {
//                adapter.actualizarTurnos(listaTurnos)
//            }
//        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Si quieres regresar al panel del nutricionista al presionar atrás
        val intent = Intent(this, NutricionistaActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun editarTurno(turno: Turno) {
        Toast.makeText(this, "Editar: ${turno.diaSemana}", Toast.LENGTH_SHORT).show()
        // TODO: Agrega lógica real para editar

    }

    private fun eliminarTurno(turno: Turno) {
        Toast.makeText(this, "Eliminar: ${turno.diaSemana}", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@VerTurnosActivity)
            val turnoDao = db.turnoDao()
            turnoDao.eliminarTurnoPorId(turno.id)
            runOnUiThread {
                adapter.eliminarTurno(turno)
                Toast.makeText(this@VerTurnosActivity, "Turno eliminado", Toast.LENGTH_SHORT).show()
            }
        }
    }



}