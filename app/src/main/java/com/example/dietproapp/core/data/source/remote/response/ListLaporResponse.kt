package com.example.dietproapp.core.data.source.remote.response

import com.example.dietproapp.core.data.source.model.Laporan
import com.example.dietproapp.core.data.source.model.Makanan

data class ListLaporResponse (
    val code :  Int? = null,
    val message:    String? = null,
    val total_kalori: String?=null,
    val data : List<Laporan>?
)
