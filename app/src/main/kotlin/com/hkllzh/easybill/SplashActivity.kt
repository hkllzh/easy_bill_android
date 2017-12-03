package com.hkllzh.easybill

import android.os.Bundle
import com.hkllzh.easybill.base.EBBaseActivity
import com.hkllzh.easybill.event.ReLoggedIn
import com.hkllzh.easybill.event.RxBus
import com.hkllzh.easybill.http.api.UserApiImpl
import com.hkllzh.easybill.http.base.commonSubscribe
import com.hkllzh.easybill.ui.activity.LoginActivity
import com.hkllzh.easybill.ui.activity.MainActivity
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

        addDisposable(RxBus.toObservable(ReLoggedIn::class.java).subscribe {
            LoginActivity.start(this)
            finish()
        })

        addDisposable {
            UserApiImpl.checkLogin().commonSubscribe(Consumer {
                MainActivity.start(this)
                finish()
            })
        }

    }
}
