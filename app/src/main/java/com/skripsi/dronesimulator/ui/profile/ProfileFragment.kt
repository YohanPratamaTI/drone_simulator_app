package com.skripsi.dronesimulator.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.databinding.FragmentProfileBinding
import com.skripsi.dronesimulator.ui.login.LoginActivity
import com.skripsi.dronesimulator.viewmodel.MainViewModel
import com.skripsi.dronesimulator.viewmodel.ViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title =  "Profile"
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupViewModel()
        setupAction()
        return binding.root
    }

    private fun setupAction() {
        binding.ivProfile.setImageResource(R.drawable.profile)
        binding.btLogout.setOnClickListener{
            val intentToLogin = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intentToLogin)
            requireActivity().finish()
            mainViewModel.logoutDemo()
        }
    }

    private fun setupViewModel() {
        mainViewModel= ViewModelProvider(
            this,
            ViewModelFactory(requireActivity())
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.tvProfileName.text = user.username
                binding.tvUsername.text = user.username
                binding.tvEmail.text = user.email
                binding.tvPhoneNumber.text = user.phoneNumber
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}