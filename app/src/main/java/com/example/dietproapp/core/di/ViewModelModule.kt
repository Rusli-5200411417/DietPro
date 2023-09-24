package com.example.dietproapp.core.di

import com.example.dietproapp.ui.home.HomeViewModel
import com.example.dietproapp.ui.jurnalmakanan.JurnalMenuViewModel
import com.example.dietproapp.ui.login.AuthViewModel
import com.example.dietproapp.ui.statistik.StatistikViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { JurnalMenuViewModel(get()) }
    viewModel { StatistikViewModel(get()) }
}
