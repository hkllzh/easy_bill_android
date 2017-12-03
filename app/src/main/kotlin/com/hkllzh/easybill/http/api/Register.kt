package com.hkllzh.easybill.http.api

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