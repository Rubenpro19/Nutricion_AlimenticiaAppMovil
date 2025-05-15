package com.example.loginfuncional2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfuncional2.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.loginfuncional2.utilidades.Seguridad

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btniniciosesion: Button
    private lateinit var tvToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btniniciosesion = findViewById(R.id.btniniciosesion)
        tvToRegister = findViewById(R.id.tv_ToRegister)

        btniniciosesion.setOnClickListener{
            validarInicio()
        }

        tvToRegister.setOnClickListener {
            goToRegister()
        }
    }

    private fun validarInicio() {
        val correo = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        if (correo.isEmpty()) {
            etEmail.error = "Campo requerido"
            return
        }

        if (password.isEmpty()) {
            etPassword.error = "Campo requerido"
            return
        }

        // Validación real con Room
        val db = AppDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()

        CoroutineScope(Dispatchers.IO).launch {
            val hashedPassword = Seguridad.hashPassword(password)
            val usuario = usuarioDao.login(correo, hashedPassword)

            withContext(Dispatchers.Main) {
                if (usuario != null) {
                    Toast.makeText(applicationContext, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    goToMenu()
                } else {
                    Toast.makeText(applicationContext, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToRegister(){
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    private fun goToMenu(){
        val i = Intent(this, MenuActivity::class.java)
        startActivity(i)
        finish()
    }
}