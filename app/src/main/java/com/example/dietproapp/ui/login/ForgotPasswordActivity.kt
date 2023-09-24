package com.example.dietproapp.ui.login

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.dietproapp.R
import com.example.dietproapp.core.data.source.remote.network.State
import com.example.dietproapp.core.data.source.remote.request.ForgotPasswordRequest
import com.example.dietproapp.databinding.ActivityEditProfilBinding
import com.example.dietproapp.databinding.ActivityForgetPasswordBinding
import com.inyongtisto.myhelper.extension.isEmpty
import com.inyongtisto.myhelper.extension.showToast
import com.inyongtisto.myhelper.extension.toastError
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModel()

    private var _binding: ActivityForgetPasswordBinding?  =   null
    private val binding get()   =   _binding!!
    private  lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        progressBar.visibility = View.GONE

        mainButton()
    }

    private fun mainButton(){

        binding.btnSend.setOnClickListener{
            setForgetPassword()
        }
    }

    private fun setUiEnabled(enabled: Boolean) {
        val rootView = findViewById<View>(android.R.id.content)
        setViewEnabled(rootView, enabled)
    }

    private fun setViewEnabled(view: View, enabled: Boolean) {
        view.isEnabled = enabled
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                setViewEnabled(view.getChildAt(i), enabled)
            }
        }
        if (!enabled) {
            view.alpha = 0.5f
            progressBar.visibility = View.VISIBLE// Menggelapkan tampilan saat di-disable
        } else {
            view.alpha = 1.0f
        }
    }

    fun setForgetPassword(){
        if (binding.edtEmail.isEmpty())return

        progressBar.visibility = View.GONE
        setUiEnabled(true)

        val body = ForgotPasswordRequest(
            binding.edtEmail.text.toString()
        )

        viewModel.forgotPassword(body).observe(this){
            when (it.state) {
                State.SUCCESS -> {
                    setUiEnabled(true)
                    progressBar.visibility = View.GONE
                    val body = it.data
                    showToast("Cek Email ${body?.email} untuk langkah selanjutnya ")
                }
                State.ERROR -> {
                    setUiEnabled(false)
                    progressBar.visibility = View.GONE
                    toastError(it.message ?: "Error")
                }
                State.LOADING -> {
                    setUiEnabled(false)
                    progressBar.visibility = View.VISIBLE

                }
            }
        }
    }
}