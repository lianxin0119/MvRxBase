package space.lianxin.base.net.http.converter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import space.lianxin.base.BuildConfig

/**
 * 返回值data为空或者data直接为常量的转换.
 * Create by YangYang on 2018/11/7 11:53.
 */
class AbnormalResponseBodyConverter<T> constructor(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {

    private val keyData = "data"
    private val keyCode = "code"
    private val emptyDataListConver = """{"data":[]}"""
    private val emptyDataObjectConver = """{"data":{}}"""
    private val emptyDataListAdd = """{"data":[],"""
    private val emptyDataObjectAdd = """{"data":{},"""

    override fun convert(value: ResponseBody): T {
        if (value.contentLength() > Int.MAX_VALUE) {
            // 超出String字符串的长度
            value.use {
                val jsonReader = gson.newJsonReader(it.charStream())
                return adapter.read(jsonReader)
            }
        }

        var resStr = String(value.bytes())
        val resJsonOb = JSONObject(resStr)
        if (resStr.isNotEmpty()
            && resStr.startsWith("{")
            && resJsonOb.has(keyCode)
            && resJsonOb.getInt(keyCode) == 200
            && !resJsonOb.has(keyData)
        ) {
            try {
                adapter.fromJson(emptyDataObjectConver)
                resStr = emptyDataObjectAdd + resStr.substring(1)
            } catch (e: Exception) {
                try {
                    adapter.fromJson(emptyDataListConver)
                    resStr = emptyDataListAdd + resStr.substring(1)
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (BuildConfig.DEBUG) {
                        Log.e("BodyConverter", "data is not list and object")
                    }
                }
            }
        }
        return adapter.fromJson(resStr)
    }
}