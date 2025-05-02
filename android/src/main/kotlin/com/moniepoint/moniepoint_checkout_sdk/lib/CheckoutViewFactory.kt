package com.moniepoint.moniepoint_checkout_sdk.lib

import CheckoutInitParams
import android.content.Context
import androidx.fragment.app.FragmentActivity
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class CheckoutPlatformViewFactory(
    private val activity: FragmentActivity
) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {

    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val params = CheckoutInitRegistry.params
            ?: throw IllegalStateException("CheckoutInitParams not initialized. Call init() first.")
        return CheckoutPlatformView(activity, params)
    }
}
