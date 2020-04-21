package com.luiz.guidelines.ui.activities.login

import android.content.Intent
import com.luiz.guidelines.ui.activities.forgot_password.ForgotPasswordActivity
import com.luiz.guidelines.ui.activities.main.MainActivity
import com.luiz.guidelines.ui.activities.register.RegisterActivity
import com.luiz.guidelines.ui.base.BaseViewModel
import com.luiz.guidelines.util.Utils.openActivity
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldText

class LoginViewModel : BaseViewModel() {

    val email = ValiFieldEmail().addEmailValidator("E-mail inválido!")
    val password =
        ValiFieldText().addMinLengthValidator("Senha curta!", 6)

    val form = ValiFiForm(email, password)

    fun sendLogin() {
        loading.value = true

        firebaseAuth.signInWithEmailAndPassword(email.value.toString(), password.value.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loadError.value = null
                    loading.value = false

                    val user = firebaseAuth.currentUser
                    firebaseUser.value = user
                } else {
                    loadError.value = "E-mail e/ou senha incorretos!"
                    loading.value = false

                    firebaseUser.value = null
                }
            }
    }

    //Função para encaminhar para o cadastro
    fun register() {
        context.openActivity<RegisterActivity>()
    }

    //Função para encaminhar para a recuperação de senha
    fun forgotPassword() {
        context.openActivity<ForgotPasswordActivity>()
    }

    private fun redirectToHome() {
        context.openActivity<MainActivity> {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}