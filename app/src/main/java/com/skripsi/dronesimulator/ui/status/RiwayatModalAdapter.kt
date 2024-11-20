package com.skripsi.dronesimulator.ui.status

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.retrofit.DronesItem

class RiwayatModalAdapter(
    private val drones: ArrayList<DronesItem?>?,
    var onItemCLick: ((DronesItem) -> Unit)? = null,

    ) : RecyclerView.Adapter<RiwayatModalAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val droneName: TextView = itemView.findViewById(R.id.drone_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drone_unit, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = drones!![position]
        holder.droneName.text = item!!.droneName

        holder.itemView.setOnClickListener {
            onItemCLick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = drones!!.size

}
