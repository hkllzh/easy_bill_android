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

    /**
     * 参考[UserApi.login]
     */
    fun login(username: String, password: String): Observable<BaseResult<LoginResBean>> {
        return dataConversion({
            EasyBillHttpServer.userApi.login(LoginReqParam(username = username, password = password))
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

    /**
     * 参考[UserApi.register]
     */
    fun register(username: String, password: String): Observable<BaseResult<RegisterResBean>> {
        return dataConversion({
            EasyBillHttpServer.userApi.register(RegisterReqParam(username, password))
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

    /**
     * 参考[UserApi.checkLogin]
     */
    fun checkLogin(): Observable<BaseResult<Any>> {
        return dataConversion({
            //            Observable
//                    .create<User> {
//                        val ls = Database.getUserData()?.getAll()
//
//
//
//                        it.onNext(if (ls == null) {
//                            User(-1, "", "'")
//                        } else {
//                            if (ls.size == 1) {
//                                User(ls[0].uid, ls[0].username, ls[0].token)
//                            } else {
//                                User(-1, "", "")
//                            }
//                        })
//                    }
//                    .flatMap {
//                        EasyBillHttpServer.userApi.login(LoginReqParam(it.username, it.token))
//                    }

            EasyBillHttpServer.userApi.checkLogin(Any())

        }, object : DataConversion<Any>() {
            override fun parseData4JsonObject(dataJson: JsonObject): BaseResult<Any> {
                return try {
                    BaseResult(Gson().fromJson(dataJson, Any::class.java))
                } catch (e: Exception) {
                    e.printStackTrace()
                    BaseResult(true, "数据解析失败\n${e.message}")
                }
            }

        })
    }

}