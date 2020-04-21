package com.luiz.guidelines.ui.fragments.guidelines

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.luiz.guidelines.models.Guidelines
import com.luiz.guidelines.models.User
import com.luiz.guidelines.ui.activities.include_guidelines.IncludeGuidelinesActivity
import com.luiz.guidelines.ui.activities.login.LoginActivity
import com.luiz.guidelines.ui.base.BaseViewModel
import com.luiz.guidelines.util.Utils.openActivity

class GuidelinesViewModel : BaseViewModel() {

    var response = MutableLiveData<User>()

    var idUser = ""
    var nameUser = ""
    var emailUser = ""
    var passwordUser = ""

    private lateinit var mDatabaseReferenceGuideline: DatabaseReference
    var updateResponse = MutableLiveData<String>()

    val guidelinesOpen by lazy { MutableLiveData<ArrayList<Guidelines>>() }
    val guidelinesFinish by lazy { MutableLiveData<ArrayList<Guidelines>>() }

    fun getAllGuidelines() {
        loading.value = true

        mDatabaseReference = mDatabase.reference.child("users")
        mDatabaseReferenceGuideline = mDatabase.reference.child("guidelines")

        val mUser = firebaseAuth.currentUser!!

        val guidelinesListOpen = ArrayList<Guidelines>()
        val guidelinesListFinish = ArrayList<Guidelines>()

        mDatabaseReferenceGuideline.child(mUser.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    loadError.value = "Falha ao cadastrar pauta. Tente novamente mais tarde!"
                    loading.value = false
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    loadError.value = null
                    loading.value = false

                    guidelinesOpen.value = null
                    guidelinesFinish.value = null

                    for (i in snapshot.children) {
                        val title = i.child("title").value as String
                        val descriptionShort = i.child("descriptionShort").value as String
                        val description = i.child("description").value as String
                        val author = i.child("author").value as String
                        val isOpen = i.child("open").value as Boolean
                        val id = i.child("id").value as String

                        if (isOpen) {
                            guidelinesListOpen.add(
                                Guidelines(
                                    title, descriptionShort, description, author, false, isOpen, id
                                )
                            )

                            guidelinesOpen.value = guidelinesListOpen
                        } else {
                            guidelinesListFinish.add(
                                Guidelines(
                                    title, descriptionShort, description, author, false, isOpen, id
                                )
                            )

                            guidelinesFinish.value = guidelinesListFinish
                        }
                    }
                }
            })
    }

    fun clear() {
        guidelinesOpen.value = arrayListOf()
        guidelinesFinish.value = arrayListOf()
    }

    fun updateGuideline(
        action: Boolean,
        title: String,
        descriptionShort: String,
        description: String,
        author: String,
        id: String
    ): MutableLiveData<String> {
        updateResponse = MutableLiveData()

        //Criando instancia do firebase
        mDatabaseReference = mDatabase.reference.child("users")
        mDatabaseReferenceGuideline = mDatabase.reference.child("guidelines")

        val mUser = firebaseAuth.currentUser!!

        mDatabaseReferenceGuideline.child(mUser.uid).child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    updateResponse.value = null

                    loadError.value = "Falha ao cadastrar pauta. Tente novamente mais tarde!"
                    loading.value = false
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.ref.child("open").setValue(action)
                    snapshot.ref.child("title").setValue(title)
                    snapshot.ref.child("descriptionShort").setValue(descriptionShort)
                    snapshot.ref.child("description").setValue(description)
                    snapshot.ref.child("author").setValue(author)

                    updateResponse.value = "Sucesso"

                    loadError.value = null
                    loading.value = false
                }
            })

        return updateResponse
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

                response.value = User(idUser, nameUser, emailUser, passwordUser)
            }
        })

        return response
    }

    fun logOut() {
        firebaseAuth.signOut()

        context.openActivity<LoginActivity>()
    }

    fun includeGuidelines() {
        context.openActivity<IncludeGuidelinesActivity>() {
            putExtra("name", nameUser)
        }
    }
}
