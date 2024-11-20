package com.skripsi.dronesimulator.ui.riwayat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.retrofit.DroneHistoryResponseItem

class RiwayatAdapter(
    private var riwayatList: ArrayList<DroneHistoryResponseItem>,
    var onRiwayatClick: ((DroneHistoryResponseItem) -> Unit)? = null
) : RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val riwayat = riwayatList[position]

        holder.apply {
            tvDescription.text = riwayat.description
            tvDate.text = "Tanggal: ${riwayat.date}"
            tvLocation.text = "Lokasi: ${riwayat.location}"

            itemView.setOnClickListener {
                onRiwayatClick?.invoke(riwayat)
            }
        }
    }

    override fun getItemCount(): Int = riwayatList.size

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_location)
    }
}