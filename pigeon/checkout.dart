import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/moniepoint_checkout.g.dart',
  kotlinOut:
      'android/src/main/kotlin/com/moniepoint/moniepoint_checkout_sdk/lib/MoniepointCheckout.g.kt',
  kotlinOptions: KotlinOptions(),
  swiftOut: 'ios/Runner/MoniepointCheckout.g.swift',
  swiftOptions: SwiftOptions(),
  dartPackageName: 'moniepoint_checkout',
))
// #docregion host-definitions
enum CheckoutEnvironment { sandbox, production }

@HostApi()
abstract class MoniepointCheckout {
  void initialize({
    required String publicKey,
    CheckoutEnvironment environment = CheckoutEnvironment.sandbox,
  });

  @async
  String tokenize();
}
