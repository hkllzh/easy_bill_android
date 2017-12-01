package com.hkllzh.easybill.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hkllzh.easybill.R
import com.hkllzh.easybill.base.EBBaseActivity
import com.hkllzh.easybill.http.api.LoginApiImpl
import com.hkllzh.easybill.http.api.LoginResBean
import com.hkllzh.easybill.http.base.commonSubscribe
import com.jakewharton.rxbinding2.view.RxView
import com.orhanobut.logger.Logger
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.act_login.*
import org.jetbrains.anko.toast
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

/**
 * 登录页面
 *
 * @author lizheng 2017-11-30
 */
class LoginActivity : EBBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)

        addDisposable {
            RxView.clicks(tvNoAccount).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                Logger.e(DateTime.now().toString("yyyy-MM-dd_HH-mm-ss_SSS"))
                toast("创建账号")
            }
        }

        addDisposable(RxView.clicks(btnLogin).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            login()
        })

    }


    private fun login() {
        addDisposable {
            LoginApiImpl.login(etUsername.text.toString(), etPassword.text.toString())
                    .commonSubscribe(Consumer { it: LoginResBean ->
                        Logger.d(it)
                        toast("登录成功")
                    })
        }
    }

    companion object {
        private val TAG = "LoginActivity"
        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }

}
