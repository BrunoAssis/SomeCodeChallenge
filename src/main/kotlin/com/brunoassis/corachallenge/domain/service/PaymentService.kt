package com.brunoassis.corachallenge.domain.service

import com.brunoassis.corachallenge.domain.repository.PaymentRepository
import com.brunoassis.corachallenge.domain.service.paymentprocessor.PaymentProcessor
import com.brunoassis.corachallenge.domain.validation.ClientValidator
import com.brunoassis.corachallenge.entity.CreatePaymentRequest
import com.brunoassis.corachallenge.entity.PaymentResponse
import com.brunoassis.corachallenge.entity.PaymentType
import java.security.InvalidParameterException

class PaymentService(
    private val clientValidator: ClientValidator,
    private val paymentProcessors: HashMap<PaymentType, PaymentProcessor>,
    private val paymentRepository: PaymentRepository
) {
    fun findById(paymentId: Long): PaymentResponse {
        val payment = paymentRepository.findById(paymentId)
        return if (payment != null) {
            PaymentResponse(successful = true, payment = payment)
        } else {
            PaymentResponse(successful = false, errorMessage = "Payment $paymentId not found.")
        }
    }

    fun create(createPaymentRequest: CreatePaymentRequest): PaymentResponse {
        val client = createPaymentRequest.client
        val clientValidation = clientValidator.validate(client)

        return if (clientValidation.successful) {
            val paymentInformation = createPaymentRequest.paymentInformation
            val paymentType = paymentInformation.chargeInformation.type
            val paymentProcessor: PaymentProcessor = choosePaymentProcessor(paymentType)

            paymentProcessor.processPayment(paymentInformation)
        } else {
            PaymentResponse(successful = false, errorMessage = clientValidation.errorMessage())
        }
    }

    private fun choosePaymentProcessor(paymentType: PaymentType): PaymentProcessor {
        val paymentProcessor = paymentProcessors[paymentType]

        if (paymentProcessor != null) {
            return paymentProcessor
        } else {
            throw InvalidParameterException("Payment processor for paymentType $paymentType doesn't exist.")
        }
    }
}