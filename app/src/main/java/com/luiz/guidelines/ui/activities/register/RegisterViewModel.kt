package com.luiz.guidelines.ui.activities.register

import android.content.Intent
import android.util.Log
import com.luiz.guidelines.ui.activities.main.MainActivity
import com.luiz.guidelines.ui.base.BaseViewModel
import com.luiz.guidelines.util.Utils.openActivity
import com.luiz.guidelines.util.Utils.toast
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldText

class RegisterViewModel : BaseViewModel() {

    val name = ValiFieldText().addNotEmptyValidator("Campo obrigatório!")
    val email = ValiFieldEmail().addEmailValidator("E-mail inválido!")
    val password =
        ValiFieldText().addMinLengthValidator("Senha curta! Mínimo seis caracteres.", 6)
    val form = ValiFiForm(email, name, password)

    fun sendRegisterUser() {
        loading.value = true

        mDatabaseReference = mDatabase.reference.child("users")

        firebaseAuth.createUserWithEmailAndPassword(
            email.value.toString(),
            password.value.toString()
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loadError.value = null
                loading.value = false

                val userId = firebaseAuth.currentUser!!.uid

                val currentUserDb = mDatabaseReference.child(userId)

                currentUserDb.child("id").setValue(userId)
                currentUserDb.child("name").setValue(name.value.toString())
                currentUserDb.child("email").setValue(email.value.toString())
                currentUserDb.child("password").setValue(password.value.toString())

                redirectToHome()
            } else {
                Log.d("_res", "Error: " + task.exception)
                loadError.value = "Falha ao cadastrar usuário. Tente novamente mais tarde!"
                loading.value = false
            }
        }
    }

    private fun redirectToHome() {
        context.toast("Cadastro efetuado com sucesso!")
        context.openActivity<MainActivity> {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}