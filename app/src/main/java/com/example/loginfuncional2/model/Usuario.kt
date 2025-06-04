package com.example.loginfuncional2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.material.color.ColorRoles
import java.util.Date

@Entity(tableName = "usuarios")
data class Usuario(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val cedula: String ? = null,
    val telefono: String ? = null,
    val fechaNacimiento: Date ? = null,
    val rol: String
)