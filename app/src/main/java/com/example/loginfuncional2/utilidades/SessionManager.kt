package com.example.loginfuncional2

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveToken(username: String) {
        prefs.edit().putString("TOKEN", username).apply()
    }

    fun getToken(): String? {
        return prefs.getString("TOKEN", null)
    }

    fun clearToken() {
        prefs.edit().remove("TOKEN").apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun saveUserId(userId: Int) {
        val editor = prefs.edit()
        editor.putInt("user_id", userId)
        editor.apply()
    }

    fun getUserId(): Int {
        return prefs.getInt("user_id", -1) // -1 si no existe
    }

    fun saveUserRole(role: String) {
        prefs.edit().putString("user_role", role).apply()
    }

    fun getUserRole(): String? {
        return prefs.getString("user_role", null)
    }
}