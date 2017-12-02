package com.hkllzh.easybill.http.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.hkllzh.easybill.http.EasyBillHttpClient
import com.hkllzh.easybill.http.base.BaseApiImpl
import com.hkllzh.easybill.http.base.BaseResult
import com.hkllzh.easybill.http.base.DataConversion
import io.reactivex.Observable

/**
 * 登录接口参数
 */
data class LoginReqParam(
        @SerializedName("username") // 自定义序列化名字
        val username: String,
        val name: String? = null, // 空值不会被序列化
        val age: Int? = null,
        @SerializedName("password")
        val password: String)

/**
 * 登录接口返回值
 */
data class LoginResBean(
        val userId: Int,
        val username: String,
        val token: String
)

/**
 * 登录实现
 */
object LoginApiImpl : BaseApiImpl() {
    fun login(username: String, password: String): Observable<BaseResult<LoginResBean>> {
        return dataConversion({
            EasyBillHttpClient.userApi.login(LoginReqParam(username = username, password = password))
        }, object : DataConversion<LoginResBean>() {
            override fun parseData4JsonObject(dataJson: JsonObject): BaseResult<LoginResBean> {
                return try {
                    BaseResult(Gson().fromJson(dataJson, LoginResBean::class.java))
                } catch (e: Exception) {
                    e.printStackTrace()
                    BaseResult(true, "数据解析失败\n${e.message}")
                }
            }
        })
    }
}