package com.example.dietproapp.ui.statistik

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dietproapp.core.data.repository.AppRepository

class StatistikViewModel(private val repo: AppRepository) : ViewModel() {

    fun getLaporan(id: Int?) = repo.getLaporan(id).asLiveData()

}