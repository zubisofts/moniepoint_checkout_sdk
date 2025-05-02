package com.moniepoint.moniepoint_checkout_sdk.lib

import CheckoutInitParams
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import io.flutter.plugin.platform.PlatformView
import kotlinx.coroutines.*

class CheckoutPlatformView(
    activity: FragmentActivity,
    params: CheckoutInitParams,
) : PlatformView {

    private val container = FrameLayout(activity).apply {
        id = View.generateViewId()
    }
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        val fragment = CheckoutFragment()
        fragment.setInitParams(params)

        activity.supportFragmentManager.beginTransaction()
            .replace(container.id, fragment)
            .commit()

    }

    override fun getView(): View = container

    override fun dispose() {
        scope.cancel()
    }
}
