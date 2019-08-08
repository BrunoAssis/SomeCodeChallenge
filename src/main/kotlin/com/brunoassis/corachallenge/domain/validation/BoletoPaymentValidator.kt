package com.brunoassis.corachallenge.domain.validation

import com.brunoassis.corachallenge.entity.ChargeInformation
import com.brunoassis.corachallenge.entity.PaymentType
import java.math.BigDecimal

class BoletoPaymentValidator : Validator<ChargeInformation> {
    override fun validate(toBeValidated: ChargeInformation): Validation {
        val errors = arrayListOf<String>()

        validatePaymentTypeIsBoleto(toBeValidated.type, errors)
        validatePaymentAmount(toBeValidated.amount, errors)

        return Validation(successful = errors.isEmpty(), errors = errors)
    }

    private fun validatePaymentAmount(amount: BigDecimal, errors: ArrayList<String>) {
        if (amount <= BigDecimal.ZERO) {
            errors.add("Payment amount must be bigger than zero.")
        }
    }

    private fun validatePaymentTypeIsBoleto(paymentType: PaymentType, errors: ArrayList<String>) {
        if (paymentType != PaymentType.BOLETO) {
            errors.add("Payment type must be BOLETO. Passed type is $paymentType")
        }
    }
}
