package com.moniepoint.moniepoint_checkout_sdk

import CheckoutEnvironment
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
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
import com.checkout.threeds.AuthenticationCallback
import com.checkout.threeds.domain.model.AuthenticationCompleted
import com.checkout.threeds.domain.model.AuthenticationError
import com.checkout.threeds.domain.model.AuthenticationErrorType
import com.checkout.threeds.domain.model.AuthenticationResult
import com.checkout.threeds.domain.model.ResultType
import com.checkout.threedsecure.model.ThreeDSRequest
import com.checkout.threedsecure.model.ThreeDSResult
import com.checkout.threedsecure.model.ThreeDSResultHandler
import com.checkout.tokenization.model.TokenDetails
import com.moniepoint.moniepoint_checkout_sdk.lib.PaymentUtil
import com.moniepoint.moniepoint_checkout_sdk.lib.PaymentUtil.*

class CheckoutActivity : AppCompatActivity(), AuthenticationCallback {
    var paymentFormMediator: PaymentFormMediator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        val publicKey =
            intent.getStringExtra(EXTRA_PUBLIC_KEY) ?: return finishWithError("Missing public key")
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
                purchase(tokenDetails.token)
                //finish()
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
        paymentFormMediator = PaymentFormMediator(paymentFormConfig);
        paymentFormMediator?.setActivityContent(this)

    }

    private fun purchase(token: String) {
        createPayment(token) { success, redirectUrl ->

            if (redirectUrl != null) {
                start3DS(redirectUrl)
            } else {
                println(" no redirect url avaulable")
            }
        }
    }

    private fun start3DS(redirectUrl: String) {
        val threeDSRequest = ThreeDSRequest(
            this.findViewById(R.id.frameMe),
            redirectUrl,
            "https://authentication-devices.sandbox.checkout.com/sessions-interceptor/sid_7ul6fdirserevaifrdsir3cexu",
            "https://authentication-devices.sandbox.checkout.com/sessions-interceptor/sid_7ul6fdirserevaifrdsir3cexu",
            threeDSResultHandler
        )

        paymentFormMediator?.handleThreeDS(threeDSRequest)
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

    override fun onAuthenticationResult(authenticationResult: AuthenticationResult) {
        when (authenticationResult.resultType) {
            ResultType.Completed -> {
                // Handle authentication result - continue with the payment based on transaction status

                val authenticationCompleted: AuthenticationCompleted =
                    (authenticationResult as AuthenticationCompleted)
                val sdkTransactionId = authenticationCompleted.sdkTransactionId
                val transactionStatus = authenticationCompleted.transactionStatus
                println("Result: Authentication success: $sdkTransactionId")
            }

            ResultType.Error -> {
                // Handle error (result as AuthenticationError)

                // Handle error based on error type category
                val errorType: AuthenticationErrorType =
                    (authenticationResult as AuthenticationError).errorType

                // Handle error based on fine grained error code or simply log the error
                val errorCode: String = (authenticationResult as AuthenticationError).errorCode
                println("Result: Authentication error: $errorCode $errorType")
            }
        }

    }

    private val threeDSResultHandler: ThreeDSResultHandler = { threeDSResult: ThreeDSResult ->
        when (threeDSResult) {
            is ThreeDSResult.Success -> {
                /* Handle success result */
                val token: String = (threeDSResult as ThreeDSResult.Success).token
                //displayMessage("Result", "Authentication success: $token", true)
                println("Result: Authentication success: $token")
                threeDSResult.token
            }

            is ThreeDSResult.Failure -> {
                val errorMessage: String = (threeDSResult as Error).message.toString()
                println("Result Authentication error:  + $errorMessage, true")
                /* Handle failure result */
            }

            is ThreeDSResult.Error -> {
                /* Handle error result */
                println("Result Authentication Failure");
                threeDSResult.error
            }
        }

    }
}
