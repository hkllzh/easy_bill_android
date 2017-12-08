package com.hkllzh.easybill.util

import com.hkllzh.easybill.base.EBBaseActivity
import com.hkllzh.easybill.base.EBBaseFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * 扩展类
 *
 * @author lizheng on 2017/12/2
 */
// 延时执行
fun EBBaseActivity.delayed(time: Long, block: () -> Unit, error: (t: Throwable) -> Unit = {}) {
    addDisposable(Observable.timer(time, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ block.invoke() }, { error.invoke(it) })
    )
}

// 延时执行
fun EBBaseFragment.delayed(time: Long, block: () -> Unit, error: (t: Throwable) -> Unit = {}) {
    addDisposable(Observable.timer(time, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ block.invoke() }, { error.invoke(it) })
    )
}