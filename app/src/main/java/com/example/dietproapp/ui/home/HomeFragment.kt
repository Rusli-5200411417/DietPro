package com.example.dietproapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dietproapp.core.data.source.model.Laporan
import com.example.dietproapp.core.data.source.remote.network.State
import com.example.dietproapp.databinding.FragmentHomeBinding
import com.example.dietproapp.ui.jurnalmakanan.JurnalActivity
import com.example.dietproapp.ui.profil.ProfilActivity
import com.example.dietproapp.util.Constants
import com.example.dietproapp.util.SPrefs
import com.inyongtisto.myhelper.extension.getInitial
import com.inyongtisto.myhelper.extension.intentActivity
import com.inyongtisto.myhelper.extension.logs
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val userId = SPrefs.getUserId()
    private val viewModel: HomeViewModel by viewModel()
    private var tampilKalori: String? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        swipeRefreshLayout = binding.refresh
        swipeRefreshLayout.setOnRefreshListener {
            // Panggil fungsi untuk melakukan refresh data di sini
            refreshData()
        }

        setUser()
        mainButton()
        getData()
        getnews()
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
            intentActivity(JurnalActivity::class.java)
        }

        binding.imgJurnal.setOnClickListener {
            intentActivity(JurnalActivity::class.java)
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

                if (targetKalori != null) {
                    tvTrackingKaloriMax.text = targetKalori
                } else {
                    tvTrackingKaloriMax.text = "Data belum lengkap!"
                }

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

    private fun getData(){
        viewModel.getLaporan(userId).observe(requireActivity()) {
            when (it.state) {
                State.SUCCESS -> {
                    val data = it.data ?: emptyList<Laporan>()
                    val jumlahKalori = data.sumOf { it.jumlah_kalori ?: 0 }.toString()
                    tampilKalori = jumlahKalori
                    setUser() // Update the UI when data changes
                }
                State.ERROR -> {
                    // Handle the error, show a message to the user, or log it
                    logs("Api", "API request failed")
                }
                State.LOADING -> {
                    // You can handle loading state here, e.g., show a progress indicator
                }
            }
        }
    }
    fun getnews() {
        viewModel.getnews().observe(viewLifecycleOwner) { response ->
            when (response.state) {
                State.SUCCESS -> {
                    val newsResponse = response.data
                    if (newsResponse != null) {
                        // Check the status field in the NewsResponse
                        if (newsResponse.status == "ok") {
                            // The API request was successful and the status is "ok"
                            val articles = newsResponse.articles
                            if (articles != null) {

                            } else {

                            }
                        } else {
                            // Handle the case where the API response status is not "ok"
                            logs("Api", "API request failed with status: ${newsResponse.status}")
                        }
                    } else {
                        // Handle the case where the API response data is null
                        logs("Api", "API response data is null")
                    }
                }
                State.ERROR -> {
                    // Handle the error, show a message to the user, or log it
                    logs("Api", "API request failed")
                }
                State.LOADING -> {
                    // You can handle loading state here, e.g., show a progress indicator
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
