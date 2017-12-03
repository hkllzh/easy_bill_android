package com.hkllzh.easybill.event

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * 时间总线的 RxJava 实现
 *
 * @author lizheng on 2017/12/3
 */
object RxBus {
    val mBus: Subject<Any>

    init {
        mBus = PublishSubject.create()
    }

    fun post(event: Any) {
        mBus.onNext(event)
    }

    fun <T> toObservable(clazz: Class<T>) = mBus.ofType(clazz)
}