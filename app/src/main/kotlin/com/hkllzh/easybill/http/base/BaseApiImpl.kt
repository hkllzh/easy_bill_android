package com.hkllzh.easybill.http.base

import com.google.gson.*
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
// 真实数据
private const val KEY_DATA = "data"

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
                        if (it.has("code")) {
                            val code = it.get("code").asInt
                            if (0 != code) {
                                val errMsg = it.get("errMsg").asString
                                j.addProperty(KEY_SERVER_DATA_ERROR, errMsg)
                            } else {
                                // 我们需要的数据
                                if (it.has(KEY_DATA)) {
                                    j.add(KEY_DATA, it.get(KEY_DATA))
                                } else {
                                    j.add(KEY_DATA, JsonNull.INSTANCE)
                                }
                            }
                        } else {
                            j.addProperty(KEY_SERVER_DATA_ERROR, "数据格式错误")
                        }
                    }

                    if (j.has(KEY_DATA)) {
                        j.get(KEY_DATA)
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
     * @param dataJson 待解析数据 ，可能为空(null)
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


class CommonObserver<T>() : Observer<BaseResult<T>> {
    override fun onNext(t: BaseResult<T>) {
        if (t.isError) {
            Logger.e(t.errorText)
            ToastAlone.showShort(t.errorText)
        } else {
            _onNext.accept(t.t)
        }
    }

    override fun onComplete() {
        _onComplete.run()
    }

    override fun onSubscribe(d: Disposable) {
        _onSubscribe.accept(d)
    }

    override fun onError(e: Throwable) {
        Logger.e(e.message)
        ToastAlone.showShort(e.message)
        _onError.accept(e)
    }

    private var _onNext = Consumer<T> { }
    private var _onError = Consumer<Throwable> {}
    private val _onComplete = Action {}
    private val _onSubscribe = Consumer<Disposable> {}

    constructor(onNext: Consumer<T>) : this() {
        this._onNext = onNext
    }

    constructor(onNext: Consumer<T>, onError: Consumer<Throwable>) : this() {
        this._onNext = onNext
        this._onError = onError
    }
}


fun <T> Observable<BaseResult<T>>.commonSubscribe(onNext: Consumer<T>): Disposable {
    val co = CommonObserver<T>(onNext)
    return this.subscribe({ co.onNext(it) }, { co.onError(it) }, { co.onComplete() }, { co.onSubscribe(it) })
}

fun <T> Observable<BaseResult<T>>.commonSubscribe(onNext: Consumer<T>, onError: Consumer<Throwable>): Disposable {
    val co = CommonObserver<T>(onNext, onError)
    return this.subscribe({ co.onNext(it) }, { co.onError(it) }, { co.onComplete() }, { co.onSubscribe(it) })
}

class BaseResult<T> {
    // 结果是否错误
    var isError: Boolean = false
    // 错误文案
    var errorText: String = ""
    // 真实数据
    var t: T? = null

    constructor() {
    }

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


