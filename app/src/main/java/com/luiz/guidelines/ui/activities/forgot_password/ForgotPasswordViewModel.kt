package com.luiz.guidelines.ui.activities.forgot_password

import com.luiz.guidelines.ui.activities.login.LoginActivity
import com.luiz.guidelines.ui.base.BaseViewModel
import com.luiz.guidelines.util.Utils.openActivity
import com.luiz.guidelines.util.Utils.toast
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail

class ForgotPasswordViewModel : BaseViewModel() {
    val email = ValiFieldEmail().addEmailValidator("E-mail inválido!")
    val form = ValiFiForm(email)

    fun sendToken() {
        loading.value = true

        firebaseAuth.sendPasswordResetEmail(email.value.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loadError.value = null
                context.toast("E-mail enviado com sucesso!")
                loading.value = false
                redirectToLogin()
            } else {
                loadError.value = "Falha ao enviar o e-mail. Tente novamente mais tarde!"
                loading.value = false
            }
        }
    }

    private fun redirectToLogin() {
        context.openActivity<LoginActivity>()
    }
}