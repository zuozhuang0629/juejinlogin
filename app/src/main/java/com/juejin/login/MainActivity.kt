package com.juejin.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.juejin.login.databinding.ActivityMainBinding
import com.juejin.login.databinding.DialogLoginBinding

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        mBinding.button.setOnClickListener {
            LoginDialog().show(supportFragmentManager, "")
        }
    }
}