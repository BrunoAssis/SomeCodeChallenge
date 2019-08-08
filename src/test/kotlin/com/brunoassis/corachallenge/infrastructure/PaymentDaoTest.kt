package com.brunoassis.corachallenge.infrastructure

import com.brunoassis.corachallenge.entity.error.PaymentNotFoundException
import com.brunoassis.corachallenge.helpers.Fixtures
import com.brunoassis.corachallenge.infrastructure.dao.PaymentDao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class PaymentDaoTest {
    private val paymentDao = PaymentDao()

    @BeforeEach
    fun setUp() {
        paymentDao.clearDatabase()
    }

    @Nested
    inner class Insert {
        @Test
        fun `returns inserted payment after persisting`() {
            val paymentToInsert = Fixtures.payment(id = null)
            assertNull(paymentToInsert.id)

            val insertedPayment = paymentDao.insert(paymentToInsert)
            assertNotNull(insertedPayment.id)

            assertEquals(paymentToInsert.information, insertedPayment.information)
            assertEquals(paymentToInsert.status, insertedPayment.status)
        }
    }

    @Nested
    inner class FindById {
        @Test
        fun `returns Payment when it exists`() {
            val insertedPayment = paymentDao.insert(Fixtures.payment(id = null))
            val foundPayment = paymentDao.findById(insertedPayment.id!!)
            assertSame(insertedPayment, foundPayment)
        }

        @Test
        fun `throws PaymentNotFoundException when Payment does not exist`() {
            assertThrows<PaymentNotFoundException> {
                paymentDao.findById(999)
            }
        }
    }

    @Nested
    inner class ClearDatabase {
        @Test
        fun `clears database`() {
            val paymentToInsert = Fixtures.payment(id = null)

            paymentDao.insert(paymentToInsert)
            paymentDao.insert(paymentToInsert)
            assertEquals(2, paymentDao.recordCount())

            paymentDao.clearDatabase()
            assertEquals(0, paymentDao.recordCount())
        }
    }
}