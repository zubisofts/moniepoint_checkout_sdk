package com.moniepoint.moniepoint_checkout_sdk

import android.app.Activity
import android.content.Intent
import androidx.annotation.NonNull
import com.moniepoint.moniepoint_checkout_sdk.lib.MoniepointCheckoutImpl

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** MoniepointCheckoutSdkPlugin */
class MoniepointCheckoutSdkPlugin: FlutterPlugin, ActivityAware  {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel

  private var activity: Activity? = null
  private var binaryMessenger: BinaryMessenger? = null

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    binaryMessenger = flutterPluginBinding.binaryMessenger
    MoniepointCheckout.setUp(flutterPluginBinding.binaryMessenger, null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity

    val checkoutImpl = MoniepointCheckoutImpl(
      binding.activity
    )
    if (binaryMessenger != null) {
      MoniepointCheckout.setUp(
        binaryMessenger!!,
        checkoutImpl,
      )
    }

    // Register activity result listener
    binding.addActivityResultListener(checkoutImpl)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivity() {
    activity = null
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
