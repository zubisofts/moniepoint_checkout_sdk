import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'moniepoint_checkout_sdk_platform_interface.dart';

/// An implementation of [MoniepointCheckoutSdkPlatform] that uses method channels.
class MethodChannelMoniepointCheckoutSdk extends MoniepointCheckoutSdkPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('moniepoint_checkout_sdk');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
