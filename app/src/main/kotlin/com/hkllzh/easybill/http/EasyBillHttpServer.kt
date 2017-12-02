package com.hkllzh.easybill.http

import com.hkllzh.easybill.Constant
import com.hkllzh.easybill.http.api.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * OkHttp 客户端
 *
 * @author lizheng on 2017/11/29
 */
object EasyBillHttpServer {

    val okHttpClient: OkHttpClient by lazy {
        val mHttpLog = HttpLoggingInterceptor()

        mHttpLog.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(mHttpLog)
                .build()
    }

    val userServer: UserApi get() = retrofit.create(UserApi::class.java)

    private val retrofit = Retrofit.Builder().client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constant.Net.BASE_URL)
            .build()


}