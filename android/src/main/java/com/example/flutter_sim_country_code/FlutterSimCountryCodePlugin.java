package com.example.flutter_sim_country_code;

import android.content.Context;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FlutterSimCountryCodePlugin */
public class FlutterSimCountryCodePlugin implements FlutterPlugin, MethodCallHandler {

  private static final String CHANNEL_NAME = "flutter_sim_country_code";

  private MethodChannel mChannel;
  private Context mContext;

  private void initInstance(BinaryMessenger messenger, Context context) {
    mChannel = new MethodChannel(messenger, CHANNEL_NAME);
    mChannel.setMethodCallHandler(this);
    mContext = context;
  }

  @Override
  public void onMethodCall(MethodCall call, @NonNull Result result) {
    if (call.method.equals("getSimCountryCode")) {
      getSimCountryCode(result);
    } else {
      result.notImplemented();
    }
  }

  private void getSimCountryCode(Result result) {
    TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    if (manager != null) {
      String countryId = manager.getSimCountryIso();
      if (countryId != null && !countryId.isEmpty()) {
        result.success(countryId.toUpperCase());
        return;
      }
    }
    result.error("SIM_COUNTRY_CODE_ERROR", "Could not retrieve SIM country code", null);
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    initInstance(binding.getBinaryMessenger(), binding.getApplicationContext());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if (mChannel != null) {
      mChannel.setMethodCallHandler(null);
      mChannel = null;
    }
    mContext = null;
  }
}
