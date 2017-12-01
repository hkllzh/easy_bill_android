package com.hkllzh.easybill.http.base

import com.google.gson.JsonArray
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

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
                    Logger.d(".onErrorReturn1")
                    t.printStackTrace()
                    Logger.e(t.message)
                    val j = JsonObject()
                    j.addProperty("name", "lizheng")
                    j
                }
                // 网络错误数据 或者 第一层数据解包
                // 结果可能是 [] {} 1 null
                .map {
                    Logger.d(".map")
                    Logger.d(it)
                    val i = 0
                    val j = 12 / i
                    it
                }
                // 上一个map处理数据错误
                .onErrorReturn { t: Throwable ->
                    Logger.d(".onErrorReturn2")
                    t.printStackTrace()
                    val j = JsonObject()
                    j.addProperty("map", "onErrorReturn")
                    j
                }
                // 转为真实想要的对象
                .map(dataConversion)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}

abstract class DataConversion<T> : Function<JsonObject, BaseResult<T>> {
    override fun apply(jsonObject: JsonObject): BaseResult<T> {

        // 错误处理

        if (jsonObject.isJsonObject) {
            return parseData4JsonObject(jsonObject)
        }
        if (jsonObject.isJsonArray) {
            return parseData4JsonArray(jsonObject.asJsonArray)
        }
        if (jsonObject.isJsonPrimitive) {
            return parseData4JsonPrimitive(jsonObject.asJsonPrimitive)
        }
        if (jsonObject.isJsonNull) {
            return parseData4JsonNull(jsonObject.asJsonNull)
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
        _onError.accept(e)
    }

    var _onNext = Consumer<T> { }
    var _onError = Consumer<Throwable> {}
    val _onComplete = Action {}
    val _onSubscribe = Consumer<Disposable> {}

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


