    package com.example.dietproapp.ui.statistik.adapter

    import android.annotation.SuppressLint
    import android.view.LayoutInflater
    import android.view.ViewGroup
    import androidx.recyclerview.widget.DiffUtil
    import androidx.recyclerview.widget.RecyclerView
    import com.example.dietproapp.core.data.source.model.Laporan
    import com.example.dietproapp.core.data.source.model.Makanan
    import com.example.dietproapp.databinding.ListItemFilterBinding
    import com.example.dietproapp.databinding.ListItemStatistikBinding
    import com.example.dietproapp.databinding.ListJurnalBinding
    import com.inyongtisto.myhelper.extension.logs

    @SuppressLint("NotifyDataSetChanged")
    class StatistikAdapter : RecyclerView.Adapter<StatistikAdapter.ViewHolder>() {

        var data = ArrayList<Laporan>()

        inner class ViewHolder(val itemBinding: ListItemStatistikBinding) :
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

        private class DiffCallback : DiffUtil.ItemCallback<Laporan>() {
            override fun areItemsTheSame(oldItem: Laporan, newItem: Laporan): Boolean {
                // Implementasikan logika untuk memeriksa apakah item adalah sama
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Laporan, newItem: Laporan): Boolean {
                // Implementasikan logika untuk memeriksa apakah kontennya sama
                return oldItem == newItem
            }
        }
        fun submitList(newList: List<Laporan>) {
            data.clear()
            data.addAll(newList)
            notifyDataSetChanged()
        }

        fun addItems(items: List<Laporan>) {
    //        data.clear()
            data.addAll(items)
            notifyDataSetChanged()
        }
        fun setData(data: List<Laporan>) {
            this.data = ArrayList(data)
            notifyDataSetChanged()
        }
        fun clearItems() {
            data.clear()
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val viewHolder = ViewHolder(
                ListItemStatistikBinding.inflate(  LayoutInflater.from(parent.context),
                    parent,
                    false)
            )
            return viewHolder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val makanan = data[position]
            holder.bind(makanan, position)

        }

        override fun getItemCount(): Int {
            return data.size
        }
    }
