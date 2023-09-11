package com.example.dietproapp.core.data.source.remote.response

import com.example.dietproapp.core.data.source.model.Laporan
import com.example.dietproapp.core.data.source.model.Makanan

data class LaporResponse (
    val code :  Int? = null,
    val message:    String? = null,
    val total_kalori: String?=null,
    val total_protein: String?=null,
    val lapor: Laporan? = null
)
