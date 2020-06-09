package br.com.beeahead.smart_config;

import android.content.Context;

import androidx.annotation.NonNull;

import br.com.beeahead.smart_config.esptouch.util.ByteUtil;
import br.com.beeahead.smart_config.esptouch.util.TouchNetUtil;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import io.flutter.plugin.common.PluginRegistry.Registrar;


/** SmartConfigPlugin */
public class SmartConfigPlugin implements FlutterPlugin, MethodCallHandler {
  private final String TAG = "SmartConfigPlugin";
  private static SmartConfigPlugin instance;
  private EspTouchAsyncTask mTask;
  private Object initializationLock = new Object();
  private MethodChannel smartconfigPluginChannel;
  private Context context;


  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger());
  }

  public static void registerWith(Registrar registrar) {
    if (instance == null) {
      instance = new SmartConfigPlugin();
    }
    instance.onAttachedToEngine(registrar.context(), registrar.messenger());
  }

  public void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
    synchronized (initializationLock) {
      if (smartconfigPluginChannel != null) {
        return;
      }
      this.context = applicationContext;
      smartconfigPluginChannel = new MethodChannel(messenger, "smart_config");
      smartconfigPluginChannel.setMethodCallHandler(this);
    }
  }
  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    context=null;
    smartconfigPluginChannel.setMethodCallHandler(null);
    smartconfigPluginChannel=null;
  }
  public SmartConfigPlugin(){}


  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    String method = call.method;
      if (method.equals("start")) {
        String ssid = call.argument("ssid");
        String bssid = call.argument("bssid");
        String password = call.argument("password");
        TriggerEspTouch(ssid, bssid, password);
      }
      else if (method.equals("stop")) {
        if (mTask != null) {
          mTask.cancelTask();
          mTask = null;
          result.success("stopped");
        }
        else {
          result.success("already stopped");
        }
      }
      else {
        result.notImplemented();
      }
  }

  private void TriggerEspTouch(String wifi_ssid, String wifi_bssid, String wifi_pass ){
    byte[] ssid = ByteUtil.getBytesByString(wifi_ssid);
    byte[] password = ByteUtil.getBytesByString(wifi_pass);
    byte [] bssid = TouchNetUtil.parseBssid2bytes(wifi_bssid);
    byte[] deviceCount = "1".getBytes();
    if (mTask != null) {
      mTask.cancelTask();
      mTask = null;
    }

    mTask = new EspTouchAsyncTask(context,smartconfigPluginChannel);
    mTask.execute(ssid, bssid, password, deviceCount);
  }



}
