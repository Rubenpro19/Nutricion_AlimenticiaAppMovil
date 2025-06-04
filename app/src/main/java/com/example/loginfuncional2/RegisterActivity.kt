package com.example.loginfuncional2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Usuario
import com.example.loginfuncional2.utilidades.Seguridad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPassword2: EditText
    private lateinit var btnRegistro: Button
    private lateinit var tvLogin: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPassword2 = findViewById(R.id.etPassword2)
        btnRegistro = findViewById(R.id.btnRegistro)
        tvLogin = findViewById(R.id.tv_go_to_login)

        btnRegistro.setOnClickListener {
            validarRegistro()
        }

        tvLogin.setOnClickListener{
            goToLogin()
        }
    }

    private fun validarRegistro(){
        val nombre = etNombre.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val password2 = etPassword2.text.toString().trim()

        if (nombre.isEmpty()) { etNombre.error = "Campo requerido"; etNombre.requestFocus(); return }
        if (nombre.length < 6) { etNombre.error = "El nombre debe tener al menos 8 caracteres"; etNombre.requestFocus(); return }
        if (nombre.any { it.isDigit() }) { etNombre.error= "El nombre no debe contener números"; etNombre.requestFocus(); return }

        if (email.isEmpty()) { etEmail.error = "Campo requerido"; etEmail.requestFocus(); return }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { etEmail.error = "Ingrese un correo válido"; etEmail.requestFocus(); return }

        if (password.isEmpty()) { etPassword.error = "Campo requerido"; etPassword.requestFocus(); return }
        if (password2.isEmpty()) { etPassword2.error = "Campo requerido"; etPassword2.requestFocus(); return }
        if (password != password2) { etPassword2.error = "Las contraseñas no coinciden"; return }

        registrarUsuario(nombre, email, password)
    }

    private fun registrarUsuario(nombre: String, email: String, password: String) {
        val db = AppDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()

        CoroutineScope(Dispatchers.IO).launch {
            val existente = usuarioDao.buscarporEmail(email)
            if (existente != null) {
                withContext(Dispatchers.Main) {
                    etEmail.error = "El correo ya está registrado"; etEmail.requestFocus()
                }
            } else {
                val hashedPassword = Seguridad.hashPassword(password)
                val nuevoUsuario = Usuario(nombre = nombre, email = email, password = hashedPassword, rol = "Paciente")
                usuarioDao.insertar(nuevoUsuario)
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    goToLogin()
                }
            }
        }
    }

    private fun goToLogin(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}