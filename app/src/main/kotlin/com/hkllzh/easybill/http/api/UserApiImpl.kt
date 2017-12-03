package com.hkllzh.easybill.http.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hkllzh.easybill.http.EasyBillHttpServer
import com.hkllzh.easybill.http.base.BaseApiImpl
import com.hkllzh.easybill.http.base.BaseResult
import com.hkllzh.easybill.http.base.DataConversion
import io.reactivex.Observable

/**
 * 用户相关接口实现
 *
 * @author lizheng on 2017/12/3
 */
object UserApiImpl : BaseApiImpl() {
    fun login(username: String, password: String): Observable<BaseResult<LoginResBean>> {
        return dataConversion({
            EasyBillHttpServer.userServer.login(LoginReqParam(username = username, password = password))
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

    fun register(username: String, password: String): Observable<BaseResult<RegisterResBean>> {
        return dataConversion({
            EasyBillHttpServer.userServer.register(RegisterReqParam(username, password))
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