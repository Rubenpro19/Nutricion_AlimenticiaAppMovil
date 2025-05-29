package com.example.loginfuncional2.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginfuncional2.model.Turno
import com.example.loginfuncional2.R


class TurnosAdapter(
    //private val turnos: List<Turno>,
    private var turnos: MutableList<Turno> = mutableListOf(),
    private val onEditarClickListener: (Turno) -> Unit,
    private val onEliminarClickListener: (Turno) -> Unit
) : RecyclerView.Adapter<TurnosAdapter.TurnoViewHolder>() {


    inner class TurnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFechaHora: TextView = itemView.findViewById(R.id.et_fecha_inicio)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        private val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        private val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        fun bind(turno: Turno) {
            tvFecha.text = turno.fecha // <-- Agrega esta línea
            tvFechaHora.text = "${turno.diaSemana} - ${turno.horaInicio} a ${turno.horaFin}"

            // Mostrar estado en texto y color
            tvEstado.text = if (turno.disponible) "DISPONIBLE" else "NO DISPONIBLE"
            val estadoBackground = if (turno.disponible) {
                R.drawable.bg_estado_pendiente
            } else {
                R.drawable.bg_estado_cancelado
            }
            tvEstado.setBackgroundResource(estadoBackground)

            // Puedes ocultar botones si no aplica
            if (!turno.disponible) {
                btnEditar.visibility = View.GONE
            }

            btnEditar.setOnClickListener { onEditarClickListener(turno) }
            btnEliminar.setOnClickListener { onEliminarClickListener(turno) }
        }
    }
    fun actualizarTurnos(nuevosTurnos: List<Turno>) {
        (turnos as MutableList).clear()
        (turnos as MutableList).addAll(nuevosTurnos)
        notifyDataSetChanged()
    }
    fun eliminarTurno(turno: Turno) {
        val index = turnos.indexOfFirst { it.id == turno.id }
        if (index != -1) {
            turnos.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun onBindViewHolder(holder: TurnoViewHolder, position: Int) {
        holder.bind(turnos[position])
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurnoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_turno, parent, false)
        return TurnoViewHolder(view)
    }
    override fun getItemCount(): Int = turnos.size
}