
import 'moniepoint_checkout_sdk_platform_interface.dart';

class MoniepointCheckoutSdk {
  Future<String?> getPlatformVersion() {
    return MoniepointCheckoutSdkPlatform.instance.getPlatformVersion();
  }
}
