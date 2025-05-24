// UsuarioAdapter.kt
package com.example.loginfuncional2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.loginfuncional2.R
import com.example.loginfuncional2.model.Usuario

class UsuarioAdapter(
    private val context: Context,
    private val usuarios: List<Usuario>,
    private val onEliminarClick: (Usuario) -> Unit,
    private val onEditarClick: (Usuario) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = usuarios.size
    override fun getItem(position: Int): Any = usuarios[position]
    override fun getItemId(position: Int): Long = usuarios[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_usuario, parent, false)

        val tvNombre = view.findViewById<TextView>(R.id.tvNombre)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val btnEliminar = view.findViewById<Button>(R.id.btnEliminar)
        val btnEditar = view.findViewById<Button>(R.id.btnEditar)

        val usuario = usuarios[position]
        tvNombre.text = "${usuario.nombre} - ${usuario.rol}"
        tvEmail.text = usuario.email

        btnEliminar.setOnClickListener { onEliminarClick(usuario) }
        btnEditar.setOnClickListener { onEditarClick(usuario) }

        return view
    }
}
