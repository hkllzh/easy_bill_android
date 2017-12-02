package com.hkllzh.easybill.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hkllzh.easybill.R
import com.hkllzh.easybill.base.EBBaseActivity
import com.hkllzh.easybill.db.Database
import com.hkllzh.easybill.db.User
import com.hkllzh.easybill.http.api.LoginApiImpl
import com.hkllzh.easybill.http.api.LoginResBean
import com.hkllzh.easybill.http.base.commonSubscribe
import com.jakewharton.rxbinding2.view.RxView
import com.orhanobut.logger.Logger
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.act_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
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
                RegisterActivity.start(this)
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
                        doAsync {
                            Database.saveUser(User(it.userId, it.username, it.token))
                            val ls = Database.getUserData()?.getAll()
                            Logger.d(ls)
                            uiThread {
                                Logger.d(ls)
                                MainActivity.start(this.weakRef.get()!!)
                            }
                        }
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
