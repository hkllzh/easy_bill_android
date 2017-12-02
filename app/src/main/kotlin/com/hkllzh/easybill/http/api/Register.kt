package com.hkllzh.easybill.http.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hkllzh.easybill.http.EasyBillHttpClient
import com.hkllzh.easybill.http.base.BaseApiImpl
import com.hkllzh.easybill.http.base.BaseResult
import com.hkllzh.easybill.http.base.DataConversion
import io.reactivex.Observable

/**
 * 注册 接口文件
 *
 * @author lizheng on 2017/12/1
 */

/**
 * 注册接口参数
 */
data class RegisterReqParam(
        val username: String,
        val password: String
)

/**
 * 注册接口返回值
 */
data class RegisterResBean(
        val userId: Int,
        val username: String,
        val token: String
)

object RegisterApiImpl : BaseApiImpl() {
    fun register(username: String, password: String): Observable<BaseResult<RegisterResBean>> {
        return dataConversion({
            EasyBillHttpClient.userApi.register(RegisterReqParam(username, password))
        }, object : DataConversion<RegisterResBean>() {
            override fun parseData4JsonObject(dataJson: JsonObject): BaseResult<RegisterResBean> {
                return try {
                    BaseResult(Gson().fromJson(dataJson, RegisterResBean::class.java))
                } catch (e: Exception) {
                    e.printStackTrace()
                    BaseResult(true, "数据解析失败\n${e.message}")
                }
            }

        })
    }
}

