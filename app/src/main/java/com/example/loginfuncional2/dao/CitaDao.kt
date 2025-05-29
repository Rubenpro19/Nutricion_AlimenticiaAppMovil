package com.example.loginfuncional2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.loginfuncional2.model.Cita
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {
    @Insert
    suspend fun insertarCita(cita: Cita)

    @Query("SELECT * FROM citas WHERE usuarioId = :idUsuario")
    fun obtenerCitasUsuario(idUsuario: Int): Flow<List<Cita>>

    @Query("SELECT * FROM citas WHERE fecha = :fecha AND hora = :hora LIMIT 1")
    fun obtenerCitaPorFechaYHora(fecha: String, hora: String): Cita?

    //mostrar la cita por el usuario loguado
    @Query("SELECT * FROM citas WHERE usuarioId = :idUsuario")
    fun obtenerCitasPorUsuario(idUsuario: Int): Flow<List<Cita>>

}