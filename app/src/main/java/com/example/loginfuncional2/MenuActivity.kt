package com.example.loginfuncional2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.utilidades.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

class MenuActivity : AppCompatActivity() {

    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvEmailUsuario: TextView
    private lateinit var tvTelefonoUsuario: TextView
    private lateinit var tvCedulaUsuario: TextView
    private lateinit var tvFechaNacimiento: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val sessionManager = SessionManager(this)

        tvNombreUsuario = findViewById(R.id.tvNombrePaciente)
        tvEmailUsuario = findViewById(R.id.tvEmailPaciente)
        tvTelefonoUsuario = findViewById(R.id.tvTelefonoPaciente)
        tvCedulaUsuario = findViewById(R.id.tvCedulaPaciente)
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener { logout() }

        val btnPerfil = findViewById<ImageButton>(R.id.btnPerfil)
        btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("usuario_id", sessionManager.getUserId())
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnReservarCita).setOnClickListener{
                startActivity(Intent(this, ReservarCitaActivity::class.java))
        }

        findViewById<Button>(R.id.btnReservarCita).setOnClickListener{
            startActivity(Intent(this, ReservarCitaActivity::class.java))
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

    override fun onResume() {
        super.onResume()
        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()
        if (userId != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                val usuario = AppDatabase.getDatabase(this@MenuActivity).usuarioDao().obtenerUsuarioPorId(userId)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val edad = usuario?.fechaNacimiento?.let { fechaNacimiento ->
                    val nacimiento = Calendar.getInstance().apply { time = fechaNacimiento }
                    val hoy = Calendar.getInstance()
                    var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)
                    if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
                        edad--
                    }
                    edad
                }

                val fechaNacimientoTexto = usuario?.fechaNacimiento?.let { dateFormat.format(it) } ?: "No disponible"

                withContext(Dispatchers.Main) {
                    tvNombreUsuario.text = usuario?.nombre ?: "No disponible"
                    tvEmailUsuario.text = "Email: ${usuario?.email ?: "No disponible"}"
                    tvTelefonoUsuario.text = "Teléfono: ${usuario?.telefono ?: "No disponible"}"
                    tvCedulaUsuario.text = "Cédula: ${usuario?.cedula ?: "No disponible"}"
                    tvFechaNacimiento.text = "Fecha de Nacimiento: $fechaNacimientoTexto"
                    findViewById<TextView>(R.id.tvEdadPaciente).text = "Edad: ${edad ?: "No disponible"} años"
                }
            }
        }
    }
}