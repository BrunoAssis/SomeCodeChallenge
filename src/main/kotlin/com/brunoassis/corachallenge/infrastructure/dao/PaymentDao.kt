package com.brunoassis.corachallenge.infrastructure.dao

import com.brunoassis.corachallenge.entity.Payment
import com.brunoassis.corachallenge.entity.error.PaymentNotFoundException

class PaymentDao : Dao<Payment> {

    /*
    To make things simple, I'm using a static MutableMap as a database.
    In a real-world scenario, this would be replaced by a Dao backed by
    a "real" database. This interface would work for most non-relational databases,
    assuming a Partition Key which can be used to do lookups.

    For relational databases the interface would be quite different.

    Note that the database is in-memory, so it's cleared every time the app starts.
    */
    companion object {
        private val database: MutableMap<Long, Payment> = mutableMapOf()
    }

    override fun insert(toBeInserted: Payment): Payment {
        val lastId = findLastId()
        val newId = lastId + 1
        val newPayment = Payment(
            id = newId,
            status = toBeInserted.status,
            information = toBeInserted.information
        )
        database[newId] = newPayment
        return newPayment
    }

    override fun findById(id: Long): Payment {
        val payment = database[id]
        if (payment != null) {
            return payment
        } else {
            throw PaymentNotFoundException()
        }
    }

    internal fun clearDatabase() = database.clear()

    internal fun recordCount() = database.keys.size

    private fun findLastId(): Long = database.keys.max() ?: 0
}