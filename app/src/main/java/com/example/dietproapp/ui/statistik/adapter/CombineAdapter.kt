package com.example.dietproapp.ui.statistik.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dietproapp.core.data.source.model.Laporan
import com.example.dietproapp.databinding.ListItemFilterBinding
import com.example.dietproapp.databinding.ListItemStatistikBinding
import com.example.dietproapp.databinding.ListJurnalBinding
import com.inyongtisto.myhelper.extension.logs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CombineAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val statistikData = ArrayList<Laporan>()
    private val filterData = ArrayList<Laporan>()
    private val filterDetailData = ArrayList<Laporan>()
    private val groupedData = LinkedHashMap<String, Laporan>()
    private val VIEW_TYPE_STATISTIK = 0
    private val VIEW_TYPE_FILTER = 1
    private var jumlahItem = 0
    private var currentDate: String? = null
    private var statistikItemBinding: ListItemStatistikBinding? = null


    inner class StatistikViewHolder(val itemBinding: ListItemStatistikBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Laporan, position: Int) {
            itemBinding.apply {
                item.id.toString()
                makananList.text = item.nama_bahan
                jumlahList.text = item.jumlah.toString()
                kaloriList.text = item.jumlah_kalori.toString()

            }
        }
    }

    inner class FilterViewHolder(val itemBinding: ListItemFilterBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val rvDetail: RecyclerView = itemBinding.rvDetail
        private val detailAdapter = StatistikAdapter()

        init {
            rvDetail.layoutManager = LinearLayoutManager(rvDetail.context) // Tambahkan layout manager di sini
            rvDetail.adapter = detailAdapter
        }
        fun bind(item: Laporan, position: Int) {
            val date = item.created_at?.substring(0, 10) ?: ""
            val kalori = groupedData[date]?.jumlah_kalori ?: 0
            itemBinding.apply {
                //bagian atas
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val parsedDate = dateFormat.parse(date)
                val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(parsedDate)

                listTanggal.text = formattedDate
                jumlahKaloriList.text = kalori.toString()
                //bagian bawah
                if (currentDate == date) {
                    // Tampilkan data di detail_statistik jika tanggal sama dengan currentDate
                    setDetailData(item)
                    detailStatistik.visibility = View.VISIBLE
                    setRvDetailData(date)
                } else {
                    detailStatistik.visibility = View.GONE
                }

                // Tambahkan OnClickListener untuk menampilkan atau menyembunyikan detail_statistik
                relativeLayout.setOnClickListener {
                    if (currentDate == date) {
                        // Jika detail_statistik sedang terbuka, tutup
                        detailStatistik.visibility = View.GONE
                        currentDate = null
                    } else {
                        // Jika detail_statistik sedang tertutup, buka
                        detailStatistik.visibility = View.VISIBLE
                        currentDate = date
                    }
                    setRvDetailData(date) // Tambahkan ini di sini
                }
            }
        }

        private fun setRvDetailData(date: String) {
            val filterData = filterDetailData.filter { it.created_at?.startsWith(date) == true }
            detailAdapter.setData(filterData)
        }
    }

    fun setDetailData(item: Laporan) {
        item.id
        statistikItemBinding?.makananList?.text = item.nama_bahan
        statistikItemBinding?.jumlahList?.text = item.jumlah.toString()
        statistikItemBinding?.kaloriList?.text = item.jumlah_kalori.toString()
    }

    fun addFilterDetailItems(items: List<Laporan>){
        filterDetailData.clear()
        filterDetailData.addAll(items)
        notifyDataSetChanged()
    }

    fun addStatistikItems(items: List<Laporan>) {
        statistikData.clear()
        statistikData.addAll(items)
        notifyDataSetChanged()
    }


    fun addFilterItems(items: List<Laporan>) {
        groupedData.clear()
        filterDetailData.clear()

        for (item in items) {
            val date = item.created_at?.substring(0, 10) ?: ""
            if (groupedData.containsKey(date)) {
                // Jika tanggal sudah ada, tambahkan jumlah kalori
                val existingItem = groupedData[date]
                existingItem?.jumlah_kalori = (existingItem?.jumlah_kalori ?: 0) + (item.jumlah_kalori ?: 0)
            } else {
                // Jika tanggal belum ada, tambahkan data baru
                groupedData[date] = item
            }
            // Tambahkan semua item ke filterDetailData
            val copyItem = item.copy()
            filterDetailData.add(copyItem)
        }

        // Menggabungkan item dengan tanggal yang sama dan menjumlahkan kalori
        val combinedData = mutableListOf<Laporan>()
        for ((date, item) in groupedData) {
            combinedData.add(item)
        }

        filterData.clear()
        filterData.addAll(combinedData)
        jumlahItem = statistikData.size + filterData.size
        notifyDataSetChanged()
    }


    fun clearItems() {
        statistikData.clear()
        filterData.clear()
        filterDetailData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_STATISTIK -> {
                val itemView = ListItemStatistikBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                statistikItemBinding = itemView
                StatistikViewHolder(itemView)
            }
            VIEW_TYPE_FILTER -> {
                val itemView = ListItemFilterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FilterViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_STATISTIK -> {
                val statistikHolder = holder as StatistikViewHolder
                statistikHolder.bind(statistikData[position], position)
            }
            VIEW_TYPE_FILTER -> {
                val filterHolder = holder as FilterViewHolder
                filterHolder.bind(filterData[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return statistikData.size + filterData.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < statistikData.size) {
            VIEW_TYPE_STATISTIK
        } else {
            VIEW_TYPE_FILTER
        }
    }
}
