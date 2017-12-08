package com.hkllzh.easybill.base

import android.annotation.SuppressLint
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@SuppressLint("Registered")
/**
 * 项目的 Act 基类
 *
 * @author lizheng on 2017/11/30
 */
open class EBBaseActivity : BaseSupportActivity() {
    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearDisposable()
    }

    fun addDisposable(disposable: Disposable) = mCompositeDisposable.add(disposable)

    private fun clearDisposable() = mCompositeDisposable.dispose()
}