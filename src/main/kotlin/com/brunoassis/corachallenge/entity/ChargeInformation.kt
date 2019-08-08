package com.brunoassis.corachallenge.entity

import java.math.BigDecimal

data class ChargeInformation(
    val amount: BigDecimal,
    val type: PaymentType,
    val creditCard: CreditCard? = null,
    val boleto: Boleto? = null
)
