package com.example.loginfuncional2.database

import android.content.Context
import com.example.loginfuncional2.model.Usuario
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.loginfuncional2.dao.UsuarioDao

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao //Metodo para acceder a la interfaz UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Tierra.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}