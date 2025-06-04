package com.example.loginfuncional2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Usuario
import com.example.loginfuncional2.utilidades.Seguridad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PerfilActivity : AppCompatActivity() {

    private var usuarioId: Int = -1
    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPassword2: EditText
    private lateinit var etCedula: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnAtras: Button
    private var fechaNacimientoDate: Date? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPassword2 = findViewById(R.id.etPassword2)
        etCedula = findViewById(R.id.etCedula)
        etTelefono = findViewById(R.id.etTelefono)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnAtras = findViewById(R.id.btnAtras)

        usuarioId = intent.getIntExtra("usuario_id", -1)
        if (usuarioId == -1) {
            Toast.makeText(this, "Error al cargar usuario", Toast.LENGTH_SHORT).show()
            finish()
        }

        cargarDatosUsuario()

        btnGuardar.setOnClickListener { actualizarUsuario() }
        btnAtras.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        etFechaNacimiento.setOnClickListener {
            mostrarDatePicker()
        }
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()
        fechaNacimientoDate?.let { calendar.time = it }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, y, m, d ->
            calendar.set(y, m, d)
            fechaNacimientoDate = calendar.time
            etFechaNacimiento.setText(dateFormat.format(fechaNacimientoDate!!))
        }, year, month, day)
        datePicker.show()
    }

    private fun cargarDatosUsuario() {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(this@PerfilActivity).usuarioDao()
            val usuario = dao.obtenerUsuarioPorId(usuarioId)
            withContext(Dispatchers.Main) {
                usuario?.let {
                    etNombre.setText(it.nombre)
                    etEmail.setText(it.email)
                    etCedula.setText(it.cedula)
                    etTelefono.setText(it.telefono)
                    fechaNacimientoDate = it.fechaNacimiento
                    etFechaNacimiento.setText(it.fechaNacimiento?.let { dateFormat.format(it) } ?: "")
                }
            }
        }
    }

    private fun actualizarUsuario() {
        val nombre = etNombre.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val password2 = etPassword2.text.toString().trim()
        val cedula = etCedula.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        if (nombre.isEmpty()) { etNombre.error = "Campo requerido"; etNombre.requestFocus(); return }
        if (nombre.length < 8) { etNombre.error = "El nombre debe tener al menos 8 caracteres"; etNombre.requestFocus(); return }
        if (nombre.any { it.isDigit() }) { etNombre.error = "El nombre no debe contener números"; etNombre.requestFocus(); return }

        if (email.isEmpty()) { etEmail.error = "Campo requerido"; etEmail.requestFocus(); return }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { etEmail.error = "Ingrese un correo válido"; etEmail.requestFocus(); return }

        if (password != password2) { etPassword2.error = "Las contraseñas no coinciden"; etPassword2.requestFocus(); return }

        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getDatabase(this@PerfilActivity).usuarioDao()
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
                    cedula = cedula,
                    telefono = telefono,
                    fechaNacimiento = fechaNacimientoDate,
                    rol = usuarioActual.rol
                )
                dao.actualizar(usuarioActualizado)

                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
