import 'package:flutter_test/flutter_test.dart';
import 'package:moniepoint_checkout_sdk/moniepoint_checkout_sdk.dart';
import 'package:moniepoint_checkout_sdk/moniepoint_checkout_sdk_platform_interface.dart';
import 'package:moniepoint_checkout_sdk/moniepoint_checkout_sdk_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMoniepointCheckoutSdkPlatform
    with MockPlatformInterfaceMixin
    implements MoniepointCheckoutSdkPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final MoniepointCheckoutSdkPlatform initialPlatform = MoniepointCheckoutSdkPlatform.instance;

  test('$MethodChannelMoniepointCheckoutSdk is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMoniepointCheckoutSdk>());
  });

  test('getPlatformVersion', () async {
    MoniepointCheckoutSdk moniepointCheckoutSdkPlugin = MoniepointCheckoutSdk();
    MockMoniepointCheckoutSdkPlatform fakePlatform = MockMoniepointCheckoutSdkPlatform();
    MoniepointCheckoutSdkPlatform.instance = fakePlatform;

    expect(await moniepointCheckoutSdkPlugin.getPlatformVersion(), '42');
  });
}
