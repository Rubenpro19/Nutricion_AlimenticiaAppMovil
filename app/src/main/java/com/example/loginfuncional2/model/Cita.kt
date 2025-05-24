package com.example.loginfuncional2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cita")
data class Cita(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val fecha: String,
    val hora: String,
)