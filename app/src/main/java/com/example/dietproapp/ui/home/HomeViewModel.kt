package com.example.dietproapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.dietproapp.core.data.repository.AppRepository

class HomeViewModel(private val repo: AppRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getLaporan(id: Int?) = repo.getLaporan(id).asLiveData()
    fun getnews() = repo.getNews().asLiveData()
}