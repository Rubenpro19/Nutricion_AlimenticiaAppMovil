package com.example.loginfuncional2.admin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfuncional2.AdminActivity
import com.example.loginfuncional2.R
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Usuario
import com.example.loginfuncional2.utilidades.Seguridad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditUserActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var spinnerRol: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnAtras: Button
    private var usuarioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        spinnerRol = findViewById(R.id.spinnerRol)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnAtras = findViewById(R.id.btnAtras)

        val roles = arrayOf("Paciente", "Nutricionista", "Admin")
        spinnerRol.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        usuarioId = intent.getIntExtra("usuario_id", -1)
        if (usuarioId == -1) {
            Toast.makeText(this, "Error al cargar usuario", Toast.LENGTH_SHORT).show()
            finish()
        }

        cargarDatosUsuario()

        btnGuardar.setOnClickListener { actualizarUsuario() }
        btnAtras.setOnClickListener{ goToAtras()}
    }

    private fun cargarDatosUsuario() {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(this@EditUserActivity).usuarioDao()
            val usuario = dao.obtenerUsuarioPorId(usuarioId)
            withContext(Dispatchers.Main) {
                usuario?.let {
                    etNombre.setText(it.nombre)
                    etEmail.setText(it.email)
                    spinnerRol.setSelection((spinnerRol.adapter as ArrayAdapter<String>).getPosition(it.rol))
                }
            }
        }
    }

    private fun actualizarUsuario() {
        val nombre = etNombre.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val rol = spinnerRol.selectedItem.toString()

        if (nombre.isEmpty()) {
            etNombre.error = "Campo requerido"
            return
        }

        if (nombre.length < 8) {
            etNombre.error = "El nombre debe tener al menos 8 caracteres"
            return
        }
        if (nombre.any { it.isDigit() }) {
            etNombre.error = "El nombre no debe contener números"
            return
        }

        if (email.isEmpty()) {
            etEmail.error = "Campo requerido"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Ingrese un correo válido"
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(this@EditUserActivity).usuarioDao()

            val usuarioConMismoEmail = dao.buscarporEmail(email)
            val usuarioActual = dao.obtenerUsuarioPorId(usuarioId)

            if (usuarioConMismoEmail != null && usuarioConMismoEmail.id != usuarioId) {
                withContext(Dispatchers.Main) {
                    etEmail.error = "Este correo ya está en uso por otro usuario"
                    etEmail.requestFocus()
                }
                return@launch
            }

            if (usuarioActual != null) {
                val usuarioActualizado = Usuario(
                    id = usuarioActual.id,
                    nombre = nombre,
                    email = email,
                    password = if (password.isNotBlank()) Seguridad.hashPassword(password) else usuarioActual.password,
                    rol = rol
                )
                dao.actualizar(usuarioActualizado)

                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private  fun goToAtras(){
        val regreso = Intent(this, AdminActivity::class.java)
        startActivity(regreso)
        finish()
    }

}
