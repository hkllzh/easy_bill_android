package com.hkllzh.easybill.http.api

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.hkllzh.easybill.http.EasyBillHttpClient
import com.hkllzh.easybill.http.base.BaseApiImpl
import com.hkllzh.easybill.http.base.BaseResult
import com.hkllzh.easybill.http.base.DataConversion
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 登录接口文件
 *
 * @author lizheng on 2017/11/30
 */
interface LoginApi {
    @POST("/v1/user/login")
    fun login(@Body param: LoginReqParam): Observable<JsonObject>
}

data class LoginReqParam(
        @SerializedName("username") // 自定义序列化名字
        val username: String,
        val name: String? = null, // 空值不会被序列化
        val age: Int? = null,
        @SerializedName("password")
        val password: String)

data class LoginResBean(
        val userId: Int,
        val username: String,
        val token: String
)

object LoginApiImpl : BaseApiImpl() {
    fun login(): Observable<BaseResult<LoginResBean>> {
        return dataConversion({
            EasyBillHttpClient.getAPI(LoginApi::class.java).login(LoginReqParam(username = "35", password = "p"))
        }, object : DataConversion<LoginResBean>() {
            override fun parseData4JsonObject(dataJson: JsonObject): BaseResult<LoginResBean> {
                return BaseResult()
            }
        })
    }
}