package com.hkllzh.easybill.base

import android.annotation.SuppressLint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@SuppressLint("Registered")
/**
 * 项目的 Act 基类
 *
 * @author lizheng on 2017/11/30
 */
open class EBBaseActivity : BaseSupportActivity() {
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()


    fun addDisposable(action: () -> Disposable) {
        mCompositeDisposable.add(action.invoke())
    }

    private fun clearDisposable() {
        mCompositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearDisposable()
    }
}