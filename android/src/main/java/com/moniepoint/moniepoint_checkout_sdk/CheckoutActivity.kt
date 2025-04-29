package com.moniepoint.moniepoint_checkout_sdk

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.checkout.base.model.Environment
import com.checkout.frames.api.PaymentFlowHandler
import com.checkout.frames.api.PaymentFormMediator
import com.checkout.frames.screen.paymentform.model.PaymentFormConfig
import com.checkout.frames.style.screen.PaymentFormStyle
import com.checkout.tokenization.model.TokenDetails

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val paymentFlowHandler = object : PaymentFlowHandler {
            override fun onSubmit() {
                // form submit initiated; you can choose to display a loader here
                // log the event
                println("onSubmit: Form submitted")
            }

            override fun onSuccess(tokenDetails: TokenDetails) {
                // your token is here
                println("onSuccess: ${tokenDetails.token}")
            }

            override fun onFailure(errorMessage: String) {
                // token request error
                println("onFailure: $errorMessage")
            }

            override fun onBackPressed() {
                // the user decided to leave the payment page
                println("onBackPressed")
            }
        }
        val paymentFormConfig = PaymentFormConfig(
            publicKey = "pk_sbox_pmfui27bywspolwlmqej5b6gvmr",                     // set your public key
            context = this,                          // set context
            environment = Environment.SANDBOX,          // set the environment
            paymentFlowHandler = paymentFlowHandler,    // set the callback
            style = PaymentFormStyle(),                 // set the style
            supportedCardSchemeList = emptyList()       // set supported card schemes, by default uses all schemes
        )
        val paymentFormMediator = PaymentFormMediator(paymentFormConfig)
        paymentFormMediator.setActivityContent(this);
    }
}