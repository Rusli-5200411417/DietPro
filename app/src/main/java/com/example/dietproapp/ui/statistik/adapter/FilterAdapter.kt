package com.example.dietproapp.ui.statistik.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dietproapp.core.data.source.model.Laporan
import com.example.dietproapp.core.data.source.model.Makanan
import com.example.dietproapp.databinding.ListItemFilterBinding
import com.example.dietproapp.databinding.ListItemStatistikBinding
import com.example.dietproapp.databinding.ListJurnalBinding
import com.inyongtisto.myhelper.extension.logs

@SuppressLint("NotifyDataSetChanged")
class FilterAdapter : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    var data = ArrayList<Laporan>()
    private var currentDate: String? = null
    private var statistikItemBinding: ListItemStatistikBinding? = null
    inner class ViewHolder(val itemBinding: ListItemFilterBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val rvDetail: RecyclerView = itemBinding.rvDetail
        private val detailAdapter = StatistikAdapter()

        init {
            rvDetail.layoutManager = LinearLayoutManager(rvDetail.context) // Tambahkan layout manager di sini
            rvDetail.adapter = detailAdapter
        }
        fun bind(item: Laporan, position: Int) {
            val date = item.created_at?.substring(0, 10) ?: ""
            itemBinding.apply {
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
            val filteredData = data.filter { it.created_at?.startsWith(date) == true }
            detailAdapter.setData(filteredData)
        }
    }

    fun setDetailData(item: Laporan) {
        statistikItemBinding?.makananList?.text = item.nama_bahan
        statistikItemBinding?.jumlahList?.text = item.jumlah.toString()
        statistikItemBinding?.kaloriList?.text = item.jumlah_kalori.toString()
    }

    fun addItems(items: List<Laporan>) {
//        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }
    fun clearItems() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(
            ListItemFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data, position)
    }
}




