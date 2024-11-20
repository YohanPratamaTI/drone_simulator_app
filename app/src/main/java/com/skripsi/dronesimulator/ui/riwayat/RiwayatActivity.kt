package com.skripsi.dronesimulator.ui.riwayat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.skripsi.dronesimulator.Utils.DRONE_ITEM
import com.skripsi.dronesimulator.Utils.DRONE_HISTORY
import com.skripsi.dronesimulator.Utils.dateFormat
import com.skripsi.dronesimulator.Utils.dateFormatArgs
import com.skripsi.dronesimulator.databinding.ActivityRiwayatBinding
import com.skripsi.dronesimulator.retrofit.DronesItem
import com.skripsi.dronesimulator.viewmodel.MainViewModel
import com.skripsi.dronesimulator.viewmodel.ViewModelFactory
import java.util.Date


class RiwayatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatBinding
    private lateinit var adapter: RiwayatAdapter
    private var selectedDateRange: String? = null

    private lateinit var dronesItem: DronesItem
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Riwayat"

        //get drone from intent
        dronesItem = intent.getParcelableExtra<DronesItem>(DRONE_ITEM)!!

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {

        val recyclerView = binding.recyclerView

        mainViewModel= ViewModelProvider(
            this,
            ViewModelFactory(this)
        )[MainViewModel::class.java]

        mainViewModel.getHistoriesById(dronesItem.droneId!!).observe(this){
            if(it?.isEmpty() == true){
                binding.tvNoData.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
            else{
                adapter = RiwayatAdapter(it!!)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                adapter.onRiwayatClick = {history ->
                    val intent = Intent(this, RiwayatMapActivity::class.java)
                    intent.putExtra(DRONE_HISTORY, history)
                    startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupAction() {
        binding.tvDroneName.text = dronesItem.droneName

        // Set the click listener for the date range picker
        binding.etCalendar.setEndIconOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Pilih Rentang Waktu")
                .build()

            dateRangePicker.show(supportFragmentManager, "dateRangePicker")

            dateRangePicker.addOnPositiveButtonClickListener { dateRange ->
                val startDate = dateRange.first
                val endDate = dateRange.second

                Log.e("startDate", startDate.toString())

                val startDateFormatted = dateFormat.format(Date(startDate))
                val endDateFormatted = dateFormat.format(Date(endDate))

                val startDateArg = dateFormatArgs.format(Date(startDate))
                val endDateArg = dateFormatArgs.format(Date(endDate))

                selectedDateRange = "$startDateFormatted - $endDateFormatted"
                binding.tvSelectedRange.setText(selectedDateRange)

                mainViewModel.getHistoriesByIdRange(dronesItem.droneId!!, startDateArg, endDateArg).observe(this){
                    if (it!!.isNotEmpty()) {
                        val recyclerView = binding.recyclerView
                        adapter = RiwayatAdapter(it)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this)

                        adapter.onRiwayatClick = {history ->
                            val intent = Intent(this, RiwayatMapActivity::class.java)
                            intent.putExtra(DRONE_HISTORY, history)
                            startActivity(intent)
                        }

                    } else {
                        Toast.makeText(this, "Tidak ada riwayat pada rentang waktu tersebut", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
}