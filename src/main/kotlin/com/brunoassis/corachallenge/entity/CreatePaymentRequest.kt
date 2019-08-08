package com.brunoassis.corachallenge.entity

data class CreatePaymentRequest(val client: Client, val paymentInformation: PaymentInformation)