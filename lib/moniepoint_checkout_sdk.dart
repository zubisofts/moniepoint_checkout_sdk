
import 'moniepoint_checkout_sdk_platform_interface.dart';
export 'package:moniepoint_checkout_sdk/src/moniepoint_checkout.g.dart';

class MoniepointCheckoutSdk {
  Future<String?> getPlatformVersion() {
    return MoniepointCheckoutSdkPlatform.instance.getPlatformVersion();
  }
}
