package com.example.loginfuncional2.utilidades

import org.mindrot.jbcrypt.BCrypt

object Seguridad {

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12))
    }

    fun verificarPassword(plainPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(plainPassword, hashedPassword)
    }
}