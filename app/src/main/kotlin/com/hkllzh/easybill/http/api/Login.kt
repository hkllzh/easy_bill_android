package com.hkllzh.easybill.http.api

import com.google.gson.JsonObject
import com.hkllzh.easybill.http.EasyBillHttpClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 登录接口文件
 *
 * @author lizheng on 2017/11/30
 */
interface LoginApi {
    @POST("/v1/user/login")
    fun login(@Body param: HashMap<String, Any>): Observable<JsonObject>
}

object Login {
    fun login(): Observable<String> {
        return EasyBillHttpClient.getAPI(LoginApi::class.java).login(hashMapOf<String, Any>("username" to "35", "password" to "p")).map {
            it.toString()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}