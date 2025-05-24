package com.example.loginfuncional2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.loginfuncional2.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios") //Consulta los datos de la tabla usuario
    fun obtenerUsuario(): Flow<List<Usuario>> //Se devuelve los datos en tiempo real

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Query("DELETE FROM usuarios WHERE id = :idUsuario")
    suspend fun eliminarPorId(idUsuario: Int)

    @Query("SELECT * FROM usuarios WHERE id = :userId LIMIT 1")
    suspend fun obtenerUsuarioPorId(userId: Int): Usuario?

    @Query("SELECT * FROM usuarios WHERE rol != 'Admin'")
    fun obtenerUsuariosNoAdmin(): Flow<List<Usuario>>

    @Update
    suspend fun actualizar(usuario: Usuario)


}