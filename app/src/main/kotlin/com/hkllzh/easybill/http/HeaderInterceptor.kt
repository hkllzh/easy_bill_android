package com.hkllzh.easybill.http

import com.hkllzh.easybill.util.delegate.Preference
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 网络请求 Header
 *
 * @author lizheng on 2017/12/3
 */
object HeaderInterceptor : Interceptor {

    private var userId by Preference("userId", "")
    private var token by Preference("userToken", "")

    override fun intercept(chain: Interceptor.Chain?): Response {

        Logger.d("userId,token = $userId,$token")

        val builder = chain!!.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Token", "$userId,$token")
        return chain.proceed(builder.build())
    }
}