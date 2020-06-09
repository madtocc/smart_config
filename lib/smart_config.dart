import 'dart:async';

import 'package:flutter/services.dart';

class SmartConfig {
  static const MethodChannel _channel =
      const MethodChannel('smart_config');

  static void showESPs(Future<dynamic> handler(MethodCall call)){
    _channel.setMethodCallHandler(handler);
  }
  static void startSmartConfig([dynamic arguments]){
    _channel.invokeMethod("start",arguments);
  }
  static void stopSmartConfig(){
    _channel.invokeMethod("stop");
  }
}
