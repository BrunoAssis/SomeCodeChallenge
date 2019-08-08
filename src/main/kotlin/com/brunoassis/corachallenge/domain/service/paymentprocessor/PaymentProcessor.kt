package com.brunoassis.corachallenge.domain.service.paymentprocessor

import com.brunoassis.corachallenge.entity.PaymentInformation
import com.brunoassis.corachallenge.entity.PaymentResponse

interface PaymentProcessor {
    fun processPayment(paymentInformation: PaymentInformation) : PaymentResponse
}
