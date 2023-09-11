package com.example.dietproapp.ui.statistik

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dietproapp.R
import com.example.dietproapp.core.data.factory.StatistikViewModelFactory
import com.example.dietproapp.core.data.repository.AppRepository
import com.example.dietproapp.core.data.source.local.LocalDataSource
import com.example.dietproapp.core.data.source.remote.RemoteDataSource
import com.example.dietproapp.core.data.source.remote.network.ApiConfig
import com.example.dietproapp.core.data.source.remote.network.ApiService
import com.example.dietproapp.core.data.source.remote.network.State
import com.example.dietproapp.databinding.FragmentStatistikBinding
import com.example.dietproapp.util.SPrefs
import com.inyongtisto.myhelper.extension.logs

class StatistikFragment : Fragment() {

    companion object {
        fun newInstance() = StatistikFragment()
    }

    private lateinit var viewModel: StatistikViewModel
    private lateinit var binding: FragmentStatistikBinding

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStatistikBinding.inflate(inflater, container, false)

        val apiService: ApiService = ApiConfig.provideApiService

        val localDataSource = LocalDataSource()
        val remoteDataSource = RemoteDataSource(apiService)
        val appRepository = AppRepository(localDataSource, remoteDataSource)

        val viewModelFactory = StatistikViewModelFactory(appRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StatistikViewModel::class.java)




        swipeRefreshLayout = binding.refresh
        swipeRefreshLayout.setOnRefreshListener {
            // Panggil fungsi untuk melakukan refresh data di sini
            refreshData()
        }
        return binding.root
    }

    private fun refreshData() {
        getData()

        // Selesai melakukan refresh, beritahu SwipeRefreshLayout bahwa proses refresh sudah selesai
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData()

    }
    private fun getData(){
        val userId = SPrefs.getUserId()
        val user = SPrefs.getUser() // Ambil model User dari SPrefs
        val targetKalori = user?.kebutuhan_kalori
        binding.target.text = targetKalori
        viewModel.getLaporan(userId).observe(viewLifecycleOwner) { result ->
            when (result.state) {
                State.SUCCESS -> {
                    val laporResponse = result.data // Ini adalah objek LaporResponse

                    // Pastikan respons tidak null
                    if (laporResponse != null) {
                        val totalKalori = laporResponse.total_kalori
                        binding.coba.text = laporResponse.total_protein

                        // Pastikan totalKalori tidak null
                        if (totalKalori != null) {
                            binding.kalori.text = totalKalori.toString()

                            SPrefs.setLaporan(laporResponse)

                            Log.d("TotalKalori", "Nilai total_kalori dari respons JSON: $totalKalori")
                        }
                    }
                }
                State.ERROR -> {
                    // Tangani kesalahan jika terjadi
                }
                State.LOADING -> {
                    // Tampilkan indikator loading jika sedang memuat data
                }
            }
        }
    }

}