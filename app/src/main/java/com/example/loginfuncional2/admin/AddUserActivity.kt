package com.example.loginfuncional2.admin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
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

class AddUserActivity : AppCompatActivity() {
    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPassword2: EditText
    private lateinit var btnRegistro: Button
    private lateinit var spinnerRol: Spinner
    private lateinit var btnAtras: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPassword2 = findViewById(R.id.etPassword2)
        btnRegistro = findViewById(R.id.btnRegistro)
        btnAtras = findViewById(R.id.btnAtras)
        spinnerRol = findViewById(R.id.spinnerRol)
        val roles = arrayOf("Paciente", "Nutricionista", "Admin")
        spinnerRol.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,roles)

        btnRegistro.setOnClickListener { validarRegistro() }

        btnAtras.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }
    }

    private fun validarRegistro(){
        val rol = spinnerRol.selectedItem.toString()
        val nombre = etNombre.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val password2 = etPassword2.text.toString().trim()

        if (nombre.isEmpty()) { etNombre.error = "Campo requerido"; etNombre.requestFocus(); return }
        if (nombre.length < 8) { etNombre.error = "El nombre debe tener al menos 8 caracteres"; etNombre.requestFocus(); return }
        if (nombre.any { it.isDigit() }) { etNombre.error = "El nombre no debe contener números"; etEmail.requestFocus(); return }

        if (email.isEmpty()) { etEmail.error = "Campo requerido"; etEmail.requestFocus(); return }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { etEmail.error = "Ingrese un correo válido"; etEmail.requestFocus();return }

        if (password.isEmpty()) { etPassword.error = "Campo requerido"; etPassword.requestFocus(); return }
        if (password2.isEmpty()) { etPassword2.error = "Campo requerido"; etPassword2.requestFocus(); return }
        if (password != password2) { etPassword2.error = "Las contraseñas no coinciden"; etPassword2.requestFocus(); return }

        registrarUsuario(nombre, email, password, rol)
    }

    private fun registrarUsuario(nombre: String, email: String, password: String, rol: String) {
        val db = AppDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()

        CoroutineScope(Dispatchers.IO).launch {
            val existente = usuarioDao.buscarporEmail(email)
            if (existente != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                }
            } else {
                val hashedPassword = Seguridad.hashPassword(password)
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    email = email,
                    password = hashedPassword,
                    rol = rol)

                usuarioDao.insertar(nuevoUsuario)
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                }
            }
        }
    }

    private fun limpiarCampos() {
        etNombre.text.clear()
        etEmail.text.clear()
        etPassword.text.clear()
        etPassword2.text.clear()
    }
}