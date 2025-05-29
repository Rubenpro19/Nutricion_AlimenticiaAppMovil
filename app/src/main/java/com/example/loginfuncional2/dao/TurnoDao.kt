package com.example.loginfuncional2.dao

import androidx.room.*
import com.example.loginfuncional2.model.Turno

@Dao
interface TurnoDao {

    @Insert
    suspend fun insertarTurno(turno: Turno)

    @Insert
    suspend fun insertarTurnos(turnos: List<Turno>)

//    @Query("SELECT * FROM turnos WHERE idNutricionista = :idNutricionista")
//    fun obtenerTurnosPorNutricionista(idNutricionista: Int): Flow<List<Turno>>

    @Query("SELECT * FROM turnos WHERE idNutricionista = :idNutricionista AND diaSemana = :dia AND horaInicio = :hora")
    suspend fun obtenerTurnoPorHorario(idNutricionista: Int, dia: String, hora: String): Turno?

    @Query("DELETE FROM turnos WHERE idNutricionista = :idNutricionista")
    suspend fun eliminarTurnosPorNutricionista(idNutricionista: Int)

    @Update
    suspend fun actualizarTurno(turno: Turno)

    @Query("SELECT * FROM turnos WHERE idNutricionista = :idNutricionista")
    suspend fun obtenerTurnosPorNutricionista(idNutricionista: Int): List<Turno>

    @Query("DELETE FROM turnos WHERE id = :id")
    suspend fun eliminarTurno(id: Int)

    @Query("DELETE FROM turnos WHERE id = :id")
    suspend fun eliminarTurnoPorId(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM turnos WHERE idNutricionista = :idNutricionista AND fecha = :fecha AND horaInicio = :horaInicio AND horaFin = :horaFin)")
    suspend fun existeTurno(idNutricionista: Int, fecha: String, horaInicio: String, horaFin: String): Boolean

    @Query("SELECT * FROM turnos WHERE (fecha > :hoy) OR (fecha = :hoy AND horaInicio > :horaActual)")
    fun obtenerTurnosDisponibles(hoy: String, horaActual: String): List<Turno>

    @Query("SELECT * FROM turnos WHERE idNutricionista = :idNutricionista AND (fecha > :hoy OR (fecha = :hoy AND horaFin > :horaActual))")
    fun obtenerTurnosActualesPorNutricionista(idNutricionista: Int, hoy: String, horaActual: String): List<Turno>
}