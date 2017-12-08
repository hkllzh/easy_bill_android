package com.hkllzh.easybill.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 项目的 Fragment 基类
 *
 * @author lizheng on 2017/11/30
 */
abstract class EBBaseFragment : BaseSupportFragment() {

    private lateinit var mCompositeDisposable: CompositeDisposable

    protected lateinit var mRootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater?.inflate(layoutId(), container, false)!!
        initView()
        return mRootView
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.dispose()
    }

    @LayoutRes
    abstract fun layoutId(): Int

    abstract fun initView()

    fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

}