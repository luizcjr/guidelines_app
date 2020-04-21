package com.luiz.guidelines.ui.activities.forgot_password

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.luiz.guidelines.R
import com.luiz.guidelines.databinding.ForgotPasswordBinding
import com.luiz.guidelines.ui.base.BaseActivity

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var viewModel: ForgotPasswordViewModel
    private var binding: ForgotPasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // data binding class
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

        //ViewModel
        viewModel = ViewModelProviders.of(this)[ForgotPasswordViewModel::class.java]

        binding!!.forgotViewModel = viewModel
        binding!!.lifecycleOwner = this

        viewModel.loading.observe(this, loadingLiveDataObserver)
        viewModel.loadError.observe(this, errorLiveDataObserver)
        viewModel.context = this
    }
}
