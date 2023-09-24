package com.example.dietproapp.core.data.repository

import com.example.dietproapp.core.data.source.local.LocalDataSource
import com.example.dietproapp.core.data.source.model.ArticlesItem
import com.example.dietproapp.core.data.source.model.Laporan
import com.example.dietproapp.core.data.source.model.Makanan
import com.example.dietproapp.core.data.source.remote.RemoteDataSource
import com.example.dietproapp.core.data.source.remote.network.Resource
import com.example.dietproapp.core.data.source.remote.network.ResourceLapor
import com.example.dietproapp.core.data.source.remote.request.ForgotPasswordRequest
import com.example.dietproapp.core.data.source.remote.request.LaporanMakananRequest
import com.example.dietproapp.core.data.source.remote.request.LoginRequest
import com.example.dietproapp.core.data.source.remote.request.RegisterRequest
import com.example.dietproapp.core.data.source.remote.request.UpdateRequest
import com.example.dietproapp.core.data.source.remote.response.LaporResponse
import com.example.dietproapp.core.data.source.remote.response.MakananResponse
import com.example.dietproapp.util.SPrefs
import com.google.gson.JsonObject
import com.inyongtisto.myhelper.extension.getErrorBody
import com.inyongtisto.myhelper.extension.logs
import com.inyongtisto.myhelper.extension.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.Response

class AppRepository(val local:LocalDataSource, val remote:RemoteDataSource) {

    fun login(data: LoginRequest) = flow {
        emit(Resource.loading(null,))
        try {
            remote.login(data).let {
                if (it.isSuccessful) {
                    SPrefs.isLogin = true
                    val body = it.body()
                    val user = body?.data
                    logs("user:" + user.toJson())
                    SPrefs.setUser(user)
                    emit(Resource.success(user))
                    logs("success" + body.toString())
                } else {
                    emit(Resource.error(it.getErrorBody()?.message ?: "Error Default", null))
                    logs("Error: " + "keterangan error")
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
            logs("Error:" + e.message)
        }
    }


    fun register(data: RegisterRequest) =   flow {
        emit(Resource.loading(null))
        try {
            remote.register(data).let {
                if (it.isSuccessful) {
                    SPrefs.isLogin  = true //jika mau ke login dulu ini bisa dihapus
                    val body    =   it.body()
                    val user    = body?.data
                    SPrefs.setUser(user) //ini juga -> lanjut ke RegisterActivity
                    emit(Resource.success(user))
                    logs("success" + body.toString())
                } else {
                    emit(Resource.error(it.getErrorBody()?.message
                        ?: "Error Default", null))
                    logs("Error: " + "keterangan error")
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
            logs("Error:" + e.message)
        }
    }

    fun updateUser(data: UpdateRequest ) =   flow {
        emit(Resource.loading(null))
        try {
            remote.updateUser(data).let {
                if (it.isSuccessful) {
                    val body    =   it.body()
                    val user    = body?.data
                    SPrefs.setUser(user) //untuk update data user terbaru
                    emit(Resource.success(user))
                } else {
                    emit(Resource.error(it.getErrorBody()?.message
                        ?: "Error Default", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }

    fun uploadUser(id: Int? = null, fileImage: MultipartBody.Part? = null) =   flow {
        emit(Resource.loading(null))
        try {
            remote.uploadUser(id, fileImage).let {
                if (it.isSuccessful) {
                    val body    =   it.body()
                    val user    = body?.data
                    SPrefs.setUser(user) //untuk update data user terbaru
                    emit(Resource.success(user))
                } else {
                    emit(Resource.error(it.getErrorBody()?.message
                        ?: "Error Default", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }

    fun setForgotPassword(data: ForgotPasswordRequest) =   flow {
        emit(Resource.loading(null))
        try {
            remote.setForgetPassword(data).let {
                if (it.isSuccessful) {
                    val body    =   it.body()
                    val data    = body?.data
                    emit(Resource.success(data))
                    logs("forgot-password","$data")
                } else {
                    emit(Resource.error(it.getErrorBody()?.message
                        ?: "Error Default", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }

    fun getLaporan(id: Int?) = flow {
        emit(Resource.loading(null))
        try {
            remote.getLaporan(id).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    val data = body?.data
                    logs("data" + data.toJson())
                    // untuk update data user terbaru
                    logs("success" + body.toString())
                    emit(Resource.success(data))
                } else {
                    emit(
                        Resource.error(
                            it.errorBody()?.string() ?: "Error Default",
                            null
                        )
                    )
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }

    fun getDataMinggu(id: Int?) = flow {
        emit(Resource.loading(null))
        try {
            remote.getDataMinggu(id).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    val data = body?.data
                    logs("data" + data.toJson())
                    // untuk update data user terbaru
                    logs("success" + body.toString())
                    emit(Resource.success(data))
                } else {
                    emit(
                        Resource.error(
                            it.errorBody()?.string() ?: "Error Default",
                            null
                        )
                    )
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }

    fun getDataBulan(id: Int?) = flow {
        emit(Resource.loading(null))
        try {
            remote.getDataBulan(id).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    val data = body?.data
                    logs("data" + data.toJson())
                    // untuk update data user terbaru
                    logs("success" + body.toString())
                    emit(Resource.success(data))
                } else {
                    emit(
                        Resource.error(
                            it.errorBody()?.string() ?: "Error Default",
                            null
                        )
                    )
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }
    fun getmenuJurnal() = flow <Resource<List<Makanan>>>{
        emit(Resource.loading(null))
        try {
            remote.getmenuJurnal().let {
                if (it.isSuccessful) {
                    val body = it.body()
                    val data = body?.data
                    logs("data:" + data.toJson())
                    // untuk update data user terbaru
                    logs("success" + body.toString())
                    emit(Resource.success(data))
                } else {
                    emit(Resource.error(it.getErrorBody()?.message?: "Error Default", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message?: "Terjadi Kesalahan", null))
        }
    }
    fun store(idUser: Int?, requestJson: JsonObject) = flow {
        emit(Resource.loading(null))
        try {
            val response = remote.store(idUser, requestJson)
            if (response.isSuccessful) {
                val body = response.body()
                val user = body?.data
                if (user != null) {
                    emit(Resource.success(user))
                } else {
                    emit(Resource.error("Response body is empty", null))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error Default"
                emit(Resource.error(errorMessage, null))
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }.flowOn(Dispatchers.IO)

    fun getNews() = flow {
        emit(Resource.loading(null))
        try {
            remote.getNews().let { response ->
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse != null && newsResponse.status == "ok") {
                        logs("news","$newsResponse")
                        emit(Resource.success(newsResponse))
                    } else {
                        emit(Resource.error("API response status is not 'ok'", null))
                    }
                } else {
                    emit(Resource.error(response.errorBody()?.string() ?: "Error Default", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }



}