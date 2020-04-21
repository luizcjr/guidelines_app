package com.luiz.guidelines.ui.fragments.guidelines

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.luiz.guidelines.R
import com.luiz.guidelines.databinding.GuidelinesBinding
import com.luiz.guidelines.models.Guidelines
import com.luiz.guidelines.models.User
import com.luiz.guidelines.ui.adapters.GuidelinesAdapter
import com.luiz.guidelines.ui.base.BaseFragment
import com.luiz.guidelines.ui.interfaces.GuidelinesListener
import com.luiz.guidelines.ui.view.AlertDefault
import com.luiz.guidelines.util.Utils
import java.util.*
import kotlin.collections.ArrayList

class GuidelinesFragment : BaseFragment() {

    private lateinit var viewModel: GuidelinesViewModel
    private var binding: GuidelinesBinding? = null
    private lateinit var adapterGuidelinesOpen: GuidelinesAdapter
    private lateinit var adapterGuidelinesFinished: GuidelinesAdapter

    companion object {
        fun newInstance() = GuidelinesFragment()
    }

    private val userDataObserver = Observer<User> { user ->
        user.let {
            getParentActivity()!!.title = user.name
        }
    }

    private val guidelinesOpenListDataObserver = Observer<ArrayList<Guidelines>> { list ->
        if (list != null && list.isNotEmpty()) {
            adapterGuidelinesOpen = GuidelinesAdapter(
                requireContext(),
                list,
                object : GuidelinesListener {
                    override fun action(position: Int) {
                        val itemAtual = list[position]

                        if (itemAtual.isOpen) {
                            itemAtual.isOpen = false
                            adapterGuidelinesOpen.notifyItemChanged(position)
                            updateGuideline(
                                false,
                                itemAtual.title,
                                itemAtual.descriptionShort,
                                itemAtual.description,
                                itemAtual.author,
                                itemAtual.id
                            )
                        }
                    }
                })

            binding!!.rvGuidelinesOpen.layoutManager = LinearLayoutManager(requireContext())
            binding!!.rvGuidelinesOpen.adapter = adapterGuidelinesOpen
        } else {
            binding!!.rvGuidelinesOpen.layoutManager = LinearLayoutManager(requireContext())
            binding!!.rvGuidelinesOpen.adapter = Utils.noResultAdapter(
                requireContext(),
                requireContext().getString(R.string.title_guidelines_open_empty),
                R.drawable.ic_sad
            )
        }
    }

    private val guidelinesFinishedListDataObserver = Observer<ArrayList<Guidelines>> { list ->
        if (list != null && list.isNotEmpty()) {
            adapterGuidelinesFinished = GuidelinesAdapter(
                requireContext(),
                list,
                object : GuidelinesListener {
                    override fun action(position: Int) {
                        val itemAtual = list[position]

                        if (!itemAtual.isOpen) {
                            itemAtual.isOpen = true
                            adapterGuidelinesFinished.notifyItemChanged(position)
                            updateGuideline(
                                true,
                                itemAtual.title,
                                itemAtual.descriptionShort,
                                itemAtual.description,
                                itemAtual.author,
                                itemAtual.id
                            )
                        }
                    }
                })

            binding!!.rvGuidelinesFinished.layoutManager = LinearLayoutManager(requireContext())
            binding!!.rvGuidelinesFinished.adapter = adapterGuidelinesFinished
        } else {
            binding!!.rvGuidelinesFinished.layoutManager = LinearLayoutManager(requireContext())
            binding!!.rvGuidelinesFinished.adapter = Utils.noResultAdapter(
                requireContext(),
                requireContext().getString(R.string.title_guidelines_finished_empty),
                R.drawable.ic_sad
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.guidelines_fragment, container, false)

        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GuidelinesViewModel::class.java)

        setupToolbar()
        setHasOptionsMenu(true)

        binding!!.guidelineViewModel = viewModel
        binding!!.lifecycleOwner = this

        viewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        viewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        viewModel.loadSuccess.observe(viewLifecycleOwner, successLiveDataObserver)
        viewModel.context = requireContext()
        viewModel.getUserInfo().observe(viewLifecycleOwner, userDataObserver)
        viewModel.getAllGuidelines()
        viewModel.guidelinesOpen.observe(viewLifecycleOwner, guidelinesOpenListDataObserver)
        viewModel.guidelinesFinish.observe(viewLifecycleOwner, guidelinesFinishedListDataObserver)
    }

    private fun updateGuideline(
        action: Boolean, title: String,
        descriptionShort: String,
        description: String,
        author: String, id: String
    ) {
        viewModel.updateGuideline(action, title, descriptionShort, description, author, id)
            .observe(this, Observer {
                if (it != null) {
                    viewModel.clear()
                    viewModel.getAllGuidelines()
                }
            })
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
