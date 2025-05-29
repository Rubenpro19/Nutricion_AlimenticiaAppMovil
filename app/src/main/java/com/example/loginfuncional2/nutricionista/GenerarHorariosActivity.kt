package com.example.loginfuncional2.nutricionista

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginfuncional2.R
import com.example.loginfuncional2.database.AppDatabase
import com.example.loginfuncional2.model.Turno
import com.example.loginfuncional2.utilidades.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GenerarHorariosActivity : AppCompatActivity() {

    private lateinit var etFechaInicio: EditText
    private lateinit var etFechaFin: EditText
    private lateinit var etHoraInicio: EditText
    private lateinit var etHoraFin: EditText
    private lateinit var etDescansoInicio: EditText
    private lateinit var etDescansoFin: EditText
    private lateinit var btnGenerarHorarios: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_horarios)

        // Vinculación de vistas
        etFechaInicio = findViewById(R.id.et_fecha_inicio)
        etFechaFin = findViewById(R.id.et_fecha_fin)
        etHoraInicio = findViewById(R.id.et_hora_inicio)
        etHoraFin = findViewById(R.id.et_hora_fin)
        etDescansoInicio = findViewById(R.id.et_descanso_inicio)
        etDescansoFin = findViewById(R.id.et_descanso_fin)
        btnGenerarHorarios = findViewById(R.id.btn_generar_horarios)

        // Set Listeners
        etFechaInicio.setOnClickListener { showDatePicker(etFechaInicio) }
        etFechaFin.setOnClickListener { showDatePicker(etFechaFin) }

        etHoraInicio.setOnClickListener { showTimePicker(etHoraInicio) }
        etHoraFin.setOnClickListener { showTimePicker(etHoraFin) }
        etDescansoInicio.setOnClickListener { showTimePicker(etDescansoInicio) }
        etDescansoFin.setOnClickListener { showTimePicker(etDescansoFin) }

        btnGenerarHorarios.setOnClickListener {
            val fechaInicioStr = etFechaInicio.text.toString()
            val fechaFinStr = etFechaFin.text.toString()
            val horaInicioStr = etHoraInicio.text.toString()
            val horaFinStr = etHoraFin.text.toString()
            val descansoInicioStr = etDescansoInicio.text.toString()
            val descansoFinStr = etDescansoFin.text.toString()

            // Validación de campos vacíos
            if (fechaInicioStr.isEmpty() || fechaFinStr.isEmpty() ||
                horaInicioStr.isEmpty() || horaFinStr.isEmpty() ||
                descansoInicioStr.isEmpty() || descansoFinStr.isEmpty()
            ) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validaciones de fechas y horas
            val formatoFecha = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
            val fechaInicio = formatoFecha.parse(fechaInicioStr)
            val hoy = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            if (fechaInicio.before(hoy)) {
                Toast.makeText(this, "No puedes generar turnos en fechas pasadas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                val fechaInicio = formatoFecha.parse(fechaInicioStr)
                val fechaFin = formatoFecha.parse(fechaFinStr)
                val horaInicio = formatoHora.parse(horaInicioStr)
                val horaFin = formatoHora.parse(horaFinStr)
                val descansoInicio = formatoHora.parse(descansoInicioStr)
                val descansoFin = formatoHora.parse(descansoFinStr)

                // Validaciones lógicas
                if (fechaInicio.after(fechaFin)) {
                    Toast.makeText(this, "La fecha de inicio debe ser anterior o igual a la fecha de fin", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!horaInicio.before(horaFin)) {
                    Toast.makeText(this, "La hora de inicio debe ser anterior a la hora de fin", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (descansoInicio.before(horaInicio) || descansoFin.after(horaFin)) {
                    Toast.makeText(this, "El descanso debe estar dentro del rango de atención", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!descansoInicio.before(descansoFin)) {
                    Toast.makeText(this, "La hora de inicio del descanso debe ser anterior a la hora de fin del descanso", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                Toast.makeText(this, "Validaciones correctas. Listo para generar turnos.", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(this, "Formato de fecha u hora incorrecto", Toast.LENGTH_SHORT).show()
            }


            generarTurnosAutomaticos(
                fechaInicioStr, fechaFinStr,
                horaInicioStr, horaFinStr,
                descansoInicioStr, descansoFinStr
            )

        }

    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%02d/%02d/%02d", dayOfMonth, month + 1, year % 100)
                editText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(this,
            { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                editText.setText(selectedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    private fun generarTurnosAutomaticos(
        fechaInicioStr: String,
        fechaFinStr: String,
        horaInicioStr: String,
        horaFinStr: String,
        descansoInicioStr: String,
        descansoFinStr: String
    ) {
        val formatoFecha = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())

        val fechaInicio = formatoFecha.parse(fechaInicioStr)!!
        val fechaFin = formatoFecha.parse(fechaFinStr)!!
        val horaInicio = formatoHora.parse(horaInicioStr)!!
        val horaFin = formatoHora.parse(horaFinStr)!!
        val descansoInicio = formatoHora.parse(descansoInicioStr)!!
        val descansoFin = formatoHora.parse(descansoFinStr)!!
        if (fechaInicio == null || fechaFin == null || horaInicio == null || horaFin == null || descansoInicio == null || descansoFin == null) {
            Toast.makeText(this, "Formato de fecha u hora incorrecto", Toast.LENGTH_SHORT).show()
            return
        }

        val sessionManager = SessionManager(this)
        val idNutricionista = sessionManager.getUserId()
        val db = AppDatabase.getDatabase(this)
        val turnoDao = db.turnoDao()

        CoroutineScope(Dispatchers.IO).launch {
            val turnosAGuardar = mutableListOf<Turno>()
            val calendarioDia = Calendar.getInstance()
            calendarioDia.time = fechaInicio

            val ahora = Calendar.getInstance()

            while (!calendarioDia.time.after(fechaFin)) {
                val diaSemana = calendarioDia.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale("es")) ?: "Día"
                val calInicio = Calendar.getInstance().apply {
                    time = horaInicio
                    set(Calendar.YEAR, calendarioDia.get(Calendar.YEAR))
                    set(Calendar.MONTH, calendarioDia.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_MONTH, calendarioDia.get(Calendar.DAY_OF_MONTH))
                }
                val calFin = Calendar.getInstance().apply {
                    time = horaFin
                    set(Calendar.YEAR, calendarioDia.get(Calendar.YEAR))
                    set(Calendar.MONTH, calendarioDia.get(Calendar.MONTH))
                    set(Calendar.DAY_OF_MONTH, calendarioDia.get(Calendar.DAY_OF_MONTH))
                }
                val calDescansoInicio = Calendar.getInstance().apply { time = descansoInicio }
                val calDescansoFin = Calendar.getInstance().apply { time = descansoFin }

                while (calInicio.before(calFin)) {
                    // Validar que el turno no sea en el pasado
                    if (calInicio.after(ahora)) {
                        val horaActual = calInicio.time
                        if (horaActual.before(calDescansoInicio.time) || horaActual.after(calDescansoFin.time)) {
                            val horaInicioStrTurno = formatoHora.format(horaActual)
                            calInicio.add(Calendar.HOUR_OF_DAY, 1)
                            val horaFinStrTurno = formatoHora.format(calInicio.time)
                            val fechaTurno = formatoFecha.format(calendarioDia.time)

                            val existe = turnoDao.existeTurno(
                                idNutricionista,
                                fechaTurno,
                                horaInicioStrTurno,
                                horaFinStrTurno
                            )
                            if (!existe) {
                                val turno = Turno(
                                    fecha = fechaTurno,
                                    idNutricionista = idNutricionista,
                                    diaSemana = diaSemana,
                                    horaInicio = horaInicioStrTurno,
                                    horaFin = horaFinStrTurno
                                )
                                turnosAGuardar.add(turno)
                            }
                        } else {
                            calInicio.add(Calendar.HOUR_OF_DAY, 1)
                        }
                    } else {
                        calInicio.add(Calendar.HOUR_OF_DAY, 1)
                    }
                }
                calendarioDia.add(Calendar.DAY_OF_MONTH, 1)
            }

            if (turnosAGuardar.isNotEmpty()) {
                turnoDao.insertarTurnos(turnosAGuardar)
            }
            runOnUiThread {
                Toast.makeText(
                    this@GenerarHorariosActivity,
                    "Se guardaron ${turnosAGuardar.size} turnos nuevos",
                    Toast.LENGTH_LONG
                ).show()
                limpiarCamposYVolver()
            }
        }
    }

    private fun limpiarCamposYVolver() {
        etFechaInicio.text.clear()
        etFechaFin.text.clear()
        etHoraInicio.text.clear()
        etHoraFin.text.clear()
        etDescansoInicio.text.clear()
        etDescansoFin.text.clear()
        goToNutricionistaActivity()
    }
    private fun goToNutricionistaActivity() {
        // Aquí puedes iniciar la actividad principal o la actividad que desees
        val intent = Intent(this, NutricionistaActivity::class.java)
        startActivity(intent)
        finish()
    }

}