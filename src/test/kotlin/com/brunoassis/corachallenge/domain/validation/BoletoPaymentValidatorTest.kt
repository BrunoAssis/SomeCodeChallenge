package com.brunoassis.corachallenge.domain.validation

import com.brunoassis.corachallenge.entity.PaymentType
import com.brunoassis.corachallenge.helpers.Fixtures
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class BoletoPaymentValidatorTest {
    private val boletoPaymentValidator = BoletoPaymentValidator()
    private val valid = Fixtures.boletoChargeInformation

    @Nested
    inner class Validate {
        @Test
        fun `return successful Validation when Boleto is valid`() {
            assertEquals(Validation(true), boletoPaymentValidator.validate(valid))
        }

        @Test
        fun `return failed Validation when Boleto type is not Boleto`() {
            val invalid = valid.copy(type = PaymentType.CREDIT_CARD)
            assertEquals(
                Validation(false, listOf("Payment type must be BOLETO. Passed type is CREDIT_CARD")),
                boletoPaymentValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when amount is less or equal to zero`() {
            val invalid = valid.copy(amount = BigDecimal.ZERO)
            assertEquals(
                Validation(false, listOf("Payment amount must be bigger than zero.")),
                boletoPaymentValidator.validate(invalid)
            )
        }
    }
}