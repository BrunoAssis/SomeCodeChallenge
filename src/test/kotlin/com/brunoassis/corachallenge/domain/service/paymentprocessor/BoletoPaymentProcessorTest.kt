package com.brunoassis.corachallenge.domain.service.paymentprocessor

import com.brunoassis.corachallenge.domain.repository.PaymentRepository
import com.brunoassis.corachallenge.domain.service.BoletoGenerator
import com.brunoassis.corachallenge.domain.validation.BoletoPaymentValidator
import com.brunoassis.corachallenge.domain.validation.BuyerValidator
import com.brunoassis.corachallenge.domain.validation.Validation
import com.brunoassis.corachallenge.entity.PaymentResponse
import com.brunoassis.corachallenge.helpers.Fixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BoletoPaymentProcessorTest {
    private val boletoGenerator = BoletoGenerator() // In a real use-case this would be mocked as well
    private val boletoPaymentValidator = mockk<BoletoPaymentValidator>()
    private val buyerValidator = mockk<BuyerValidator>()
    private val paymentRepository = mockk<PaymentRepository>()

    private val boletoPaymentProcessor = BoletoPaymentProcessor(
        boletoGenerator = boletoGenerator,
        boletoPaymentValidator = boletoPaymentValidator,
        buyerValidator = buyerValidator,
        paymentRepository = paymentRepository
    )

    @Nested
    inner class ProcessPayment {
        @Test
        fun `return successful PaymentResponse when everything is valid`() {
            every { buyerValidator.validate(any()) } returns Validation(true)
            every { boletoPaymentValidator.validate(any()) } returns Validation(true)
            every { paymentRepository.insert(any()) } returns Fixtures.payment()

            val expectedPaymentResponse = PaymentResponse(
                successful = true,
                payment = Fixtures.payment()
            )
            val actualPaymentResponse = boletoPaymentProcessor.processPayment(Fixtures.boletoPaymentInformation)
            assertEquals(expectedPaymentResponse, actualPaymentResponse)
        }

        @Test
        fun `return failed PaymentResponse when Buyer is invalid`() {
            every { buyerValidator.validate(any()) } returns Validation(false, listOf("Invalid Buyer"))

            val expectedPaymentResponse = PaymentResponse(
                successful = false,
                errorMessage = "Invalid Buyer"
            )
            val actualPaymentResponse = boletoPaymentProcessor.processPayment(Fixtures.boletoPaymentInformation)
            assertEquals(expectedPaymentResponse, actualPaymentResponse)
        }

        @Test
        fun `return failed PaymentResponse when Boleto is invalid`() {
            every { buyerValidator.validate(any()) } returns Validation(true)
            every { boletoPaymentValidator.validate(any()) } returns Validation(false, listOf("Invalid Boleto"))

            val expectedPaymentResponse = PaymentResponse(
                successful = false,
                errorMessage = "Invalid Boleto"
            )
            val actualPaymentResponse = boletoPaymentProcessor.processPayment(Fixtures.boletoPaymentInformation)
            assertEquals(expectedPaymentResponse, actualPaymentResponse)
        }
    }
}