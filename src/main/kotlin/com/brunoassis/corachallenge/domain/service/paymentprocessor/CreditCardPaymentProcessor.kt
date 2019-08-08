package com.brunoassis.corachallenge.domain.service.paymentprocessor

import com.brunoassis.corachallenge.domain.repository.PaymentRepository
import com.brunoassis.corachallenge.domain.validation.BuyerValidator
import com.brunoassis.corachallenge.domain.validation.CreditCardPaymentValidator
import com.brunoassis.corachallenge.entity.Payment
import com.brunoassis.corachallenge.entity.PaymentInformation
import com.brunoassis.corachallenge.entity.PaymentResponse

class CreditCardPaymentProcessor(
    private val creditCardPaymentValidator: CreditCardPaymentValidator,
    private val buyerValidator: BuyerValidator,
    private val paymentRepository: PaymentRepository
) : PaymentProcessor {

    override fun processPayment(paymentInformation: PaymentInformation): PaymentResponse {
        val buyerValidation = buyerValidator.validate(paymentInformation.buyer)
        if (!buyerValidation.successful) {
            return PaymentResponse(successful = false, errorMessage = buyerValidation.errorMessage())
        }

        val creditCardValidation = creditCardPaymentValidator.validate(paymentInformation)
        if (!creditCardValidation.successful) {
            return PaymentResponse(successful = false, errorMessage = creditCardValidation.errorMessage())
        }

        val payment = createCreditCardPayment(paymentInformation)
        return PaymentResponse(
            successful = true,
            payment = payment
        )
    }

    private fun createCreditCardPayment(paymentInformation: PaymentInformation): Payment {
        return paymentRepository.insert(paymentInformation)
    }
}