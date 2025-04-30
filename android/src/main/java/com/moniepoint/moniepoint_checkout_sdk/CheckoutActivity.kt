package com.moniepoint.moniepoint_checkout_sdk

import CheckoutEnvironment
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.checkout.base.model.CardScheme
import com.checkout.base.model.Environment
import com.checkout.frames.api.PaymentFlowHandler
import com.checkout.frames.api.PaymentFormMediator
import com.checkout.frames.model.CornerRadius
import com.checkout.frames.model.Margin
import com.checkout.frames.screen.paymentform.model.PaymentFormConfig
import com.checkout.frames.style.component.base.ContainerStyle
import com.checkout.frames.style.screen.PaymentDetailsStyle
import com.checkout.frames.style.screen.PaymentFormStyle
import com.checkout.tokenization.model.TokenDetails

 class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val publicKey = intent.getStringExtra(EXTRA_PUBLIC_KEY) ?: return finishWithError("Missing public key")
        val envRaw = intent.getIntExtra(EXTRA_ENVIRONMENT, CheckoutEnvironment.SANDBOX.raw)
        val environment = when (CheckoutEnvironment.ofRaw(envRaw)) {
            CheckoutEnvironment.PRODUCTION -> Environment.PRODUCTION
            else -> Environment.SANDBOX
        }

        val paymentFlowHandler = object : PaymentFlowHandler {
            override fun onSubmit() {
                println("onSubmit: Form submitted")
            }

            override fun onSuccess(tokenDetails: TokenDetails) {
                println("onSuccess: ${tokenDetails.token}")
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_TOKEN, tokenDetails.token)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            override fun onFailure(errorMessage: String) {
                println("onFailure: $errorMessage")
                finishWithError(errorMessage)
            }

            override fun onBackPressed() {
                println("onBackPressed")
                finishWithError("User cancelled")
            }
        }

        val paymentFormConfig = PaymentFormConfig(
            publicKey = publicKey,
            context = this,
            environment = environment,
            paymentFlowHandler = paymentFlowHandler,
            style = PaymentFormStyle(),
            supportedCardSchemeList = listOf(CardScheme.VISA, CardScheme.MASTERCARD),
        )

        PaymentFormMediator(paymentFormConfig).setActivityContent(this)
    }

    private fun finishWithError(message: String) {
        val errorIntent = Intent().apply {
            putExtra(EXTRA_ERROR, message)
        }
        setResult(RESULT_CANCELED, errorIntent)
        finish()
    }

    companion object {
        const val EXTRA_PUBLIC_KEY = "extra_public_key"
        const val EXTRA_ENVIRONMENT = "extra_environment"
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_ERROR = "extra_error"

        fun start(context: Activity, publicKey: String, environment: CheckoutEnvironment) {
            val intent = Intent(context, CheckoutActivity::class.java).apply {
                putExtra(EXTRA_PUBLIC_KEY, publicKey)
                putExtra(EXTRA_ENVIRONMENT, environment.raw)
            }
            context.startActivityForResult(intent, REQUEST_CODE)
        }

        const val REQUEST_CODE = 4242
    }
}
