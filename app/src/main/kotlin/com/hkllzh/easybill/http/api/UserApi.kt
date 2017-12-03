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
    @POST(V1_USER_LOGIN)
    fun login(@Body param: LoginReqParam): Observable<JsonObject>

    @POST(V1_USER_REGISTER)
    fun register(@Body param: RegisterReqParam): Observable<JsonObject>

    @POST(V1_USER_CHECK_LOGIN)
    fun checkLogin(@Body param: Any): Observable<JsonObject>
}


/**
 * 登录接口
 */
private const val V1_USER_LOGIN = "/v1/user/login"
/**
 * 注册接口
 */
private const val V1_USER_REGISTER = "/v1/user/register"
/**
 * 检查登录权限
 */
private const val V1_USER_CHECK_LOGIN = "/v1/user/checkLogin"

