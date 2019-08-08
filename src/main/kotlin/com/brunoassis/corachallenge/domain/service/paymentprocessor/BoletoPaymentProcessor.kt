package com.brunoassis.corachallenge.domain.service.paymentprocessor

import com.brunoassis.corachallenge.domain.repository.PaymentRepository
import com.brunoassis.corachallenge.domain.service.BoletoGenerator
import com.brunoassis.corachallenge.domain.validation.BoletoPaymentValidator
import com.brunoassis.corachallenge.domain.validation.BuyerValidator
import com.brunoassis.corachallenge.entity.Buyer
import com.brunoassis.corachallenge.entity.ChargeInformation
import com.brunoassis.corachallenge.entity.Payment
import com.brunoassis.corachallenge.entity.PaymentInformation
import com.brunoassis.corachallenge.entity.PaymentResponse
import com.brunoassis.corachallenge.entity.PaymentType

class BoletoPaymentProcessor(
    private val boletoGenerator: BoletoGenerator,
    private val boletoPaymentValidator: BoletoPaymentValidator,
    private val buyerValidator: BuyerValidator,
    private val paymentRepository: PaymentRepository
) : PaymentProcessor {

    override fun processPayment(paymentInformation: PaymentInformation): PaymentResponse {
        val buyerValidation = buyerValidator.validate(paymentInformation.buyer)
        if (!buyerValidation.successful) {
            return PaymentResponse(successful = false, errorMessage = buyerValidation.errorMessage())
        }

        val boletoValidation = boletoPaymentValidator.validate(paymentInformation.chargeInformation)
        if (!boletoValidation.successful) {
            return PaymentResponse(successful = false, errorMessage = boletoValidation.errorMessage())
        }

        val payment = createBoletoPayment(paymentInformation)
        return PaymentResponse(
            successful = true,
            payment = payment
        )
    }

    private fun createBoletoPayment(paymentInformation: PaymentInformation): Payment {
        val buyer = paymentInformation.buyer
        val chargeInformation = paymentInformation.chargeInformation
        val paymentInformationWithBoleto = buildPaymentInformationWithNewBoleto(
            buyer = buyer,
            chargeInformation = chargeInformation
        )

        return paymentRepository.insert(paymentInformationWithBoleto)
    }

    private fun buildPaymentInformationWithNewBoleto(
        buyer: Buyer,
        chargeInformation: ChargeInformation
    ): PaymentInformation {
        return PaymentInformation(
            buyer = buyer,
            chargeInformation = ChargeInformation(
                type = PaymentType.BOLETO,
                amount = chargeInformation.amount,
                boleto = boletoGenerator.generate()
            )
        )
    }
}