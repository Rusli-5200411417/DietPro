package com.example.dietproapp.core.data.source.remote.response

import com.example.dietproapp.core.data.source.model.ForgotPassword
import com.example.dietproapp.core.data.source.model.User

data class ForgotPasswordResponse (
   val code :  Int? = null,
   val message:    String? = null,
   val data: ForgotPassword?  =   null
)
