package com.hkllzh.easybill.http.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 用户相关的Api
 *
 * @author lizheng on 2017/12/2
 */
interface UserApi {
    @POST("/v1/user/login")
    fun login(@Body param: LoginReqParam): Observable<JsonObject>

    @POST("/v1/user/register")
    fun register(@Body param: RegisterReqParam): Observable<JsonObject>
}