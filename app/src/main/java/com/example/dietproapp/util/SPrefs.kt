package com.example.dietproapp.util

import com.chibatching.kotpref.KotprefModel
import com.example.dietproapp.core.data.source.model.Laporan
import com.example.dietproapp.core.data.source.model.User
import com.example.dietproapp.core.data.source.remote.response.LaporResponse
import com.inyongtisto.myhelper.extension.toJson
import com.inyongtisto.myhelper.extension.toModel

object SPrefs :KotprefModel() {

    //    private var sp : SharedPreferences? = null
    var isLogin by booleanPref(false)
    var user by stringPref()
    var token by stringPref("token")
    var laporResponse by stringPref()

    fun setUser(data: User?) {
        user = data.toJson()
    }

    fun setLaporan(lapor : LaporResponse) {
        laporResponse = lapor.toJson()
    }

    fun getUser(): User? {
        if (user.isEmpty()) return null
        return user.toModel(User::class.java)
    }

    fun getLaporResponse(): LaporResponse? {
        if (laporResponse.isEmpty()) return null
        return laporResponse.toModel(LaporResponse::class.java)
    }
    fun getTotalKalori(): String{
        val lapor = getLaporResponse()
        return lapor?.total_kalori ?: ""
    }

    fun getUserId(): Int {
        val user = getUser()
        return user?.id ?: -1 // Mengembalikan -1 jika objek User atau ID tidak tersedia
    }
}

    fun getMakan() = SPrefs.getUser()?.makan?.id

//    init {
//        sp  =   activity.getSharedPreferences("MYPREF", Context.MODE_PRIVATE)
//    }
//
//    fun  setIsLogin(value: Boolean)   {
//        sp!!.edit().putBoolean(login, value).apply()
//    }
//
//    fun getIsLogin() :Boolean {
//        return sp!!.getBoolean(login, false)
//    }
