import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:smart_config/smart_config.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _result = 'connecting';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    var a = {"ssid":"home","password":"qazWSXedc"};
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      print("a $a");
      SmartConfig.startSmartConfig(a);
      SmartConfig.showESPs((call)async{
        if(call.method == "showESPs"){
          var dev = call.arguments.toString().split('-');
          print("devs len: ${dev.length}\n$dev\n\n");
          if (dev.length > 1) {
            print("OK");
            String uid = dev[0].toUpperCase();
            print("uid is $uid");
            setState(()=>_result=uid);
          }
          else{ 
            print("LENGTH 0 = erro...");
            setState(()=>_result="ERRO");
          }
        }
      });
    }
    on PlatformException {
      print("ERROOOOOOOO");
      setState(()=>_result="Erro");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_result'),
        ),
      ),
    );
  }
}
