package com.example.loginfuncional2.dao

import androidx.room.*
import com.example.loginfuncional2.model.Turno
import kotlinx.coroutines.flow.Flow

@Dao
interface TurnoDao {

    @Insert
    suspend fun insertarTurno(turno: Turno)

    @Insert
    suspend fun insertarTurnos(turnos: List<Turno>)

    @Query("SELECT * FROM turnos WHERE idNutricionista = :idNutricionista")
    fun obtenerTurnosPorNutricionista(idNutricionista: Int): Flow<List<Turno>>

    @Query("SELECT * FROM turnos WHERE idNutricionista = :idNutricionista AND diaSemana = :dia AND horaInicio = :hora")
    suspend fun obtenerTurnoPorHorario(idNutricionista: Int, dia: String, hora: String): Turno?

    @Query("DELETE FROM turnos WHERE idNutricionista = :idNutricionista")
    suspend fun eliminarTurnosPorNutricionista(idNutricionista: Int)

    @Update
    suspend fun actualizarTurno(turno: Turno)
}
