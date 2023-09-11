package com.example.dietproapp.core.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dietproapp.core.data.repository.AppRepository
import com.example.dietproapp.ui.statistik.StatistikViewModel

class StatistikViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatistikViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatistikViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
