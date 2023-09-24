package com.example.dietproapp.ui.statistik

import android.R
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dietproapp.core.data.factory.StatistikViewModelFactory
import com.example.dietproapp.core.data.repository.AppRepository
import com.example.dietproapp.core.data.source.local.LocalDataSource
import com.example.dietproapp.core.data.source.model.Laporan
import com.example.dietproapp.core.data.source.remote.RemoteDataSource
import com.example.dietproapp.core.data.source.remote.network.ApiConfig
import com.example.dietproapp.core.data.source.remote.network.ApiService
import com.example.dietproapp.core.data.source.remote.network.State
import com.example.dietproapp.core.data.source.remote.response.LaporResponse
import com.example.dietproapp.databinding.FragmentStatistikBinding
import com.example.dietproapp.databinding.ListItemFilterBinding
import com.example.dietproapp.ui.statistik.adapter.CombineAdapter
import com.example.dietproapp.ui.statistik.adapter.FilterAdapter
import com.example.dietproapp.ui.statistik.adapter.StatistikAdapter
import com.example.dietproapp.util.SPrefs
import com.inyongtisto.myhelper.extension.logs

class StatistikFragment : Fragment() {

    companion object {
        fun newInstance() = StatistikFragment()
    }

    private lateinit var viewModel: StatistikViewModel
    private lateinit var binding: FragmentStatistikBinding

    private lateinit var filterbinding: ListItemFilterBinding
    private lateinit var listRincianMakanan: RecyclerView

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val filterAdapter = FilterAdapter()

    private  var combineAdapter =  CombineAdapter()


    private val userId = SPrefs.getUserId()
    private val user = SPrefs.getUser()

    private val spinnerOptions = arrayOf("Hari Ini", "Minggu Ini", "Bulan Ini")
    private var isDetailVisible = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStatistikBinding.inflate(inflater, container, false)
        filterbinding = ListItemFilterBinding.inflate(inflater,container,false)
        //pengaturan adapter
        val apiService: ApiService = ApiConfig.provideApiService

        val localDataSource = LocalDataSource()
        val remoteDataSource = RemoteDataSource(apiService)
        val appRepository = AppRepository(localDataSource, remoteDataSource)

        val viewModelFactory = StatistikViewModelFactory(appRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StatistikViewModel::class.java)

        binding.rvListRincianMakanan.layoutManager = LinearLayoutManager(context)
        binding.rvListRincianMakanan.adapter = combineAdapter

        filterbinding.rvDetail.layoutManager = LinearLayoutManager(context)
        filterbinding.rvDetail.adapter = filterAdapter



        //pengaturan refresh
        swipeRefreshLayout = binding.refresh
        swipeRefreshLayout.setOnRefreshListener {
            // Panggil fungsi untuk melakukan refresh data di sini
            refreshData()

        }

        val spinner = binding.ddOpsihari
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Set a listener to handle item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selected item here
                val selectedOption = spinnerOptions[position]
                // You can perform actions based on the selected option
                when (selectedOption) {
                    "Hari Ini" -> {
                        refreshData()
                    }

                    "Minggu Ini" -> {
                        refreshData()
                        getDataMinggu()
                    }

                    "Bulan Ini" -> {
                        refreshData()
                        getDataBulan()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing if nothing is selected
            }
        }
        return binding.root
    }

    private fun refreshData() {
        // Get data based on the selected spinner option
        when (binding.ddOpsihari.selectedItem.toString()) {
            "Hari Ini" -> {
                combineAdapter.clearItems()
                getData()
            }
            "Minggu Ini" -> {
                getDataMinggu()
            }
            "Bulan Ini" -> {
                getDataBulan()
            }
        }
        // Selesai melakukan refresh, beritahu SwipeRefreshLayout bahwa proses refresh sudah selesai
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
    private fun getData() {
        val targetKalori = user?.kebutuhan_kalori
        binding.target.text = targetKalori
        viewModel.getLaporan(userId).observe(this) {
            when (it.state) {
                State.SUCCESS -> {
                    val data = it.data ?: emptyList<Laporan>()
                    val jumlahKalori = data.sumOf { it.jumlah_kalori ?: 0 }.toString()
                    binding.kalori.text = jumlahKalori
                    binding.rataRata.text = jumlahKalori

                    // Save jumlahKalori into SPrefs
//                    SPrefs.setKalori(jumlahKalori)


                    combineAdapter.addStatistikItems(data)
                }

                State.ERROR -> {
                    logs("Api","api gagal dimuat")
                }
                State.LOADING -> {

                }
            }
        }
    }
    private fun getDataMinggu() {
        binding.label1.text = "Tanggal"
        binding.label2.text = ""
        binding.label3.text = "Jumlah Kalori"
        val targetKalori = user?.kebutuhan_kalori
        binding.target.text = targetKalori
        viewModel.getDataMinggu(userId).observe(this) {
            when (it.state) {
                State.SUCCESS -> {
                    val data = it.data ?: emptyList<Laporan>()

                    val jumlahKalori = data.sumOf { it.jumlah_kalori ?: 0 }.toString()
                    val groupedData = data.groupBy { it.created_at?.substring(0, 10) ?: "" }
                    val groupedDataList = ArrayList(groupedData.values.flatten())
                    val jumlahItem = combineAdapter.itemCount
                    logs("val1","$groupedData")
                    logs("jumlah item", "$jumlahItem")
                    logs("val","$groupedDataList")
                    val rataRata = if (jumlahItem > 0) jumlahKalori.toDouble() / jumlahItem else 0

                    // Buat list baru untuk data yang sudah digelompokkan

                    binding.kalori.text = jumlahKalori
                    binding.rataRata.text = rataRata.toString()
                    combineAdapter.clearItems()
                    combineAdapter.addFilterItems(groupedDataList)
                }

                State.ERROR -> {
                    logs("Api","api gagal dimuat")
                }
                State.LOADING -> {

                }

            }
        }
    }
    private fun getDataBulan() {
        binding.label1.text = "Tanggal"
        binding.label2.text = ""
        binding.label3.text = "Jumlah Kalori"
        val targetKalori = user?.kebutuhan_kalori
        binding.target.text = targetKalori
        viewModel.getDataBulan(userId).observe(this) {
            when (it.state) {
                State.SUCCESS -> {
                    val data = it.data ?: emptyList<Laporan>()
                    val jumlahKalori = data.sumOf { it.jumlah_kalori ?: 0 }.toString()
                    val jumlahItem = combineAdapter.itemCount
                    val groupedData = data.groupBy { it.created_at?.substring(0, 10) ?: "" }
                    val groupedDataList = ArrayList(groupedData.values.flatten())
                    logs("jumlah item", "$jumlahItem")
                    val rataRata = if (jumlahItem > 0) jumlahKalori.toDouble() / jumlahItem else 0
                    binding.rataRata.text = rataRata.toString()
                    binding.kalori.text = jumlahKalori


                    combineAdapter.clearItems()
                    combineAdapter.addFilterItems(groupedDataList)
                }

                State.ERROR -> {
                    logs("Api","api gagal dimuat")
                }
                State.LOADING -> {

                }
            }
        }
    }
}