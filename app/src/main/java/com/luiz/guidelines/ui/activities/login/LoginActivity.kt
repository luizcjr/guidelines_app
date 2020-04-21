package com.luiz.guidelines.ui.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseUser
import com.luiz.guidelines.R
import com.luiz.guidelines.databinding.LoginBinding
import com.luiz.guidelines.ui.activities.main.MainActivity
import com.luiz.guidelines.ui.base.BaseActivity
import com.luiz.guidelines.util.Utils.openActivity

class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // data binding class
        val binding: LoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)

        //ViewModel
        loginViewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]

        //Texto com tag em html
        binding.tvRegister.text = Html.fromHtml(getString(R.string.title_sign_up_login))

        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this

        loginViewModel.firebaseUser.observe(this, Observer {
            updateUI(it)
        })

        loginViewModel.loading.observe(this, loadingLiveDataObserver)
        loginViewModel.loadError.observe(this, errorLiveDataObserver)
        loginViewModel.context = this
    }

    override fun onStart() {
        super.onStart()

        updateUI(firebaseAuth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            openActivity<MainActivity> {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }

    //Quando o botão de retornar físico do aparelho é apertado, o aplicativo é fechado
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finishAndRemoveTask()
    }
}
