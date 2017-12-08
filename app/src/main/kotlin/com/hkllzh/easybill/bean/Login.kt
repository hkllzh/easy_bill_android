package com.hkllzh.easybill.bean

import com.google.gson.annotations.SerializedName

/**
 * 登录接口参数
 */
data class LoginReqParam(
        @SerializedName("username") // 自定义序列化名字
        val username: String, // 用户名
        val password: String // 密码
)

/**
 * 登录接口返回值
 */
data class LoginResBean(
        val userId: Int,
        val username: String,
        val token: String
)