package com.skripsi.dronesimulator.ui.status

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.retrofit.DronesItem

class PantauModalAdapter(
    private val drones: ArrayList<DronesItem?>?,
    var onItemCLick: ((DronesItem) -> Unit)? = null,
    var getOfflineMsg: (()-> Unit)? = null,

    ) : RecyclerView.Adapter<PantauModalAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val droneName: TextView = itemView.findViewById(R.id.drone_name)
        val statusIcon: ImageView = itemView.findViewById(R.id.iv_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drone_unit, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val drone = drones!![position]!!
        holder.droneName.text = drone.droneName
        if(drone.isOnline == false ){
            holder.statusIcon.setImageResource(R.drawable.baseline_airplanemode_inactive_24)
            holder.itemView.setOnClickListener {
                getOfflineMsg?.invoke()
            }
        }else{
            holder.statusIcon.setImageResource(R.drawable.baseline_airplanemode_active_24)
            holder.itemView.setOnClickListener {
                onItemCLick?.invoke(drone)
            }
        }

    }

    override fun getItemCount(): Int = drones!!.size

}
