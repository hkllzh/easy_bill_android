package com.hkllzh.easybill.util

import android.annotation.SuppressLint
import android.app.Application
import android.text.TextUtils
import android.widget.Toast
import java.lang.ref.WeakReference

@SuppressLint("ShowToast")
object ToastAlone {
    /**
     * 唯一的toast
     */
    private var mToast: Toast? = null

    private var mApplication: WeakReference<Application>? = null

    fun init(application: Application) {
        mApplication = WeakReference(application)
    }

    fun showShort(text: String?) {
        if (null == mApplication || null == mApplication?.get() || TextUtils.isEmpty(text)) {
            return
        }
        if (null == mToast) {
            mToast = Toast.makeText(mApplication?.get(), text, Toast.LENGTH_SHORT)
        }
        mToast!!.duration = Toast.LENGTH_SHORT
        mToast!!.setText(text)
        mToast!!.show()
    }

    fun showShort(textRid: Int) {
        if (null == mApplication || null == mApplication?.get()) {
            return
        }
        if (null == mToast) {
            mToast = Toast.makeText(mApplication?.get(), textRid, Toast.LENGTH_SHORT)
        }
        mToast!!.duration = Toast.LENGTH_SHORT
        mToast!!.setText(textRid)
        mToast!!.show()
    }

    fun showLong(text: String?) {
        if (null == mApplication || null == mApplication?.get() || TextUtils.isEmpty(text)) {
            return
        }
        if (null == mToast) {
            mToast = Toast.makeText(mApplication?.get(), text, Toast.LENGTH_LONG)
        }
        mToast!!.duration = Toast.LENGTH_LONG
        mToast!!.setText(text)
        mToast!!.show()
    }

    fun showLong(textRid: Int) {
        if (null == mApplication || null == mApplication?.get()) {
            return
        }
        if (null == mToast) {
            mToast = Toast.makeText(mApplication?.get(), textRid, Toast.LENGTH_SHORT)
        }
        mToast!!.duration = Toast.LENGTH_LONG
        mToast!!.setText(textRid)
        mToast!!.show()
    }
}