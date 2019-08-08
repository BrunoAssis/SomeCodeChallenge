package com.brunoassis.corachallenge.domain.service

import com.brunoassis.corachallenge.domain.repository.PaymentRepository
import com.brunoassis.corachallenge.domain.service.paymentprocessor.PaymentProcessor
import com.brunoassis.corachallenge.domain.validation.ClientValidator
import com.brunoassis.corachallenge.domain.validation.Validation
import com.brunoassis.corachallenge.entity.Client
import com.brunoassis.corachallenge.entity.CreatePaymentRequest
import com.brunoassis.corachallenge.entity.PaymentResponse
import com.brunoassis.corachallenge.entity.PaymentType
import com.brunoassis.corachallenge.helpers.Fixtures
import com.brunoassis.corachallenge.infrastructure.dao.PaymentDao
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class PaymentServiceTest {
    private val client = Client(1)

    private val clientValidator = mockk<ClientValidator>()
    private val paymentProcessors = mockk<HashMap<PaymentType, PaymentProcessor>>()
    private val paymentDao =
        PaymentDao() // This would be mocked in a real-word scenario.
    private val paymentRepository =
        PaymentRepository(paymentDao = paymentDao) // This would be mocked in a real-word scenario.

    private val createPaymentRequest = CreatePaymentRequest(
        client = client,
        paymentInformation = Fixtures.boletoPaymentInformation
    )
    private val expectedPayment = Fixtures.payment()

    private val paymentService = PaymentService(
        clientValidator = clientValidator,
        paymentProcessors = paymentProcessors,
        paymentRepository = paymentRepository
    )

    @BeforeEach
    fun setUp() {
        paymentDao.clearDatabase()
    }

    @Nested
    inner class FindById {
        @Test
        fun `returns successful PaymentResponse when Payment exists`() {
            paymentRepository.insert(Fixtures.boletoPaymentInformation)

            val expectedResponse = PaymentResponse(
                successful = true,
                payment = expectedPayment
            )

            val actualResponse = paymentService.findById(expectedPayment.id!!)
            assertEquals(expectedResponse, actualResponse)
        }

        @Test
        fun `returns failed PaymentResponse when Payment does not exist`() {
            val expectedResponse = PaymentResponse(
                successful = false,
                errorMessage = "Payment 999 not found."
            )

            val actualResponse = paymentService.findById(999)
            assertEquals(expectedResponse, actualResponse)
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `returns successful PaymentResponse when Payment is created`() {
            val expectedResponse = PaymentResponse(
                successful = true,
                payment = expectedPayment
            )

            val paymentProcessor = mockk<PaymentProcessor>()

            every { clientValidator.validate(client) } returns Validation(true)
            every { paymentProcessor.processPayment(Fixtures.boletoPaymentInformation) } returns expectedResponse
            every { paymentProcessors[PaymentType.BOLETO] } returns paymentProcessor

            val actualResponse = paymentService.create(createPaymentRequest)
            assertEquals(expectedResponse, actualResponse)
        }

        @Test
        fun `returns failed PaymentResponse when Client validation fails`() {
            every { clientValidator.validate(client) } returns Validation(false, listOf("Error 1", "Error 2"))

            val paymentResponse = paymentService.create(createPaymentRequest)

            assertFalse(paymentResponse.successful)
            assertEquals("Error 1, Error 2", paymentResponse.errorMessage)
            assertNull(paymentResponse.payment)
        }

        @Test
        fun `throws InvalidParameterException when PaymentProcessor does not exist`() {
            every { clientValidator.validate(client) } returns Validation(true)
            every { paymentProcessors[PaymentType.BOLETO] } returns null

            assertThrows<IllegalArgumentException> {
                paymentService.create(createPaymentRequest)
            }
        }
    }
}