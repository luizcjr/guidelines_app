package com.luiz.guidelines.ui.activities.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.luiz.guidelines.R
import com.luiz.guidelines.ui.activities.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Contador para que a splash abra a activity de login
        val handler = Handler()
        handler.postDelayed({
            startActivity(LoginActivity::class.java)
        }, 2500)
    }

    //Função para abrir a activity de login
    private fun startActivity(redirect: Class<*>) {
        startActivity(Intent(this@SplashActivity, redirect))
        finish()
    }

    //Quando o botão de retornar físico do aparelho é apertado, nada acontece para não atrapalhar a transição de tela
    override fun onBackPressed() {

    }
}
