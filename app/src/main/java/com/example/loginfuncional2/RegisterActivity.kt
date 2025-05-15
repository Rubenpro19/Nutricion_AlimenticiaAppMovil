package com.example.loginfuncional2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.loginfuncional2.utilidades.Seguridad


class RegisterActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPassword2: EditText
    private lateinit var btnRegistro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPassword2 = findViewById(R.id.etPassword2)
        btnRegistro = findViewById(R.id.btnRegistro)

        btnRegistro.setOnClickListener {
            validarRegistro()
        }

        val tvToRegister = findViewById<TextView>(R.id.tv_go_to_login)
        tvToRegister.setOnClickListener{
            goToLogin()
        }
    }

    private fun goToLogin(){
        val l = Intent(this, MainActivity::class.java)
        startActivity(l)
        finish()
    }

    private fun validarRegistro(){
        val nombre = etNombre.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val password2 = etPassword2.text.toString().trim()

        if (nombre.isEmpty()) {
            etNombre.setError ("Campo requerido")
            return
        }
        if (nombre.length < 8) {
            etNombre.setError ("El nombre debe tener al menos 8 caracteres")
            return
        }
        if (nombre.any { it.isDigit() }) {
            etNombre.setError ("El nombre no debe contener números")
            return
        }

        if (email.isEmpty()) {
            etEmail.setError ("Campo requerido")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError ("Ingrese un correo válido")
            return
        }

        if (password.isEmpty()) {
            etPassword.setError("Campo requerido")
            return
        }
        if (password.length < 8) {
            etPassword.setError ("La contraseña debe tener al menos 8 caracteres")
            return
        }
        if (password2.isEmpty()) {
            etPassword2.setError("Campo requerido")
            return
        }
        if (password != password2) {
            etPassword2.setError("Las contraseñas no coinciden")
            return
        }

        registrarUsuario(nombre, email, password)

    }

    private fun registrarUsuario(nombre: String, email: String, password: String) {
        val db = AppDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()

        CoroutineScope(Dispatchers.IO).launch {
            val existente = usuarioDao.buscarPorEmail(email)
            if (existente != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                }
            } else {
                val hashedPassword = Seguridad.hashPassword(password)
                val nuevoUsuario = Usuario(nombre = nombre, email = email, password = hashedPassword)
                usuarioDao.insertar(nuevoUsuario)
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    goToLogin()
                }
            }
        }
    }
}