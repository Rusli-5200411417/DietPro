package com.example.dietproapp.util

import com.chibatching.kotpref.KotprefModel
import com.example.dietproapp.core.data.source.model.User
import com.example.dietproapp.core.data.source.remote.response.LaporResponse
import com.inyongtisto.myhelper.extension.toJson
import com.inyongtisto.myhelper.extension.toModel

object SPrefs : KotprefModel() {

    var isLogin by booleanPref(false)
    var user by stringPref()
    var token by stringPref("token")
    var laporResponse by stringPref()
    var jumlahKalori by stringPref()

    fun setUser(data: User?) {
        user = data.toJson()
    }

    fun setLaporan(lapor : LaporResponse) {
        laporResponse = lapor.toJson()
    }
    fun setKalori(jumlah: String) {
        jumlahKalori = jumlah
    }

    fun getUser(): User? {
        if (user.isEmpty()) return null
        return user.toModel(User::class.java)
    }

    fun getUserId(): Int {
        val user = getUser()
        return user?.id ?: -1 // Mengembalikan -1 jika objek User atau ID tidak tersedia
    }
}


