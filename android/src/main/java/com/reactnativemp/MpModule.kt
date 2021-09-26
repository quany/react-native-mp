package com.reactnativemp

import android.util.Log
import com.alibaba.fastjson.JSON.parseObject
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import io.dcloud.feature.sdk.DCSDKInitConfig
import io.dcloud.feature.sdk.DCUniMPSDK
import io.dcloud.feature.sdk.MenuActionSheetItem
import com.alibaba.fastjson.JSONObject
import io.dcloud.feature.sdk.Interface.IUniMP
import io.dcloud.feature.sdk.DCUniMPCapsuleButtonStyle
import io.dcloud.feature.barcode2.BarcodeProxy.context
import android.widget.Toast

class MpModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  private var appid: String? = null
  private var path: String? = null
  private var iup: IUniMP? = null

  override fun getName(): String {
    return "Mp"
  }

  private fun sendEvent(evntName: String, params: Any) {
    var data = params
    if (params is JSONObject) {
      data = params.toString()
    }
    reactContext.getJSModule(RCTDeviceEventEmitter::class.java).emit(evntName, data)
  }

  @ReactMethod
  fun initialize(params: ReadableMap, promise: Promise) {
    try {
      val items = params.getArray("items")
      val capsule = params.getBoolean("capsule")
      val fontSize = params.getString("fontSize")
      val fontColor = params.getString("fontColor")
      val fontWeight = params.getString("fontWeight")
      val sheetItems: MutableList<MenuActionSheetItem> = ArrayList()
      for (i in 0 until items!!.size()) {
        val item = items.getMap(i)
        sheetItems.add(MenuActionSheetItem(item!!.getString("title"), item.getString("key")))
      }

      //首先构建 DCUniMPCapsuleButtonStyle胶囊按钮样式
      val style = DCUniMPCapsuleButtonStyle()

      //设置胶囊按钮背景颜色
      style.setBackgroundColor("#990099")
      //设置胶囊按钮“···｜x” 的字体颜色
      style.setTextColor("#090909")
      //设置胶囊按钮边框颜色
      style.setBorderColor("#777777")
      //设置胶囊按钮按下状态背景颜色
      style.setHighlightColor("#888888")
      //**以上目前可设置的样式**

      val config = DCSDKInitConfig.Builder().setCapsule(capsule).setMenuDefFontSize(fontSize)
        .setMenuDefFontColor(fontColor)
        .setMenuDefFontWeight(fontWeight)
        .setMenuActionSheetItems(sheetItems)
        .setCapsuleButtonStyle(style)
        .setEnableBackground(true)
        .build()
      val instance = DCUniMPSDK.getInstance()
      instance.setDefMenuButtonClickCallBack { appid, key -> sendEvent("MenuItemClick", key) }

      instance.setOnUniMPEventCallBack { appid, event, data, callback ->
        sendEvent("MPEventReceive", data)
        Log.d("cs", "onUniMPEventReceive    event=" + event);
        //回传数据给小程序
        callback.invoke("收到消息");
      }

      instance.setUniMPOnCloseCallBack { appid -> sendEvent("MPOnclose", appid) }
      instance.initialize(reactContext, config) { b -> promise.resolve(b) }
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  @ReactMethod
  fun launch(config: ReadableMap, promise: Promise) {
    try {
      var params: org.json.JSONObject? = null
      this.appid = config.getString("appid")
      if (config.hasKey("path")) {
        this.path = config.getString("path")
      }
      if (config.hasKey("params")) {
//        params = config.getString("params")
//        params = config.getMap()

//        params = JSONObject.parseObject(config.getString("params"))
//        log(111.0, config.getString("params"))
//        val p = config.getMap("params")
//        p.
      }
//      DCUniMPSDK.getInstance().openUniMP()

//      DCUniMPSDK.getInstance().startApp(context, "__UNI__04E3A11", MpSplashView::class.java)
      iup = DCUniMPSDK.getInstance().openUniMP(this.currentActivity, this.appid, MpSplashView::class.java, this.path, params)
//      DCUniMPSDK.getInstance().startActivityForUniMPTask()
//      DCUniMPSDK.getInstance().startApp(this.currentActivity, appid, MpSplashView::class.java, path, if (params == null) null else org.json.JSONObject(params))
      promise.resolve(null)
    } catch (e: Exception) {
      e.printStackTrace()
      promise.reject(e)
    }
  }

  @ReactMethod
  fun getAppBasePath(promise: Promise) {
    try {
      val path = DCUniMPSDK.getInstance().getAppBasePath(this.currentActivity)
      promise.resolve(path)
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  @ReactMethod
  fun isExistsApp(appid: String?, promise: Promise) {
    try {
      promise.resolve(DCUniMPSDK.getInstance().isExistsApp(appid))
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  @ReactMethod
  fun getRuningAppid(promise: Promise) {
    try {
      promise.resolve(this.appid)
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  @ReactMethod
  fun getCurrentPageUrl(promise: Promise) {
    try {
//      DCUniMPSDK.getInstance().getCurrentPageUrl()
      promise.resolve(path)
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  @ReactMethod
  fun getAppVersionInfo(appid: String?, promise: Promise) {
    try {
      val info = DCUniMPSDK.getInstance().getAppVersionInfo(appid)
      if (info === null) {
        promise.resolve(null)
        return
      }
      val map = Arguments.createMap()
      map.putString("name", info.getString("name"))
      map.putInt("code", info.getString("code").toInt())
      promise.resolve(map)
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  @ReactMethod
  fun closeCurrentApp(promise: Promise) {
    try {
      promise.resolve(iup?.closeUniMP())
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  @ReactMethod
  fun sendEvent(name: String?, data: ReadableMap?, promise: Promise) {
    try {
      promise.resolve(iup?.sendUniMPEvent(name, data))
    } catch (e: Exception) {
      promise.reject(e)
    }
  }

  @ReactMethod
  fun releaseWgtToRunPathFromPath(path: String?, promise: Promise) {
    try {
      val wgtPath = context.externalCacheDir!!.path + "/__UNI__04E3A11.wgt"
      DCUniMPSDK.getInstance().releaseWgtToRunPathFromePath(this.appid, path) { code, pArgs ->
        if (code == 1) { //释放wgt完成
          try {
            DCUniMPSDK.getInstance().openUniMP(context, "__UNI__04E3A11")
          } catch (e: Exception) {
            e.printStackTrace()
          }
        } else { //释放wgt失败
          Toast.makeText(context, "资源释放失败", Toast.LENGTH_SHORT).show()
        }
        null
      }

//      DCUniMPSDK.getInstance().getAppBasePath()
//      iup?.releaseWgtToRunPathFromePath(path) { code: Int, _: Any? ->
//        promise.resolve(code == 1)
//      }
    } catch (e: Exception) {
      promise.reject(e)
    }
  }
}
