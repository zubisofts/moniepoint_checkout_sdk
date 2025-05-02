package com.moniepoint.moniepoint_checkout_sdk

import android.app.Activity
import android.content.Intent
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import com.moniepoint.moniepoint_checkout_sdk.lib.CheckoutPlatformViewFactory
import com.moniepoint.moniepoint_checkout_sdk.lib.MoniepointCheckoutPluginImpl

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** MoniepointCheckoutSdkPlugin */
class MoniepointCheckoutSdkPlugin: FlutterPlugin, ActivityAware   {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity

  private var activity: Activity? = null
  private var binaryMessenger: BinaryMessenger? = null
  private var pluginBinding: FlutterPlugin.FlutterPluginBinding? = null

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    binaryMessenger = flutterPluginBinding.binaryMessenger
    pluginBinding = flutterPluginBinding

    MoniepointCheckoutPlugin.setUp(binaryMessenger!!, MoniepointCheckoutPluginImpl())
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    binaryMessenger = null
    pluginBinding = null
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity

    if (activity is FragmentActivity ) {
      pluginBinding!!.platformViewRegistry.registerViewFactory(
        "moniepoint_checkout_view",
        CheckoutPlatformViewFactory(activity as FragmentActivity)
      )
    } else {
      throw IllegalStateException("Activity must be FragmentActivity")
    }
  }

  override fun onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity()
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    onAttachedToActivity(binding)
  }

  override fun onDetachedFromActivity() {
    activity = null
  }

}
