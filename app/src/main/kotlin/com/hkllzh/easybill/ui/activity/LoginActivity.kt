package com.hkllzh.easybill.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.hkllzh.easybill.R
import com.hkllzh.easybill.base.EBBaseActivity
import com.jakewharton.rxbinding2.view.RxView
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
                Log.e(TAG, DateTime.now().toString("yyyy-MM-dd_HH-mm-ss_SSS"))
                toast("创建账号")
            }
        }

        addDisposable {
            RxView.clicks(btnLogin).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                toast("登录")
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
