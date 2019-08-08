package com.brunoassis.corachallenge.domain.service.paymentprocessor

import com.brunoassis.corachallenge.domain.repository.PaymentRepository
import com.brunoassis.corachallenge.domain.validation.BuyerValidator
import com.brunoassis.corachallenge.domain.validation.CreditCardPaymentValidator
import com.brunoassis.corachallenge.domain.validation.Validation
import com.brunoassis.corachallenge.entity.PaymentResponse
import com.brunoassis.corachallenge.helpers.Fixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CreditCardPaymentProcessorTest {
    private val creditCardPaymentValidator = mockk<CreditCardPaymentValidator>()
    private val buyerValidator = mockk<BuyerValidator>()
    private val paymentRepository = mockk<PaymentRepository>()

    private val creditCardPaymentProcessor = CreditCardPaymentProcessor(
        creditCardPaymentValidator = creditCardPaymentValidator,
        buyerValidator = buyerValidator,
        paymentRepository = paymentRepository
    )

    @Nested
    inner class ProcessPayment {
        @Test
        fun `return successful PaymentResponse when everything is valid`() {
            every { buyerValidator.validate(any()) } returns Validation(true)
            every { creditCardPaymentValidator.validate(any()) } returns Validation(true)
            every { paymentRepository.insert(any()) } returns Fixtures.payment()

            val expectedPaymentResponse = PaymentResponse(
                successful = true,
                payment = Fixtures.payment()
            )
            val actualPaymentResponse = creditCardPaymentProcessor.processPayment(Fixtures.creditCardPaymentInformation)
            assertEquals(expectedPaymentResponse, actualPaymentResponse)
        }

        @Test
        fun `return failed PaymentResponse when Buyer is invalid`() {
            every { buyerValidator.validate(any()) } returns Validation(false, listOf("Invalid Buyer"))

            val expectedPaymentResponse = PaymentResponse(
                successful = false,
                errorMessage = "Invalid Buyer"
            )
            val actualPaymentResponse = creditCardPaymentProcessor.processPayment(Fixtures.creditCardPaymentInformation)
            assertEquals(expectedPaymentResponse, actualPaymentResponse)
        }

        @Test
        fun `return failed PaymentResponse when Credit Card is invalid`() {
            every { buyerValidator.validate(any()) } returns Validation(true)
            every { creditCardPaymentValidator.validate(any()) } returns Validation(false, listOf("Invalid Credit Card"))

            val expectedPaymentResponse = PaymentResponse(
                successful = false,
                errorMessage = "Invalid Credit Card"
            )
            val actualPaymentResponse = creditCardPaymentProcessor.processPayment(Fixtures.creditCardPaymentInformation)
            assertEquals(expectedPaymentResponse, actualPaymentResponse)
        }
    }
}