import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:moniepoint_checkout_sdk/moniepoint_checkout_sdk.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _token = 'Unknown';

  @override
  void initState() {
    super.initState();
    // initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> getPaymentToken() async {
    try {
      final checkout = MoniepointCheckout();
      await checkout.initialize(
        publicKey: 'pk_sbox_pmfui27bywspolwlmqej5b6gvmr',
        environment: CheckoutEnvironment.sandbox
      );
      final token = await checkout.tokenize();
      setState(() {
        _token = token;
      });
    } on PlatformException catch (e) {
      setState(() {
        _token = "Failed to get token: '${e.message}'.";
      });
    } on Exception catch (e) {
      setState(() {
        _token = "Failed to get token: '${e.toString()}'.";
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Text('Payment Token: ', style: TextStyle(fontWeight: FontWeight.w500),),
                Text(_token, style: const TextStyle(fontWeight: FontWeight.w800),),
              ],
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                getPaymentToken();
              },
              child: const Text("Get Payment Token"),
            ),
          ],
        ),
      ),
    );
  }
}
