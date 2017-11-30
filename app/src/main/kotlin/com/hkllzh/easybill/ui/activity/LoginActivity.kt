package com.hkllzh.easybill.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hkllzh.easybill.R
import com.hkllzh.easybill.base.EBBaseActivity

/**
 * 登录页面
 *
 * @author lizheng 2017-11-30
 */
class LoginActivity : EBBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)
    }

    companion object {
        private val TAG = "LoginActivity"
        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }

}
