package com.reactnativemp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONObject;

import java.util.ArrayList;

import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.feature.sdk.DCSDKInitConfig;
import io.dcloud.feature.sdk.DCUniMPCapsuleButtonStyle;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IDCUniMPOnCapsuleMenuButtontCallBack;
import io.dcloud.feature.sdk.Interface.IDCUniMPPreInitCallback;
import io.dcloud.feature.sdk.Interface.IMenuButtonClickCallBack;
import io.dcloud.feature.sdk.Interface.IOnUniMPEventCallBack;
import io.dcloud.feature.sdk.Interface.IUniMPOnCloseCallBack;
import io.dcloud.feature.sdk.MenuActionSheetItem;
import io.dcloud.feature.unimp.DCUniMPJSCallback;

@ReactModule(name = MpModule.NAME)
public class MpModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Mp";

  public MpModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  private boolean hasValue(ReadableMap rm, String key) {
    return rm.hasKey(key) && !rm.isNull(key);
  }

  private void sendEvent(String eventName, WritableMap data) {
    this.getReactApplicationContext()
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, data);
  }

  @ReactMethod
  public void addListener(String eventName) {
    // Set up any upstream listeners or background tasks as necessary
    Log.i("addListener eventName", eventName);
  }
  @ReactMethod
  public void removeListeners(Integer count) {
    // Remove upstream listeners, stop unnecessary background tasks
    Log.i("removeListeners count", String.valueOf(count));
  }

  @ReactMethod
  public void initialize(ReadableMap params, Promise promise) {
    try {
      ReactApplicationContext cxt = this.getReactApplicationContext();
      DCSDKInitConfig.Builder builder = new DCSDKInitConfig.Builder();
      if (params != null) {
        if (hasValue(params, "menus")) {
          ReadableArray menus = params.getArray("menus");
          int len = menus.size();
          if (len > 0) {
            ArrayList<MenuActionSheetItem> sheets = new ArrayList();
            for (int i = 0; i < len; i++) {
              ReadableMap m = menus.getMap(i);
              sheets.add(new MenuActionSheetItem(m.getString("title"), m.getString("key")));
            }
            builder.setMenuActionSheetItems(sheets);
          }
        }

        if (hasValue(params, "capsule")) {
          builder.setCapsule(params.getBoolean("capsule"));
        }

        if (hasValue(params, "enableBackground")) {
          builder.setEnableBackground(params.getBoolean("enableBackground"));
        }

        if (hasValue(params, "isFromRecents")) {
          builder.setUniMPFromRecents(params.getBoolean("isFromRecents"));
        }

        if (hasValue(params, "fontSize")) {
          builder.setMenuDefFontSize(params.getString("fontSize"));
        }

        if (hasValue(params, "fontColor")) {
          builder.setMenuDefFontColor(params.getString("fontColor"));
        }

        if (hasValue(params, "fontWeight")) {
          builder.setMenuDefFontWeight(params.getString("fontWeight"));
        }

        if (hasValue(params, "capsuleButtonStyle")) {
          ReadableMap capsuleButtonStyle = params.getMap("capsuleButtonStyle");
          DCUniMPCapsuleButtonStyle style = new DCUniMPCapsuleButtonStyle();
          String backgroundColor = params.getString("backgroundColor");
          if (backgroundColor != null) {
            style.setBackgroundColor(backgroundColor);
          }
          String borderColor = params.getString("borderColor");
          if (borderColor != null) {
            style.setBorderColor(borderColor);
          }
          String highlightColor = params.getString("highlightColor");
          if (highlightColor != null) {
            style.setHighlightColor(highlightColor);
          }
          String textColor = params.getString("textColor");
          if (textColor != null) {
            style.setTextColor(textColor);
          }
          builder.setCapsuleButtonStyle(style);
        }
      }
//      DCUniMPSDK.getInstance().setCapsuleMenuButtonClickCallBack(new IDCUniMPOnCapsuleMenuButtontCallBack() {
//        @Override
//        public void menuButtonClicked(String s) {
//          cxt.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
//            .emit("onCapsuleMenuButtonClick", s);
//        }
//      });
//      DCUniMPSDK.getInstance().setOnUniMPEventCallBack(new IOnUniMPEventCallBack() {
//        @Override
//        public void onUniMPEventReceive(String s, String s1, Object o, DCUniMPJSCallback dcUniMPJSCallback) {
//          dcUniMPJSCallback.invoke(o); // 小程序js回调接口
//        }
//      });
      DCUniMPSDK.getInstance().setDefMenuButtonClickCallBack(new IMenuButtonClickCallBack() {
        @Override
        public void onClick(String appid, String key) {
          WritableMap map = Arguments.createMap();
          map.putString("key", key);
          map.putString("appid", appid);
          cxt.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("onMenuButtonClick", map);
        }
      });
      DCUniMPSDK.getInstance().setUniMPOnCloseCallBack(new IUniMPOnCloseCallBack() {
        @Override
        public void onClose(String appid) {
          cxt.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("onCloseMp", appid);
        }
      });

      DCUniMPSDK.getInstance().initialize(cxt, builder.build(), new IDCUniMPPreInitCallback() {
        @Override
        public void onInitFinished(boolean isSuccess) {
          promise.resolve(isSuccess);
        }
      });
    } catch (Exception e) {
      promise.reject(e);
    }
  }

  @ReactMethod
  public void releaseWgtToRunPathFromPath(String wgtPath, String appId, Promise promise) {
    try {
      DCUniMPSDK.getInstance().releaseWgtToRunPathFromePath(appId, wgtPath, new ICallBack() {
        @Override
        public Object onCallBack(int code, Object pArgs) {
          if (code == 1) {//释放wgt完成
            promise.resolve(pArgs);
          } else {//释放wgt失败
            promise.reject(code + pArgs.toString());
          }
          return null;
        }
      });
    } catch (Exception e) {
      promise.reject(e);
    }
  }

  @ReactMethod
  public void isExistsApp(String appid, Promise promise) {
    try {
      promise.resolve(DCUniMPSDK.getInstance().isExistsApp(appid));
    } catch (Exception e) {
      promise.reject(e);
    }
  }

  @ReactMethod
  public void launch(ReadableMap config, Promise promise) {
    try {
      String path = null;
      JSONObject params = null;
      Class splash = null;
      String appid = config.getString("appid");
      if (hasValue(config, "path")) {
        path = config.getString("path");
      }
      if (config.hasKey("path")) {
        path = config.getString("path");
      }
      if (hasValue(config, "params")) {
        params = ReactNativeJson.convertMapToJson(config.getMap("params"));
      }
      if (hasValue(config, "splash")) {
        if (config.getBoolean("splash")) {
          splash = MpSplashView.class;
        }
      }
      Log.i("launch", params.toString());
      DCUniMPSDK.getInstance().openUniMP(this.getCurrentActivity(), appid, splash, path, params);
      promise.resolve(true);
    } catch (Exception e) {
      promise.reject(e);
    }
  }

  @ReactMethod
  public void getAppVersionInfo(String appid, Promise promise) {
    try {
      JSONObject info = DCUniMPSDK.getInstance().getAppVersionInfo(appid);
      if (info == null) {
        promise.resolve("");
      }
//      WritableMap map = Arguments.createMap();
//      map.putString("name", info.getString("name"));
//      map.putInt("code", new Integer(info.getString("code")));
      promise.resolve(ReactNativeJson.convertJsonToMap(info));
    } catch (Exception e) {
      promise.reject(e);
    }
  }
}
