package com.hkllzh.easybill.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hkllzh.easybill.R
import com.hkllzh.easybill.base.EBBaseActivity
import com.hkllzh.easybill.http.api.UserApiImpl
import com.hkllzh.easybill.http.base.customSubscribe
import com.hkllzh.easybill.util.delegate.Preference
import com.jakewharton.rxbinding2.view.RxView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.act_register.*
import me.yokeyword.fragmentation.SupportHelper.hideSoftInput

/**
 * 注册页面
 *
 * @author lizheng on 2017/12/1
 */
class RegisterActivity : EBBaseActivity() {

    private var userId by Preference("userId", "")
    private var token by Preference("userToken", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_register)

        addDisposable(RxView.clicks(root).subscribe {
            hideSoftInput(root)
        })

        addDisposable(RxView.clicks(btnRegister).subscribe {
            register()
        })
    }

    private fun register() {

        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val password2 = etPassword2.text.toString()

        if (password != password2) {
            tilPassword.error = "两次密码不一致"
            return
        }


        addDisposable(UserApiImpl.register(username, password).customSubscribe {
            Logger.i(it.toString())
            userId = it.userId.toString()
            token = it.token

            MainActivity.start(this)
            finish()
        })
    }

    companion object {
        private val TAG = "RegisterActivity"
        fun start(context: Context) {
            val starter = Intent(context, RegisterActivity::class.java)
            context.startActivity(starter)
        }
    }

}