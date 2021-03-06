package com.hkllzh.easybill.http.base

import com.google.gson.*
import com.hkllzh.easybill.event.ReLoggedIn
import com.hkllzh.easybill.event.RxBus
import com.hkllzh.easybill.util.ToastAlone
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers


// 网络请求错误
private const val KEY_HTTP_ERROR = "httpError"
// 服务端返回数据错误
private const val KEY_SERVER_DATA_ERROR = "serverDataError"
// 数据处理错误
private const val KEY_DATA_ERROR = "dataError"
private const val KEY_RE_LOGGED_IN = "reLoggedIn"
private const val KEY_RE_LOGGED_IN_MSG = "需要重新登录"


private const val JSON_KEY_CODE = "code"
private const val JSON_KEY_ERR_MSG = "errMsg"
// 真实数据
private const val JSON_KEY_DATA = "data"

/**
 * 一个接口数据的通用处理类
 *
 * @author lizheng on 2017/11/30
 */
open class BaseApiImpl {

    fun <T> dataConversion(action: () -> Observable<JsonObject>, dataConversion: DataConversion<T>): Observable<BaseResult<T>> {
        return action.invoke()
                // 网络错误处理
                .onErrorReturn { t: Throwable ->
                    Logger.e("第一次错误处理 网络错误")
                    t.printStackTrace()
                    val j = JsonObject()
                    j.addProperty(KEY_HTTP_ERROR, t.message)
                    j
                }
                // 网络错误数据 或者 第一层数据解包
                // 结果可能是 [] {} 1 null
                .map {

                    Logger.d("第一层数据解包$it")

                    val j = JsonObject()
                    if (it.has(KEY_HTTP_ERROR)) {
                        // 出现了网络错误
                        j.addProperty(KEY_HTTP_ERROR, it.get(KEY_HTTP_ERROR).asString)
                    } else {
                        if (it.has(JSON_KEY_CODE)) {
                            val code = it.get(JSON_KEY_CODE).asInt

                            when (code) {
                                0 -> {
                                    // 我们需要的数据
                                    if (it.has(JSON_KEY_DATA)) {
                                        j.add(JSON_KEY_DATA, it.get(JSON_KEY_DATA))
                                    } else {
                                        j.add(JSON_KEY_DATA, JsonNull.INSTANCE)
                                    }
                                }
                                10001 -> {
                                    // 验证错误，需要重新登录
                                    j.addProperty(KEY_RE_LOGGED_IN, KEY_RE_LOGGED_IN_MSG)
                                }
                                else -> {
                                    val errMsg = it.get(JSON_KEY_ERR_MSG).asString
                                    j.addProperty(KEY_SERVER_DATA_ERROR, errMsg)
                                }
                            }
                        } else {
                            j.addProperty(KEY_SERVER_DATA_ERROR, "数据格式错误")
                        }
                    }

                    if (j.has(JSON_KEY_DATA)) {
                        j.get(JSON_KEY_DATA)
                    } else {
                        j
                    }
                }
                // 上一个map处理数据错误
                .onErrorReturn { t: Throwable ->
                    Logger.e("第二次错误处理 数据错误")
                    t.printStackTrace()
                    val j = JsonObject()
                    j.addProperty(KEY_DATA_ERROR, "数据处理错误")
                    j
                }
                // 转为真实想要的对象
                .map(dataConversion)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}

abstract class DataConversion<T> : Function<JsonElement, BaseResult<T>> {
    override fun apply(jsonElement: JsonElement): BaseResult<T> {

        Logger.d("第二层数据解包${jsonElement.toString()}")

        // 错误处理
        if (jsonElement.isJsonObject) {
            val jo = jsonElement.asJsonObject
            if (jo.has(KEY_DATA_ERROR)) {
                return BaseResult(isError = true, errorText = jo.get(KEY_DATA_ERROR).asString)
            }
            if (jo.has(KEY_SERVER_DATA_ERROR)) {
                return BaseResult(isError = true, errorText = jo.get(KEY_SERVER_DATA_ERROR).asString)
            }
            if (jo.has(KEY_HTTP_ERROR)) {
                return BaseResult(isError = true, errorText = jo.get(KEY_HTTP_ERROR).asString)
            }
            if (jo.has(KEY_RE_LOGGED_IN)) {
                return BaseResult(isError = true, errorText = jo.get(KEY_RE_LOGGED_IN).asString)
            }
        }

        if (jsonElement.isJsonObject) {
            return parseData4JsonObject(jsonElement.asJsonObject)
        }
        if (jsonElement.isJsonArray) {
            return parseData4JsonArray(jsonElement.asJsonArray)
        }
        if (jsonElement.isJsonPrimitive) {
            return parseData4JsonPrimitive(jsonElement.asJsonPrimitive)
        }
        if (jsonElement.isJsonNull) {
            return parseData4JsonNull(jsonElement.asJsonNull)
        }
        return BaseResult()
    }

