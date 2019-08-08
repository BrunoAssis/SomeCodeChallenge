package com.brunoassis.corachallenge.entity

data class PaymentResponse(val successful: Boolean, val payment: Payment? = null, val errorMessage: String? = null)