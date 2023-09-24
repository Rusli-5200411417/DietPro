package com.example.dietproapp.ui.statistik

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dietproapp.core.data.repository.AppRepository
import com.google.gson.JsonObject

class StatistikViewModel(private val repo: AppRepository) : ViewModel() {

    fun getLaporan(id: Int?) = repo.getLaporan(id).asLiveData()
    fun getDataMinggu(id: Int?) = repo.getDataMinggu(id).asLiveData()
    fun getDataBulan(id: Int?) = repo.getDataBulan(id).asLiveData()
}