    /**
     * 从JsonObject里面解析数据，99%的接口都需要重写此方法
     *
     * @param dataJson 待解析数据
     * @return 解析后的数据
     */
    abstract fun parseData4JsonObject(dataJson: JsonObject): BaseResult<T>

    /**
     * 从JsonNull里面解析数据，也解析不到数据
     */
    fun parseData4JsonNull(dataJson: JsonNull): BaseResult<T> {
        return BaseResult()
    }

    /**
     * 从JsonArray里面解析数据，很少有这样的接口。因此不设置为抽象方法
     *
     * @param jsonArray 待解析数据
     * @return 解析后的数据
     */
    fun parseData4JsonArray(jsonArray: JsonArray): BaseResult<T> {
        return BaseResult()
    }

    /**
     * 从JsonPrimitive里面解析数据，极少有这样的接口。因此不设置为抽象方法
     *
     * @param jsonPrimitive 待解析数据
     * @return 解析后的数据
     */
    fun parseData4JsonPrimitive(jsonPrimitive: JsonPrimitive): BaseResult<T> {
        return BaseResult()
    }
}


class CommonObserver<T>(
        private val onNext: Consumer<T>,
        private val onError: Consumer<Throwable>,
        private val onComplete: Action,
        private val onSubscribe: Consumer<Disposable>) : Observer<BaseResult<T>> {

    constructor(onNext: Consumer<T>) : this(onNext, Consumer<Throwable> { }, Action { }, Consumer<Disposable> { })

    override fun onNext(t: BaseResult<T>) {
        if (t.isError) {
            Logger.e(t.errorText)
            if (t.errorText == KEY_RE_LOGGED_IN_MSG) {
                RxBus.post(ReLoggedIn())
            }
            ToastAlone.showShort(t.errorText)
        } else {
            onNext.accept(t.t)
        }
    }

    override fun onComplete() {
        onComplete.run()
    }

    override fun onSubscribe(d: Disposable) {
        onSubscribe.accept(d)
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        Logger.e(e.message)
        ToastAlone.showShort(e.message)
        onError.accept(e)
    }

}

fun <T> Observable<BaseResult<T>>.customSubscribe(
        onNext: (t: T) -> Unit = {},
        onError: (t: Throwable) -> Unit = {},
        onComplete: Action = Action { },
        onSubscribe: Consumer<Disposable> = Consumer { }
): Disposable {
    val co = CommonObserver<T>(
            onNext = Consumer { onNext.invoke(it) },
            onError = Consumer { onError.invoke(it) },
            onComplete = Action { onComplete.run() },
            onSubscribe = Consumer { onSubscribe.accept(it) }
    )
    return this.subscribe({ co.onNext(it) }, { co.onError(it) }, { co.onComplete() }, { co.onSubscribe(it) })
}

fun <T> Observable<BaseResult<T>>.customSubscribe(onNext: (t: T) -> Unit = {}): Disposable {
    val co = CommonObserver<T>(onNext = Consumer { onNext.invoke(it) })
    return this.subscribe({ co.onNext(it) }, { co.onError(it) }, { co.onComplete() }, { co.onSubscribe(it) })
}

class BaseResult<T> {
    // 结果是否错误
    var isError: Boolean = false
    // 错误文案
    var errorText: String = ""
    // 真实数据
    var t: T? = null

    constructor()

    constructor(t: T) {
        this.t = t
        this.isError = false
        this.errorText = ""
    }

    constructor(isError: Boolean, errorText: String) {
        this.errorText = errorText
        this.isError = isError
    }
}


