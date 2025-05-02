import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/moniepoint_checkout.g.dart',
  kotlinOut:
      'android/src/main/kotlin/com/moniepoint/moniepoint_checkout_sdk/lib/MoniepointCheckout.g.kt',
  kotlinOptions: KotlinOptions(),
  swiftOut: 'ios/Classes/MoniepointCheckout.g.swift',
  swiftOptions: SwiftOptions(),
  dartPackageName: 'moniepoint_checkout',
))
// #docregion host-definitions

enum CheckoutEnvironment {
  sandbox,
  production,
}

class CkoDesignToken {
  CkoColorTokens? colorTokens;
  CkoBorderRadiusToken? borderRadius;
  CkoBorderRadiusToken? borderFormRadius;
  Map<String?, CkoFont>? fonts;
}

class CkoColorTokens {
  int? colorAction;
  int? colorBackground;
  int? colorBorder;
  int? colorDisabled;
  int? colorPrimary;
  int? colorFormBackground;
  int? colorFormBorder;
  int? colorInverse;
  int? colorOutline;
  int? colorSecondary;
  int? colorSuccess;
  int? colorError;
  int? colorScrolledContainer;
}

class CkoBorderRadiusToken {
  int? all;
}

class CkoFont {
  String? fontStyle;
  String? fontWeight;
  String? fontFamily;
  int? letterSpacing;
  int? lineHeight;
}

class CheckoutInitParams {
  String sessionToken;
  String sessionId;
  String sessionSecret;
  String publicKey;
  CheckoutEnvironment? environment;
  CkoDesignToken? designToken;

  CheckoutInitParams({
    required this.sessionToken,
    required this.sessionId,
    required this.sessionSecret,
    required this.publicKey,
    this.environment = CheckoutEnvironment.sandbox,
    this.designToken,
  });
}

@HostApi()
abstract class MoniepointCheckoutPlugin {
  /// This is called by Flutter to start the checkout flow.
  void startCheckout(CheckoutInitParams session);
}

@FlutterApi()
abstract class CheckoutPlatformApi {
  /// Called when checkout succeeds. Returns a string (e.g. token or result ID).
  void onSuccess(String result);

  /// Called when checkout fails. Includes the error message.
  void onFailure(String error);
}