package com.luiz.guidelines.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.luiz.guidelines.models.User
import com.luiz.guidelines.ui.activities.main.MainActivity
import com.luiz.guidelines.util.Utils

abstract class BaseFragment : Fragment() {
    protected val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        if (isLoading) {
            Utils.onStartLoading(requireContext())
        } else {
            Utils.onStopLoading()
        }
    }

    protected val errorLiveDataObserver = Observer<String> { error ->
        if (error != null) {
            Utils.alertError(error, requireContext())
        }
    }

    protected val userLiveDataObserver = Observer<User> { user ->
        user.let {
            getParentActivity()!!.title = user.name
        }
    }

    protected val successLiveDataObserver = Observer<String> { success ->
        if (success != null) {
            Utils.alertSuccess(success, requireContext())
        }
    }

    protected fun getParentActivity(): MainActivity? {
        return activity as MainActivity?
    }
}