package com.example.dietproapp.core.data.source.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Laporan(
    val id: Int?,
    val id_user: Int?,
    val id_makanan: Int?,
    val nama_bahan: String?,
    val kalori: Int?,
    val jumlah: Int?,
    var jumlah_kalori: Int?,
    val created_at: String?,
    val updated_at: String?,
    val user: User,
    val makanan: Makanan
) : Parcelable
