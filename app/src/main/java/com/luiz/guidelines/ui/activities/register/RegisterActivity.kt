package com.luiz.guidelines.ui.activities.register

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.luiz.guidelines.R
import com.luiz.guidelines.databinding.RegisterBinding
import com.luiz.guidelines.ui.base.BaseActivity

class RegisterActivity : BaseActivity() {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // data binding class
        val binding: RegisterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_register)

        //ViewModel
        viewModel = ViewModelProviders.of(this)[RegisterViewModel::class.java]

        binding.registerViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loading.observe(this, loadingLiveDataObserver)
        viewModel.loadError.observe(this, errorLiveDataObserver)
        viewModel.context = this
    }
}
