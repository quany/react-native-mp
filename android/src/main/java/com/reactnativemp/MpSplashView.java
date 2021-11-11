package com.reactnativemp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactRootView;
import io.dcloud.feature.sdk.Interface.IDCUniMPAppSplashView;

public class MpSplashView implements IDCUniMPAppSplashView {
  ReactRootView splashView;

  @Override
  public View getSplashView(Context context, String s, String s1, String s2) {
    splashView = new ReactRootView(context);
    Bundle bundle = new Bundle();
    bundle.putString("appid", s);
    bundle.putString("path", s1);
    bundle.putString("params", s2);
    Log.i("args[0]", s);
    Log.i("args[1]", s1);
    Log.i("args[2]", s2);
    ReactApplication app = (ReactApplication) context.getApplicationContext();
    splashView.startReactApplication(app.getReactNativeHost().getReactInstanceManager(), "MpModules.splashView", bundle);
    return splashView;
  }

  @Override
  public void onCloseSplash(ViewGroup rootView) {
    if(rootView != null)
      rootView.removeView(splashView);
  }
}
