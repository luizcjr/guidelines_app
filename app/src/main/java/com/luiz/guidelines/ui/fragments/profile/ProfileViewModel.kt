package com.luiz.guidelines.ui.fragments.profile

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.luiz.guidelines.models.User
import com.luiz.guidelines.ui.activities.login.LoginActivity
import com.luiz.guidelines.ui.base.BaseViewModel
import com.luiz.guidelines.util.Utils.openActivity
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldText

class ProfileViewModel : BaseViewModel() {

    val name = ValiFieldText().addNotEmptyValidator("Campo obrigatório!")
    val email = ValiFieldEmail().addEmailValidator("E-mail inválido!")
    val form = ValiFiForm(email, name)

    var response = MutableLiveData<User>()

    var idUser = ""
    var nameUser = ""
    var emailUser = ""
    var passwordUser = ""

    fun updateUser() {
        loading.value = true

        mDatabaseReference = mDatabase.reference.child("users")

        val userId = firebaseAuth.currentUser!!.uid
        val currentUserDb = mDatabaseReference.child(userId)

        currentUserDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                loadError.value = "Falha ao alterar dados do usuário. Tente novamente mais tarde!"
                loading.value = false
                loadSuccess.value = null
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                loadError.value = null
                loading.value = false
                loadSuccess.value = "Dados alterados com sucesso!"

                snapshot.ref.child("name").setValue(name.value.toString())
                snapshot.ref.child("email").setValue(email.value.toString())
                snapshot.ref.child("id").setValue(idUser)
                snapshot.ref.child("password").setValue(passwordUser)
            }

        })
    }

    fun getUserInfo(): MutableLiveData<User> {
        loading.value = true

        mDatabaseReference = mDatabase.reference.child("users")

        val mUser = firebaseAuth.currentUser!!
        val mUserReference = mDatabaseReference.child(mUser.uid)

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                loadError.value = "Falha ao recuperar dados do usuário. Tente novamente mais tarde!"
                loading.value = false
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                loadError.value = null
                loading.value = false

                idUser = snapshot.child("id").value as String
                nameUser = snapshot.child("name").value as String
                emailUser = snapshot.child("email").value as String
                passwordUser = snapshot.child("password").value as String

                name.value = nameUser
                email.value = emailUser

                response.value = User(idUser, nameUser, emailUser, passwordUser)
            }
        })

        return response
    }

    fun logOut() {
        firebaseAuth.signOut()

        context.openActivity<LoginActivity>()
    }
}
