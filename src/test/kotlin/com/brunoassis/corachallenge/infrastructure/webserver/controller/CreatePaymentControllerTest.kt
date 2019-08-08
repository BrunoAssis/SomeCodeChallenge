package com.brunoassis.corachallenge.infrastructure.webserver.controller

import com.brunoassis.corachallenge.domain.service.PaymentService
import com.brunoassis.corachallenge.entity.CreatePaymentRequest
import com.brunoassis.corachallenge.entity.Payment
import com.brunoassis.corachallenge.entity.PaymentResponse
import com.brunoassis.corachallenge.entity.error.ErrorResponse
import io.javalin.http.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.eclipse.jetty.http.HttpStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CreatePaymentControllerTest {
    private val paymentService = mockk<PaymentService>()
    private val payment = mockk<Payment>()
    private val paymentResponse = mockk<PaymentResponse>()
    private val createPaymentRequest = mockk<CreatePaymentRequest>()
    private val context = mockk<Context>()

    private val controller = CreatePaymentController(paymentService = paymentService)

    @Nested
    inner class Handle {
        @BeforeEach
        fun `set up`() {
            every { paymentResponse.payment } returns payment
            every { paymentService.create(createPaymentRequest) } returns paymentResponse
            every { context.bodyAsClass(CreatePaymentRequest::class.java) } returns createPaymentRequest
            every { context.status(any()) } returns context
            every { context.json(any<Payment>()) } returns context
        }

        @Test
        fun `returns success when paymentService responds success`() {
            every { paymentResponse.successful } returns true

            controller.handle(context)

            verify { context.status(HttpStatus.CREATED_201) }
            verify { context.json(payment) }
        }

        @Test
        fun `returns error when paymentService responds an error`() {
            every { paymentResponse.successful } returns false
            every { paymentResponse.errorMessage } returns "Lots of errors here :("

            controller.handle(context)

            verify { context.status(HttpStatus.BAD_REQUEST_400) }
            verify { context.json(ErrorResponse("Lots of errors here :(")) }
        }
    }
}