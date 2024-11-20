package com.skripsi.dronesimulator.ui.status

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.retrofit.DroneTypes
import com.skripsi.dronesimulator.retrofit.DronesItem

class StatusAdapter(
    private var listDroneType: ArrayList<DroneTypes>,
    var onRiwayatClick : ((ArrayList<DronesItem?>?) -> Unit)? = null,
    var onPantauClick : ((ArrayList<DronesItem?>?) -> Unit)? = null,
    ) : RecyclerView.Adapter<StatusAdapter.DroneListViewHolder>() {

    private val expandedStates = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DroneListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drone, parent, false)
        return DroneListViewHolder(view)
    }

    // In the DroneAdapterNew, inside onBindViewHolder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DroneListViewHolder, position: Int) {
        val item = listDroneType[position]
        val isItemExpanded = expandedStates.get(position, false)

        holder.apply {
            droneTypeCode.text = item.typeCode
            droneTypeName.text =  "Type: ${item.typeName}"
            droneFlightEndurance.text = "Flight Endurance: Up to ${item.flightEndurance}minutes"
            droneMaxFlightRange.text = "Maximum Flight Range: Up to ${item.maxFlightRange}km"
            droneCruiseSpeed.text = "Cruise Speed: ${item.cruiseSpeed}m/s"
            droneMaxSpeed.text = "Maximum Speed: Up to ${item.maxSpeed}m/s"
            droneMaxHeight.text = "Maximum Flying Height: Up to ${item.maxHeight}m"
        }

        holder.itemView.setOnClickListener {
            expandedStates.put(position, !isItemExpanded)
            notifyItemChanged(position)
        }

        if(isItemExpanded){
            holder.apply {

                droneFlightEndurance.visibility = View.VISIBLE
                droneMaxFlightRange.visibility = View.VISIBLE
                droneCruiseSpeed.visibility = View.VISIBLE
                droneMaxSpeed.visibility = View.VISIBLE
                droneMaxHeight.visibility = View.VISIBLE
                btRiwayat.visibility = View.VISIBLE
                btPantau.visibility = View.VISIBLE
            }

        }else{
            holder.apply {
                droneFlightEndurance.visibility = View.GONE
                droneMaxFlightRange.visibility = View.GONE
                droneCruiseSpeed.visibility = View.GONE
                droneMaxSpeed.visibility = View.GONE
                droneMaxHeight.visibility = View.GONE
                btRiwayat.visibility = View.GONE
                btPantau.visibility = View.GONE
            }

        }

        holder.btPantau.setOnClickListener {
            onPantauClick?.invoke(item.drones)
        }

        holder.btRiwayat.setOnClickListener{
            onRiwayatClick?.invoke(item.drones)
        }
    }


    override fun getItemCount(): Int = listDroneType.size

    inner class DroneListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val droneTypeCode: TextView = itemView.findViewById(R.id.tv_typeCode)
        val droneTypeName: TextView = itemView.findViewById(R.id.tv_typeName)
        val droneFlightEndurance: TextView = itemView.findViewById(R.id.tv_flight_endurance)
        val droneMaxFlightRange: TextView = itemView.findViewById(R.id.tv_max_flight_range)
        val droneCruiseSpeed: TextView = itemView.findViewById(R.id.tv_cruise_speed)
        val droneMaxSpeed: TextView = itemView.findViewById(R.id.tv_max_speed)
        val droneMaxHeight: TextView = itemView.findViewById(R.id.tv_max_height)

        val btRiwayat: Button = itemView.findViewById(R.id.bt_lihat_riwayat)
        val btPantau: Button = itemView.findViewById(R.id.bt_pantau)
    }
}