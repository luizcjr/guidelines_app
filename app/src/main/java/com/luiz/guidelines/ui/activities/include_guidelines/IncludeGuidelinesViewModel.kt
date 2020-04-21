package com.luiz.guidelines.ui.activities.include_guidelines

import android.content.Intent
import com.google.firebase.database.DatabaseReference
import com.luiz.guidelines.models.Guideline
import com.luiz.guidelines.ui.activities.main.MainActivity
import com.luiz.guidelines.ui.base.BaseViewModel
import com.luiz.guidelines.util.Utils.openActivity
import com.luiz.guidelines.util.Utils.toast
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldText

class IncludeGuidelinesViewModel : BaseViewModel() {

    val title = ValiFieldText().addNotEmptyValidator("Campo obrigatório!")
    val shortDescription = ValiFieldText().addNotEmptyValidator("Campo obrigatório!")
    val description = ValiFieldText().addNotEmptyValidator("Campo obrigatório!")
    val author = ValiFieldText().addNotEmptyValidator("Campo obrigatório!")
    val form = ValiFiForm(title, shortDescription, description, author)

    private lateinit var mDatabaseReferenceGuideline: DatabaseReference

    fun registerGuideline() {
        loading.value = true

        mDatabaseReference = mDatabase.reference.child("users")
        mDatabaseReferenceGuideline = mDatabase.reference.child("guidelines")

        val mUser = firebaseAuth.currentUser!!

        val guidelineId = mDatabaseReferenceGuideline.push().key

        val guideline =
            Guideline(
                title.value.toString(),
                shortDescription.value.toString(),
                description.value.toString(),
                author.value.toString(),
                true,
                guidelineId!!
            )

        mDatabaseReferenceGuideline.child(mUser.uid).child(guidelineId).setValue(guideline)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loadError.value = null
                    loading.value = false

                    redirectToHome()
                } else {
                    loadError.value = "Falha ao cadastrar pauta. Tente novamente mais tarde!"
                    loading.value = false
                }
            }
    }

    private fun redirectToHome() {
        context.toast("Pauta cadastrada com sucesso!")
        context.openActivity<MainActivity> {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}