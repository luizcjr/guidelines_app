package com.luiz.guidelines.ui.activities.include_guidelines

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.luiz.guidelines.R
import com.luiz.guidelines.databinding.IncludeGuidelineBinding
import com.luiz.guidelines.ui.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar.view.*

class IncludeGuidelinesActivity : BaseActivity() {

    private lateinit var viewModel: IncludeGuidelinesViewModel
    private var binding: IncludeGuidelineBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // data binding class
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_include_guidelines)

        //ViewModel
        viewModel = ViewModelProviders.of(this)[IncludeGuidelinesViewModel::class.java]

        binding!!.toolbar.toolbarTitle.text = getString(R.string.title_include_guideline)
        binding!!.toolbar.toolbar.setNavigationOnClickListener { onBackPressed() }

        val name = intent.getStringExtra("name")
        name.let {
            viewModel.author.value = name
        }

        binding!!.includeGuidelineViewModel = viewModel
        binding!!.lifecycleOwner = this

        viewModel.loading.observe(this, loadingLiveDataObserver)
        viewModel.loadError.observe(this, errorLiveDataObserver)
        viewModel.context = this
    }
}
