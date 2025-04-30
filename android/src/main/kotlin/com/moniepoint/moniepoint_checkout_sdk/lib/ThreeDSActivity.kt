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
import com.checkout.threeds.AuthenticationCallback
import com.checkout.threeds.Checkout3DSService
import com.checkout.threeds.domain.model.AuthenticationCompleted
import com.checkout.threeds.domain.model.AuthenticationError
import com.checkout.threeds.domain.model.AuthenticationErrorType
import com.checkout.threeds.domain.model.AuthenticationParameters
import com.checkout.threeds.domain.model.AuthenticationResult
import com.checkout.threeds.domain.model.ResultType
import com.checkout.tokenization.model.TokenDetails
import java.util.Locale

class ThreeDSActivity : AppCompatActivity(), AuthenticationCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionId = intent.getStringExtra(EXTRA_SESSION_ID) ?: return finishWithError("Missing session id")
        val sessionSecret = intent.getStringExtra(EXTRA_SESSION_SECRET)?: return finishWithError("Missing session id")
        val authenticationParameters = AuthenticationParameters(
            sessionId = sessionId,
            sessionSecret = sessionSecret,
            scheme = "scheme")

        val checkout3DS = Checkout3DSService(
            this,
            com.checkout.threeds.Environment.PRODUCTION,
            Locale.UK,
        )

        checkout3DS.authenticate(authenticationParameters, this)
    }

    private fun finishWithError(message: String) {
        val errorIntent = Intent().apply {
            putExtra(EXTRA_ERROR, message)
        }
        setResult(RESULT_CANCELED, errorIntent)
        finish()
    }

    companion object {
        const val EXTRA_SESSION_ID = "extra_session_id"
        const val EXTRA_SESSION_SECRET = "extra_session_secret"
        const val EXTRA_TOKEN = "extra_token"
        const val EXTRA_ERROR = "extra_error"

        fun start(context: Activity, sessionId: String, sessionSecret: String) {
            val intent = Intent(context, CheckoutActivity::class.java).apply {
                putExtra(EXTRA_SESSION_ID, sessionId)
                putExtra(EXTRA_SESSION_SECRET, sessionSecret)
            }
            context.startActivityForResult(intent, REQUEST_CODE)
        }

        const val REQUEST_CODE = 4141
    }

    override fun onAuthenticationResult(authenticationResult: AuthenticationResult) {
        when (authenticationResult.resultType) {
            ResultType.Completed -> {
                // Handle authentication result - continue with the payment based on transaction status

                val authenticationCompleted: AuthenticationCompleted = (authenticationResult as AuthenticationCompleted)
                val sdkTransactionId = authenticationCompleted.sdkTransactionId
                val transactionStatus = authenticationCompleted.transactionStatus
            }

            ResultType.Error -> {
                // Handle error (result as AuthenticationError)

                // Handle error based on error type category
                val errorType: AuthenticationErrorType = (authenticationResult as AuthenticationError).errorType

                // Handle error based on fine grained error code or simply log the error
                val errorCode: String = (authenticationResult as AuthenticationError).errorCode
                finishWithError("$errorCode $errorType")
            }
        }

    }
}
