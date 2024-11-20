package com.skripsi.dronesimulator.ui.status

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.Utils.DRONE_ITEM
import com.skripsi.dronesimulator.Utils.DRONE_NAME
import com.skripsi.dronesimulator.databinding.FragmentStatusBinding
import com.skripsi.dronesimulator.ui.riwayat.RiwayatActivity
import com.skripsi.dronesimulator.viewmodel.MainViewModel
import com.skripsi.dronesimulator.viewmodel.ViewModelFactory


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StatusFragment : Fragment() {
    private lateinit var binding: FragmentStatusBinding
    private lateinit var adapter: StatusAdapter
    private lateinit var adapterRiwayatModal: RiwayatModalAdapter
    private lateinit var adapterPantauModal: PantauModalAdapter

    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title =  "Status Fragment"
        binding = FragmentStatusBinding.inflate(inflater, container, false)
        setupViewModel()
        setupAction()
        return binding.root
    }

    private fun setupViewModel() {
        mainViewModel= ViewModelProvider(
            this,
            ViewModelFactory(requireActivity())
        )[MainViewModel::class.java]

        mainViewModel.getAllDrones().observe(viewLifecycleOwner){
            mainViewModel.listDroneTypes.postValue(it)
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun setupAction() {

        mainViewModel.listDroneTypes.observe(viewLifecycleOwner){

            val recyclerView = binding.recyclerView
            adapter = StatusAdapter(it!!)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())

            adapter.onRiwayatClick = {drones ->

                val dialog = BottomSheetDialog(requireActivity())
                val view = layoutInflater.inflate(R.layout.bottom_dialog, null)

                val recyclerView2 = view.findViewById<RecyclerView>(R.id.recyclerView)
                adapterRiwayatModal = RiwayatModalAdapter(drones)
                recyclerView2.adapter = adapterRiwayatModal
                recyclerView2.layoutManager = LinearLayoutManager(requireActivity())

                adapterRiwayatModal.onItemCLick = {drone ->
                    val intent = Intent(requireActivity(), RiwayatActivity::class.java)
                    intent.putExtra(DRONE_ITEM,drone)
                    startActivity(intent)
                }

                val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                val btnClose = view.findViewById<ImageView>(R.id.iv_close)

                tvTitle.text = "Pilih riwayat penugasan drone"

                btnClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setContentView(view)
                dialog.show()
            }

            adapter.onPantauClick = { drones ->

                val dialog = BottomSheetDialog(requireActivity())
                val view = layoutInflater.inflate(R.layout.bottom_dialog, null)

                val recyclerView2 = view.findViewById<RecyclerView>(R.id.recyclerView)
                adapterPantauModal = PantauModalAdapter(drones)
                recyclerView2.adapter = adapterPantauModal
                recyclerView2.layoutManager = LinearLayoutManager(requireActivity())

                adapterPantauModal.onItemCLick = {drone ->
                    val intent = Intent(requireActivity(), PantauActivity::class.java)
                    intent.putExtra(DRONE_NAME, drone.droneName)
                    startActivity(intent)
                }

                adapterPantauModal.getOfflineMsg = {
                    Toast.makeText(requireActivity(),"Drone sedang offline",Toast.LENGTH_SHORT).show()
                }

                val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                val btnClose = view.findViewById<ImageView>(R.id.iv_close)

                tvTitle.text = "Pilih drone untuk dipantau"

                btnClose.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setContentView(view)
                dialog.show()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(p1: String, p2: String) =
            StatusFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, p1)
                    putString(ARG_PARAM2, p2)
                }
            }
    }

}