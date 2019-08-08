package com.brunoassis.corachallenge.domain.validation

import com.brunoassis.corachallenge.entity.PaymentType
import com.brunoassis.corachallenge.helpers.Fixtures
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

internal class CreditCardPaymentValidatorTest {
    private val creditCardPaymentValidator = CreditCardPaymentValidator()
    private val valid = Fixtures.creditCardPaymentInformation

    @Nested
    inner class Validate {
        @Test
        fun `return successful Validation when Credit Card is valid`() {
            assertEquals(Validation(true), creditCardPaymentValidator.validate(valid))
        }

        @Test
        fun `return failed Validation when Credit Card type is not Credit Card`() {
            val invalid = valid.copy(
                chargeInformation = Fixtures.creditCardChargeInformation.copy(
                    type = PaymentType.BOLETO
                )
            )
            assertEquals(
                Validation(false, listOf("Payment type must be CREDIT_CARD. Passed type is BOLETO")),
                creditCardPaymentValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when amount is less or equal to zero`() {
            val invalid = valid.copy(
                chargeInformation = Fixtures.creditCardChargeInformation.copy(
                    amount = BigDecimal.ZERO
                )
            )
            assertEquals(
                Validation(false, listOf("Payment amount must be bigger than zero.")),
                creditCardPaymentValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when Credit Card is null`() {
            val invalid = valid.copy(
                chargeInformation = Fixtures.creditCardChargeInformation.copy(
                    creditCard = null
                )
            )
            assertEquals(
                Validation(false, listOf("Credit Card must be provided.")),
                creditCardPaymentValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when Credit Card holder name does not match with Buyer name`() {
            val invalid = valid.copy(
                chargeInformation = Fixtures.creditCardChargeInformation.copy(
                    creditCard = Fixtures.creditCard.copy(
                        holderName = "Other name"
                    )
                )
            )
            assertEquals(
                Validation(false, listOf("Credit Card holder name must match buyer's name.")),
                creditCardPaymentValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when Credit Card number is smaller than 16`() {
            val invalid = valid.copy(
                chargeInformation = Fixtures.creditCardChargeInformation.copy(
                    creditCard = Fixtures.creditCard.copy(
                        number = "123"
                    )
                )
            )
            assertEquals(
                Validation(false, listOf("Credit Card's number must have exactly 16 characters.")),
                creditCardPaymentValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when Credit Card number is bigger than 16`() {
            val invalid = valid.copy(
                chargeInformation = Fixtures.creditCardChargeInformation.copy(
                    creditCard = Fixtures.creditCard.copy(
                        number = "12345678901234567890"
                    )
                )
            )
            assertEquals(
                Validation(false, listOf("Credit Card's number must have exactly 16 characters.")),
                creditCardPaymentValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when Credit Card CVV is smaller than 3`() {
            val invalid = valid.copy(
                chargeInformation = Fixtures.creditCardChargeInformation.copy(
                    creditCard = Fixtures.creditCard.copy(
                        cvv = "12"
                    )
                )
            )
            assertEquals(
                Validation(false, listOf("Credit Card's CVV must have exactly 3 characters.")),
                creditCardPaymentValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when Credit Card CVV is bigger than 3`() {
            val invalid = valid.copy(
                chargeInformation = Fixtures.creditCardChargeInformation.copy(
                    creditCard = Fixtures.creditCard.copy(
                        cvv = "1234"
                    )
                )
            )
            assertEquals(
                Validation(false, listOf("Credit Card's CVV must have exactly 3 characters.")),
                creditCardPaymentValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when Credit Card expiration date is in the past`() {
            val expirationDateInThePast = LocalDate.now().minusYears(1)
            val invalid = valid.copy(
                chargeInformation = Fixtures.creditCardChargeInformation.copy(
                    creditCard = Fixtures.creditCard.copy(
                        expirationDate = expirationDateInThePast
                    )
                )
            )
            assertEquals(
                Validation(false, listOf("Credit card is expired. Expiration date: [$expirationDateInThePast]")),
                creditCardPaymentValidator.validate(invalid)
            )
        }
    }
}