package com.example.dietproapp.core.data.source.remote

import com.example.dietproapp.core.data.source.remote.network.ApiService
import com.example.dietproapp.core.data.source.remote.request.ForgotPasswordRequest
import com.example.dietproapp.core.data.source.remote.request.LaporanMakananRequest
import com.example.dietproapp.core.data.source.remote.request.LoginRequest
import com.example.dietproapp.core.data.source.remote.request.RegisterRequest
import com.example.dietproapp.core.data.source.remote.request.UpdateRequest
import com.google.gson.JsonObject
import okhttp3.MultipartBody

class RemoteDataSource(private val api: ApiService) {

    suspend fun login(data: LoginRequest) = api.login(data)
    suspend fun register(data: RegisterRequest) = api.register(data)
    suspend fun updateUser(data: UpdateRequest) = api.updateUser(data.id, data)
    suspend fun uploadUser(id: Int? = null, fileImage: MultipartBody.Part? = null) = api.uploadUser(id, fileImage)
    suspend fun getLaporan(id: Int?) = api.getLaporan(id)
    suspend fun getDataMinggu(id: Int?) = api.getDataMinggu(id)
    suspend fun getDataBulan(id: Int?) = api.getDataBulan(id)
    suspend fun getmenuJurnal() = api.getmenuJurnal()
    suspend fun store(idUser: Int?, requestJson: JsonObject) = api.store(idUser, requestJson)
    suspend fun getNews() = api.getNews()
    suspend fun setForgetPassword(data: ForgotPasswordRequest) = api.forgotPassword(data)


}