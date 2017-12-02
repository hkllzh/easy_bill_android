package com.hkllzh.easybill

import android.os.Bundle
import com.hkllzh.easybill.base.EBBaseActivity
import com.hkllzh.easybill.ui.activity.LoginActivity
import com.hkllzh.easybill.util.delayed
import com.orhanobut.logger.Logger

/**
 * 主页
 *
 * @author lizheng on 2017/11/29
 */
class SplashActivity : EBBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_aplash)
        Logger.d("app start 1")

        delayed(2000, {
            Logger.d("app start 2")
            LoginActivity.start(this)
            finish()
        })

    }
}
