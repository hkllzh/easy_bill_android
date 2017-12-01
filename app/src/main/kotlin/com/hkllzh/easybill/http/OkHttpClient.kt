package com.hkllzh.easybill.http

import com.hkllzh.easybill.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


/**
 * OkHttp 客户端
 *
 * @author lizheng on 2017/11/29
 */
object EasyBillHttpClient {
    private val APICache = ConcurrentHashMap<String, Any>()
    private var mClient: OkHttpClient? = null
    private val mHttpLog = HttpLoggingInterceptor()

    fun getClient(): OkHttpClient {
        if (null != mClient) {
            return mClient!!
        }

        mHttpLog.level = HttpLoggingInterceptor.Level.BODY

        mClient = OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(mHttpLog)
                .build()
        return mClient!!
    }

    fun <T> getAPI(clazz: Class<T>): T {
        var api = APICache[clazz.name] as T?
        if (api == null) {
            synchronized(EasyBillHttpClient::class.java) {
                if (api != null) {
                    return api!!
                } else {
                    api = Retrofit.Builder().client(getClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .baseUrl(Constant.Net.BASE_URL)
                            .build().create(clazz)
                    APICache.put(clazz.name, api!!)
                }

            }
        }
        return api!!
    }
}