package com.brunoassis.corachallenge.infrastructure.webserver.controller

import com.brunoassis.corachallenge.domain.service.PaymentService
import com.brunoassis.corachallenge.entity.CreatePaymentRequest
import com.brunoassis.corachallenge.entity.error.ErrorResponse
import io.javalin.http.Context
import io.javalin.http.Handler
import org.eclipse.jetty.http.HttpStatus

class CreatePaymentController(private val paymentService: PaymentService) : Handler {
    override fun handle(context: Context) {
        val createPaymentRequest = context.bodyAsClass(CreatePaymentRequest::class.java)
        val paymentServiceResponse = paymentService.create(createPaymentRequest)

        val responseStatus: Int
        val responseBody: Any?

        if (paymentServiceResponse.successful) {
            responseStatus = HttpStatus.CREATED_201
            responseBody = paymentServiceResponse.payment
        } else {
            responseStatus = HttpStatus.BAD_REQUEST_400
            responseBody = paymentServiceResponse.errorMessage?.let { ErrorResponse(it) }
        }

        context.status(responseStatus)
        if (responseBody != null) {
            context.json(responseBody)
        }
    }
}