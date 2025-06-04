package com.example.loginfuncional2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.loginfuncional2.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao{
    @Insert //insertar un registro en l tabla usuarios
    suspend fun insertar (usuario: Usuario) //se ejecuta en backgraud con corountines

    @Query("Select * from usuarios") //consulta los datos de la tabla usuarios
    fun obtenerusuarios(): Flow<List<Usuario>> // se devuelve los datos en tiempo real

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): Usuario?
    // Se utiliza LIMIT 1 para asegurarse de que solo se devuelva un usuario, en caso de que haya duplicados

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarporEmail(email: String): Usuario?

    //editar un registro en la tabla usuarios
    @Query("UPDATE usuarios SET nombre = :nombre, email = :email, password = :password WHERE id = :id")
    suspend fun editarUsuario(id: Int, nombre: String, email: String, password: String)

    //eliminar un usuario
    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun eliminarUsuario(id: Int)

    // NUEVO: obtener usuarios que no sean administradores
    @Query("SELECT * FROM usuarios WHERE rol != 'Admin'")
    fun obtenerUsuariosNoAdmin(): Flow<List<Usuario>>

    // NUEVO: obtener usuarios por rol Paciente
    @Query("SELECT * FROM usuarios WHERE rol = 'Paciente'")
    fun obtenerPacientes(): Flow<List<Usuario>>

    // NUEVO: obtener usuarios por rol Nutricionista
    @Query("SELECT * FROM usuarios WHERE rol = 'Nutricionista'")
    fun obtenerNutricionistas(): Flow<List<Usuario>>

    // NUEVO: eliminar usuario por id (alias para claridad)
    @Query("DELETE FROM usuarios WHERE id = :id")
    suspend fun eliminarPorId(id: Int)

    @Query("SELECT * FROM usuarios WHERE id = :userId LIMIT 1")
    suspend fun obtenerUsuarioPorId(userId: Int): Usuario?

    @Update
    suspend fun actualizar(usuario: Usuario)

}