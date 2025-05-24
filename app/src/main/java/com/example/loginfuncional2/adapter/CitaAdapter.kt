package com.example.loginfuncional2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loginfuncional2.R
import com.example.loginfuncional2.model.Cita

class CitaAdapter(private val citas: List<Cita>) : RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {
    class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCita: TextView = itemView.findViewById(R.id.tvCita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]
        holder.tvCita.text = "Fecha: ${cita.fecha} - Hora: ${cita.hora}"
    }

    override fun getItemCount() = citas.size
}