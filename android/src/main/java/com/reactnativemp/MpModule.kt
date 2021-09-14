package com.reactnativemp

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

class MpModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "Mp"
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
//        val config = DCSDK
      }
    }


}
