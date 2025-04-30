import Flutter
import UIKit

public class MoniepointCheckoutSdkPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
      let messenger = registrar.messenger()
      
      guard let controller = UIApplication.shared.delegate?.window??.rootViewController else {
            print("Failed to get rootViewController")
            return
          }
      
      let instance = MoniepointCheckoutHandler(controller: controller)
      MoniepointCheckoutSetup.setUp(binaryMessenger: messenger, api: instance)
  }
}
