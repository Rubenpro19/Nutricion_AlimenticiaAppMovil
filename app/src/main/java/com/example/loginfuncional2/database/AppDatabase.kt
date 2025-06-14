package com.example.loginfuncional2.database

import android.content.Context
import com.example.loginfuncional2.model.Usuario
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.example.loginfuncional2.dao.CitaDao
import com.example.loginfuncional2.dao.TurnoDao
import com.example.loginfuncional2.dao.UsuarioDao
import com.example.loginfuncional2.model.Cita
import com.example.loginfuncional2.model.Turno
import com.example.loginfuncional2.utilidades.DateTypeConverter
import androidx.room.TypeConverters

@Database(entities = [Usuario::class, Cita::class, Turno ::class], version = 17, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun citaDao(): CitaDao
    abstract fun turnoDao(): TurnoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prueba2.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}