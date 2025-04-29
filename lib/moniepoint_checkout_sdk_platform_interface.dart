import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'moniepoint_checkout_sdk_method_channel.dart';

abstract class MoniepointCheckoutSdkPlatform extends PlatformInterface {
  /// Constructs a MoniepointCheckoutSdkPlatform.
  MoniepointCheckoutSdkPlatform() : super(token: _token);

  static final Object _token = Object();

  static MoniepointCheckoutSdkPlatform _instance = MethodChannelMoniepointCheckoutSdk();

  /// The default instance of [MoniepointCheckoutSdkPlatform] to use.
  ///
  /// Defaults to [MethodChannelMoniepointCheckoutSdk].
  static MoniepointCheckoutSdkPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MoniepointCheckoutSdkPlatform] when
  /// they register themselves.
  static set instance(MoniepointCheckoutSdkPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
