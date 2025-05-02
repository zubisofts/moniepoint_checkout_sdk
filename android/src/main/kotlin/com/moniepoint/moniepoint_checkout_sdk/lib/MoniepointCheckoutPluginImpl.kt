package com.moniepoint.moniepoint_checkout_sdk.lib

import CheckoutInitParams
import MoniepointCheckoutPlugin

class MoniepointCheckoutPluginImpl: MoniepointCheckoutPlugin {

    override fun startCheckout(session: CheckoutInitParams) {
        CheckoutInitRegistry.params = session
        onInitCallback?.invoke(session)
    }

    companion object {
        var onInitCallback: ((CheckoutInitParams) -> Unit)? = null
    }
}

object CheckoutInitRegistry {
    var params: CheckoutInitParams? = null
}
