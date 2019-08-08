package com.brunoassis.corachallenge.domain.repository

import com.brunoassis.corachallenge.entity.Payment
import com.brunoassis.corachallenge.entity.PaymentInformation
import com.brunoassis.corachallenge.entity.PaymentStatus
import com.brunoassis.corachallenge.entity.PaymentStatusType
import com.brunoassis.corachallenge.entity.error.PaymentNotFoundException
import com.brunoassis.corachallenge.infrastructure.dao.PaymentDao

class PaymentRepository(private val paymentDao: PaymentDao) {
    fun insert(paymentInformation: PaymentInformation): Payment =
        paymentDao.insert(
            Payment(
                status = PaymentStatus(PaymentStatusType.NOT_PAID),
                information = paymentInformation
            )
        )

    fun findById(id: Long): Payment? {
        return try {
            paymentDao.findById(id)
        } catch (e: PaymentNotFoundException) {
            null
        }
    }
}
