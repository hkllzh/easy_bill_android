package com.hkllzh.easybill.base

import io.reactivex.disposables.Disposable

/**
 * 项目的 Fragment 基类
 *
 * @author lizheng on 2017/11/30
 */
open class EBBaseFragment : BaseSupportFragment() {
    fun addDisposable(action: () -> Disposable) {
        val act = activity
        if (null != act && act is EBBaseActivity) {
            act.addDisposable(action)
        }
    }

    fun addDisposable(disposable: Disposable) {
        val act = activity
        if (null != act && act is EBBaseActivity) {
            act.addDisposable(disposable)
        }
    }

}