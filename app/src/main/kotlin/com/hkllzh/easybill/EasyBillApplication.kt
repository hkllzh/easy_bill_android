package com.hkllzh.easybill

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.hkllzh.easybill.http.EasyBillHttpClient

/**
 * 项目Application
 *
 * @author lizheng on 2017/11/29
 */
class EasyBillApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 图片库初始化
        val pipelineConfig = ImagePipelineConfig.newBuilder(this)
                .setNetworkFetcher(OkHttpNetworkFetcher(EasyBillHttpClient.getClient()))
        Fresco.initialize(this, pipelineConfig.build())

    }
}