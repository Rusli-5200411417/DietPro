package com.example.dietproapp.core.data.source.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForgotPassword(
    val email:      String? = null
//    val token: String?
): Parcelable
