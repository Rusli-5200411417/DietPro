package com.example.dietproapp.core.data.source.remote.network

import com.example.dietproapp.core.data.source.model.JurnalMakanan
import com.example.dietproapp.core.data.source.remote.request.LaporanMakananRequest
import com.example.dietproapp.core.data.source.remote.request.LoginRequest
import com.example.dietproapp.core.data.source.remote.request.RegisterRequest
import com.example.dietproapp.core.data.source.remote.request.UpdateRequest
import com.example.dietproapp.core.data.source.remote.response.BaseSingelResponse
import com.example.dietproapp.core.data.source.remote.response.LaporResponse
import com.example.dietproapp.core.data.source.remote.response.LoginResponse
import com.example.dietproapp.core.data.source.remote.response.MakananResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //  @Header(API)
    @POST("login")
    suspend fun login(
      @Body login: LoginRequest,
    ): Response<LoginResponse>

    //  https://127.0.0.1:8000/api/register
    @POST("register")
    suspend fun register    (
        @Body data: RegisterRequest
    ):  Response<LoginResponse>

    @PUT("update-user/{id}")
    suspend fun updateUser    (
        @Path("id") int: Int,
        @Body data: UpdateRequest
    ):  Response<LoginResponse>

    @Multipart
    @POST("upload-user/{id}")
    suspend fun uploadUser    (
        @Path("id") int: Int? = null,
        @Body data: MultipartBody.Part? = null
    ):  Response<LoginResponse>

//    @POST("jurnal-makanan")
//    suspend fun menuJurnal   (
//        @Body   data: Makanan
//    ):  Response<BaseListResponse<Makanan>>

    @POST("makanan")
    suspend fun getmenuJurnal   (
    ):  Response<MakananResponse>

    @POST("store/{id}")
    suspend fun store(
        @Path("id") id: Int?,
        @Body requestJson: JsonObject
    ): Response<LaporResponse>

    @POST("laporan/{id}")
    suspend fun getLaporan   (
        @Path("id") id_user: Int? = null,
    ):  Response<LaporResponse>

}