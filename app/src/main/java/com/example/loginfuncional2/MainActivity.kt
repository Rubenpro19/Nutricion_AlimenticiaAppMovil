package com.example.loginfuncional2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Usuario
import com.example.loginfuncional2.utilidades.RedirigirSegunSesion
import com.example.loginfuncional2.utilidades.Seguridad
import com.example.loginfuncional2.utilidades.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btniniciosesion: Button
    private lateinit var tvToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crearUsuarioAdminPorDefecto {
            RedirigirSegunSesion.redirigirSegunSesion(this)

            setContentView(R.layout.activity_main)

            etEmail = findViewById(R.id.etEmail)
            etPassword = findViewById(R.id.etPassword)
            btniniciosesion = findViewById(R.id.btniniciosesion)
            tvToRegister = findViewById(R.id.tv_ToRegister)

            btniniciosesion.setOnClickListener { validarInicio() }
            tvToRegister.setOnClickListener { goToRegister() }
        }
    }

    private fun validarCampos(): Boolean {
        val correo = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        if (correo.isEmpty()) { etEmail.error = "Campo requerido"; etEmail.requestFocus(); return false }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etEmail.error = "Ingrese un correo válido"
            etEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) { etPassword.error = "Campo requerido"; etPassword.requestFocus(); return false }
        return true
    }

    private fun validarInicio() {
        if (!validarCampos()) return
        val correo = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        val db = AppDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()

        CoroutineScope(Dispatchers.IO).launch {
            val usuario = usuarioDao.buscarporEmail(correo)
            withContext(Dispatchers.Main) {
                if (usuario != null) {
                    val passwordValida = Seguridad.verificarPassword(password, usuario.password)
                    if (passwordValida) {
                        val sessionManager = SessionManager(this@MainActivity)
                        sessionManager.saveToken(correo)
                        sessionManager.saveUserId(usuario.id)
                        sessionManager.saveUserRole(usuario.rol)
                        sessionManager.saveUserName(usuario.nombre) // <- Guardamos el nombre aquí
                        sessionManager.saveUserEmail(usuario.email) // <- Guardamos el email aquí
                        Toast.makeText(applicationContext, "Bienvenido, ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                        RedirigirSegunSesion.redirigirSegunSesion(this@MainActivity)
                    } else {
                        Toast.makeText(applicationContext, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun crearUsuarioAdminPorDefecto(callback: () -> Unit) {
        val db = AppDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()

        CoroutineScope(Dispatchers.IO).launch {
            val existente = usuarioDao.buscarporEmail("admin@gmail.com")
            if (existente == null) {
                val contrasenaHasheada = Seguridad.hashPassword("admin123")
                val admin = Usuario(
                    nombre = "Administrador",
                    email = "admin@gmail.com",
                    password = contrasenaHasheada,
                    rol = "Admin"

                )
                usuarioDao.insertar(admin)
            }

            withContext(Dispatchers.Main) {
                callback()
            }
        }
    }

    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }
}