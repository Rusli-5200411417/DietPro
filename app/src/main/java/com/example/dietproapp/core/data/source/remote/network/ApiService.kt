package com.example.dietproapp.core.data.source.remote.network

import com.example.dietproapp.core.data.source.model.JurnalMakanan
import com.example.dietproapp.core.data.source.model.Laporan
import com.example.dietproapp.core.data.source.model.Statistik
import com.example.dietproapp.core.data.source.remote.request.ForgotPasswordRequest
import com.example.dietproapp.core.data.source.remote.request.LaporanMakananRequest
import com.example.dietproapp.core.data.source.remote.request.LoginRequest
import com.example.dietproapp.core.data.source.remote.request.RegisterRequest
import com.example.dietproapp.core.data.source.remote.request.UpdateRequest
import com.example.dietproapp.core.data.source.remote.response.BaseListResponse
import com.example.dietproapp.core.data.source.remote.response.BaseSingelResponse
import com.example.dietproapp.core.data.source.remote.response.ForgotPasswordResponse
import com.example.dietproapp.core.data.source.remote.response.LaporResponse
import com.example.dietproapp.core.data.source.remote.response.LoginResponse
import com.example.dietproapp.core.data.source.remote.response.MakananResponse
import com.example.dietproapp.core.data.source.remote.response.NewsResponse
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

    @POST("forgot-password")
    suspend fun forgotPassword    (
        @Body data: ForgotPasswordRequest
    ):  Response<ForgotPasswordResponse>

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

    @POST("minggu/{id}")
    suspend fun getDataMinggu   (
        @Path("id") id_user: Int? = null,
    ):  Response<LaporResponse>

    @POST("bulan/{id}")
    suspend fun getDataBulan   (
        @Path("id") id_user: Int? = null,
    ):  Response<LaporResponse>
    @GET("news")
    suspend fun getNews(
    ): Response<NewsResponse>

}