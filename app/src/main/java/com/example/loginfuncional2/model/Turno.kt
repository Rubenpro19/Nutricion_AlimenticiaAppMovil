package com.example.loginfuncional2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "turnos")
data class Turno(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idNutricionista: Int,     // Relación con el usuario
    val diaSemana: String,        // Ej: "Lunes", "Martes"
    val horaInicio: String,       // Ej: "08:00"
    val horaFin: String,          // Ej: "09:00"
    val disponible: Boolean = true
)
