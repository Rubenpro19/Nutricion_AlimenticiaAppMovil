package com.example.loginfuncional2.utilidades

import android.app.Activity
import android.content.Intent
import com.example.loginfuncional2.*
import com.example.loginfuncional2.nutricionista.NutricionistaActivity

object RedirigirSegunSesion {

    fun redirigirSegunSesion(activity: Activity) {
        val sessionManager = SessionManager(activity)
        if (sessionManager.isLoggedIn()) {
            val userId = sessionManager.getUserId()
            val userRole = sessionManager.getUserRole()
            val intent = when (userRole) {
                "Admin" -> Intent(activity, AdminActivity::class.java)
                "Paciente" -> Intent(activity, MenuActivity::class.java)
                "Nutricionista" -> Intent(activity, NutricionistaActivity::class.java)
                else -> Intent(activity, MenuActivity::class.java)
            }
            intent.putExtra("USER_ID", userId)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}
