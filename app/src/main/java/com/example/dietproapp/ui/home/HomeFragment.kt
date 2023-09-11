package com.example.dietproapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dietproapp.databinding.FragmentHomeBinding
import com.example.dietproapp.ui.jurnalmakanan.JurnalActivity
import com.example.dietproapp.ui.jurnalmakanan.MenuJurnalActivity
import com.example.dietproapp.ui.profil.ProfilActivity
import com.example.dietproapp.util.Constants
import com.example.dietproapp.util.SPrefs
import com.inyongtisto.myhelper.extension.getInitial
import com.inyongtisto.myhelper.extension.intentActivity
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        swipeRefreshLayout = binding.refresh
        swipeRefreshLayout.setOnRefreshListener {
            // Panggil fungsi untuk melakukan refresh data di sini
            refreshData()
        }

        setUser()
        mainButton()
        return root
    }

    private fun refreshData() {
        // Sebagai contoh, kita akan memanggil kembali fungsi setUser()
        setUser()
        // Selesai melakukan refresh, beritahu SwipeRefreshLayout bahwa proses refresh sudah selesai
        swipeRefreshLayout.isRefreshing = false
    }

    private fun mainButton() {
        binding.profile.setOnClickListener {
            intentActivity(ProfilActivity::class.java)
        }

        binding.menuJurnal.setOnClickListener {
            intentActivity(MenuJurnalActivity::class.java)
        }

        binding.imgJurnal.setOnClickListener {
            intentActivity(MenuJurnalActivity::class.java)
        }
    }

    private fun setUser() {
        val user = SPrefs.getUser()
        if (user != null) {
            binding.apply {
                tvUsername.text = user.nama
                tvTipeakun.text = user.email
                tvInisialProfil.text = user.nama.getInitial()

                val targetKalori = user.kebutuhan_kalori
//                tvTrackingKalori.text = targetKalori

                if (targetKalori != null) {
                    tvTrackingKaloriMax.text = targetKalori
                } else {
                    tvTrackingKaloriMax.text = "Data belum lengkap!"
                }

                val kaloriSekarang = SPrefs.getLaporResponse()
                val tampilKalori = kaloriSekarang?.total_kalori
                val tampilKaloriInt = tampilKalori?.toIntOrNull() ?: 0

                if (tampilKalori != null) {
                    tvTrackingKalori.text = tampilKalori
                } else {
                    tvTrackingKalori.text = "Data belum ada!"
                }

                val targetKaloriInt = targetKalori?.toIntOrNull() ?: 0
                val currentProgress = if (targetKaloriInt != 0) {
                    tampilKaloriInt
                } else {
                    0
                }

                progressBar.max = targetKaloriInt
                progressBar.progress = currentProgress

                Picasso.get().load(Constants.USER_URL + user.foto_profil).into(binding.imgProfil)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}