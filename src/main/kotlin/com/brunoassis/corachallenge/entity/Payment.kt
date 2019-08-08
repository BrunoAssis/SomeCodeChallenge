package com.brunoassis.corachallenge.entity

data class Payment(val id: Long? = null, val status: PaymentStatus, val information: PaymentInformation)