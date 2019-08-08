package com.brunoassis.corachallenge.domain.repository

import com.brunoassis.corachallenge.helpers.Fixtures
import com.brunoassis.corachallenge.infrastructure.dao.PaymentDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PaymentRepositoryTest {
    private val paymentDao = PaymentDao() // Would be mocked in a real-world situation

    private val paymentRepository = PaymentRepository(paymentDao)

    @BeforeEach
    fun setUp() {
        paymentDao.clearDatabase()
    }

    @Nested
    inner class Insert {
        @Test
        fun `returns inserted Payment with id`() {
            val insertedPayment = paymentRepository.insert(Fixtures.boletoPaymentInformation)
            assertEquals(Fixtures.payment(), insertedPayment)
        }
    }

    @Nested
    inner class FindById {
        @Test
        fun `returns found Payment when it exists`() {
            val insertedPayment = paymentRepository.insert(Fixtures.boletoPaymentInformation)
            val foundPayment = paymentRepository.findById(insertedPayment.id!!)
            assertSame(insertedPayment, foundPayment)
        }

        @Test
        fun `returns null when Payment does not exist`() {
            val notFoundPayment = paymentRepository.findById(999)
            assertNull(notFoundPayment)
        }
    }
}