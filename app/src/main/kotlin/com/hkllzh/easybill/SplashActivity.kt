package com.hkllzh.easybill

import android.os.Bundle
import com.hkllzh.easybill.base.EBBaseActivity
import com.hkllzh.easybill.http.api.UserApiImpl
import com.hkllzh.easybill.http.base.commonSubscribe
import com.hkllzh.easybill.ui.activity.LoginActivity
import com.hkllzh.easybill.ui.activity.MainActivity
import com.orhanobut.logger.Logger
import io.reactivex.functions.Consumer

/**
 * 主页
 *
 * @author lizheng on 2017/11/29
 */
class SplashActivity : EBBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_aplash)

        addDisposable {
            UserApiImpl.checkLogin().commonSubscribe(Consumer {
                Logger.d("检查通过，进入主页")
                MainActivity.start(this)
            }, Consumer {
                Logger.d("检查错误，重新登录")
                LoginActivity.start(this)
            })
        }

    }
}
