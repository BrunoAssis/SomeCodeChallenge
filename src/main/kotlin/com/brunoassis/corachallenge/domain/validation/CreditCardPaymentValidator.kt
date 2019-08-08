package com.brunoassis.corachallenge.domain.validation

import com.brunoassis.corachallenge.entity.Buyer
import com.brunoassis.corachallenge.entity.CreditCard
import com.brunoassis.corachallenge.entity.PaymentInformation
import com.brunoassis.corachallenge.entity.PaymentType
import java.math.BigDecimal
import java.time.LocalDate

class CreditCardPaymentValidator : Validator<PaymentInformation> {
    companion object {
        private const val NUMBER_LENGTH = 16
        private const val CVV_LENGTH = 3
    }

    /*
    In a real-word use-case this validator would contain much more scenarios,
    such as validating a Credit Card's "flag" according to its number.
    */
    override fun validate(toBeValidated: PaymentInformation): Validation {
        val errors = arrayListOf<String>()

        val chargeInformation = toBeValidated.chargeInformation
        validatePaymentTypeIsCreditCard(chargeInformation.type, errors)
        validatePaymentAmount(chargeInformation.amount, errors)

        val buyer = toBeValidated.buyer
        validateCreditCardInformation(chargeInformation.creditCard, buyer, errors)

        return Validation(successful = errors.isEmpty(), errors = errors)
    }

    private fun validateCreditCardInformation(
        creditCard: CreditCard?,
        buyer: Buyer,
        errors: ArrayList<String>
    ) {
        if (creditCard == null) {
            errors.add("Credit Card must be provided.")
        } else {
            validateCreditCardAndBuyerNameMatch(creditCard.holderName, buyer.name, errors)
            validateNumber(creditCard.number, errors)
            validateCVV(creditCard.cvv, errors)
            validateExpirationDate(creditCard.expirationDate, errors)
        }
    }

    private fun validateCreditCardAndBuyerNameMatch(
        holderName: String,
        buyerName: String,
        errors: ArrayList<String>
    ) {
        if (holderName != buyerName) {
            errors.add("Credit Card holder name must match buyer's name.")
        }
    }

    private fun validateNumber(creditCardNumber: String, errors: ArrayList<String>) {
        if (creditCardNumber.length != NUMBER_LENGTH) {
            errors.add("Credit Card's number must have exactly $NUMBER_LENGTH characters.")
        }
    }

    private fun validateCVV(cvv: String, errors: ArrayList<String>) {
        if (cvv.length != CVV_LENGTH) {
            errors.add("Credit Card's CVV must have exactly $CVV_LENGTH characters.")
        }
    }

    private fun validateExpirationDate(expirationDate: LocalDate, errors: java.util.ArrayList<String>) {
        if (expirationDate.isBefore(LocalDate.now())) {
            errors.add("Credit card is expired. Expiration date: [$expirationDate]")
        }
    }

    private fun validatePaymentAmount(amount: BigDecimal, errors: ArrayList<String>) {
        if (amount <= BigDecimal.ZERO) {
            errors.add("Payment amount must be bigger than zero.")
        }
    }

    private fun validatePaymentTypeIsCreditCard(paymentType: PaymentType, errors: ArrayList<String>) {
        if (paymentType != PaymentType.CREDIT_CARD) {
            errors.add("Payment type must be CREDIT_CARD. Passed type is $paymentType")
        }
    }
}
