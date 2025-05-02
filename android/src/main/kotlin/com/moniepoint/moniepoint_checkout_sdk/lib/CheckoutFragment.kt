package com.moniepoint.moniepoint_checkout_sdk.lib

import CheckoutInitParams
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.checkout.components.core.CheckoutComponentsFactory
import com.checkout.components.interfaces.Environment
import com.checkout.components.interfaces.component.CheckoutComponentConfiguration
import com.checkout.components.interfaces.component.ComponentCallback
import com.checkout.components.interfaces.error.CheckoutError
import com.checkout.components.interfaces.model.PaymentMethodName
import com.checkout.components.interfaces.model.PaymentSessionResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckoutFragment : Fragment() {

    private var params: CheckoutInitParams? = null

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun setInitParams(params: CheckoutInitParams) {
        this.params = params
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // This is the view that will hold the Checkout UI
        return FrameLayout(requireContext()).apply {
            id = View.generateViewId() // needed to identify this container
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view as FrameLayout
        val initParams = params ?: return

        val configuration = CheckoutComponentConfiguration(
            context = requireContext(),
            paymentSession = PaymentSessionResponse(
                id = initParams.sessionId,
                secret = initParams.sessionSecret
            ),
            publicKey = initParams.publicKey,
            environment = when (initParams.environment?.raw) {
                1 -> Environment.PRODUCTION
                else -> Environment.SANDBOX
            },
            componentCallback = ComponentCallback(
                onSuccess = { _, _ -> println("✅ onSuccess") },
                onReady = { _ -> println("✅ onReady") },
                onSubmit = { _ -> println("✅ onSubmit") },
                onError = { _, error -> println("❌ onError: ${error.message}") }
            ),
            appearance = initParams.designToken?.toDesignTokens()
        )

        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            try {
                val components = CheckoutComponentsFactory(config = configuration).create()
                val cardComponent = components.create(PaymentMethodName.Card)
                if (cardComponent.isAvailable()) {
                    val cardView = cardComponent.provideView(container)
                    withContext(Dispatchers.Main) {
                        container.addView(cardView)
                    }
                }
            } catch (e: CheckoutError) {
                withContext(Dispatchers.Main) {
                    println("❌ CheckoutError: ${e.message}")
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }
}