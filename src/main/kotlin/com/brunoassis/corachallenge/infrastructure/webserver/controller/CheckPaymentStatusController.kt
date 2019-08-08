package com.brunoassis.corachallenge.infrastructure.webserver.controller

import com.brunoassis.corachallenge.domain.service.PaymentService
import com.brunoassis.corachallenge.entity.error.ErrorResponse
import io.javalin.http.Context
import io.javalin.http.Handler
import org.eclipse.jetty.http.HttpStatus

class CheckPaymentStatusController(private val paymentService: PaymentService) : Handler {
    override fun handle(context: Context) {
        val paymentId = context.pathParam("payment-id")

        val responseStatus: Int
        val responseBody: Any?

        if (paymentId.isBlank()) {
            responseStatus = HttpStatus.BAD_REQUEST_400
            responseBody = ErrorResponse("PaymentId cannot be blank.")
        } else {
            val paymentIdLong = paymentId.toLong()
            if (paymentIdLong <= 0) {
                responseStatus = HttpStatus.BAD_REQUEST_400
                responseBody = ErrorResponse("PaymentId must be greater than zero.")
            } else {
                val paymentServiceResponse = paymentService.findById(paymentIdLong)

                if (paymentServiceResponse.successful) {
                    responseStatus = HttpStatus.OK_200
                    responseBody = paymentServiceResponse.payment
                } else {
                    responseStatus = HttpStatus.BAD_REQUEST_400
                    responseBody = paymentServiceResponse.errorMessage?.let { ErrorResponse(it) }
                }
            }
        }

        context.status(responseStatus)
        if (responseBody != null) {
            context.json(responseBody)
        }
    }
}
