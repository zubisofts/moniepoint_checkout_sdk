package com.moniepoint.moniepoint_checkout_sdk.lib

import CheckoutEnvironment
import MoniepointCheckout
import android.app.Activity
import android.content.Intent
import com.moniepoint.moniepoint_checkout_sdk.CheckoutActivity
import com.moniepoint.moniepoint_checkout_sdk.CheckoutActivity.Companion.EXTRA_ERROR
import com.moniepoint.moniepoint_checkout_sdk.CheckoutActivity.Companion.EXTRA_TOKEN
import io.flutter.plugin.common.PluginRegistry

class MoniepointCheckoutImpl(
    private val activity: Activity,
) : MoniepointCheckout, PluginRegistry.ActivityResultListener {

    var pendingResult: ((Result<String>) -> Unit)? = null

    private lateinit var publicKey: String
    private lateinit var environment: CheckoutEnvironment

    override fun initialize(publicKey: String, environment: CheckoutEnvironment) {
        this.publicKey = publicKey
        this.environment = environment
    }

    override fun tokenize(callback: (Result<String>) -> Unit) {
        pendingResult = callback
        CheckoutActivity.start(activity, publicKey, environment)

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): Boolean {
        if (requestCode != CheckoutActivity.REQUEST_CODE) return false

        val token = data?.getStringExtra(EXTRA_TOKEN)
        val error = data?.getStringExtra(EXTRA_ERROR)

        when {
            token != null -> pendingResult?.invoke(Result.success(token))
            error != null -> pendingResult?.invoke(Result.failure(Exception(error)))
            else -> pendingResult?.invoke(Result.failure(Exception("Unknown error occurred")))
        }

        pendingResult = null
        return true
    }

}