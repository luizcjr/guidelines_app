package com.luiz.guidelines.ui.fragments.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.luiz.guidelines.R
import com.luiz.guidelines.databinding.ProfileBinding
import com.luiz.guidelines.models.User
import com.luiz.guidelines.ui.base.BaseFragment
import com.luiz.guidelines.ui.view.AlertDefault
import com.luiz.guidelines.util.Utils

class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val userDataObserver = Observer<User> { user ->
        user.let {
            getParentActivity()!!.title = user.name
        }
    }

    private lateinit var viewModel: ProfileViewModel
    private var binding: ProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.perfil, container, false)

        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        setupToolbar()
        setHasOptionsMenu(true)

        binding!!.profilleViewModel = viewModel
        binding!!.lifecycleOwner = this

        viewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        viewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        viewModel.loadSuccess.observe(viewLifecycleOwner, successLiveDataObserver)
        viewModel.context = requireContext()
        viewModel.getUserInfo().observe(viewLifecycleOwner, userDataObserver)
    }

    private fun setupToolbar() {
        getParentActivity()!!.setSupportActionBar(binding!!.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit) {
            showDialogExit()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDialogExit() {
        val alertDefault = AlertDefault(
            requireContext(),
            "Atenção",
            "Deseja realmente sair?",
            true
        )

        alertDefault.addButton("Não", R.style.ButtonOutline, View.OnClickListener {
            alertDefault.dismiss()
        })

        alertDefault.addButton("Sim", R.style.ButtonDefault, View.OnClickListener {
            viewModel.logOut()
            alertDefault.dismiss()
        })
        alertDefault.show()
    }
}
