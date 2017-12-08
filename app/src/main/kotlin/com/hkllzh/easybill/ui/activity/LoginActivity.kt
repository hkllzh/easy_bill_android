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
import kotlinx.android.synthetic.main.act_login.*
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

/**
 * 登录页面
 *
 * @author lizheng 2017-11-30
 */
class LoginActivity : EBBaseActivity() {

    private var userId by Preference("userId", "")
    private var token by Preference("userToken", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)

        addDisposable {
            RxView.clicks(tvNoAccount).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                RegisterActivity.start(this)
            }
        }

        addDisposable(RxView.clicks(btnLogin).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            login()
        })

    }


    private fun login() {
        addDisposable {
            //            UserApiImpl.login(etUsername.text.toString(), etPassword.text.toString())
//                    .commonSubscribe(Consumer { it: LoginResBean ->
//                        Logger.d(it)
//
////                        doAsync {
////                            Database.saveUser(User(it.userId, it.username, it.token))
////                            val ls = Database.getUserData()?.getAll()
////                            Logger.d(ls)
////                            uiThread {
////                                Logger.d(ls)
////                                SplashActivity.start(this.weakRef.get()!!)
////                            }
////                        }
//
//
//                    })
            UserApiImpl
                    .login(etUsername.text.toString(), etPassword.text.toString())
                    .customSubscribe {
                        toast("登录成功")
                        userId = it.userId.toString()
                        token = it.token

                        MainActivity.start(this)
                        finish()
                    }
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
