package com.brunoassis.corachallenge.helpers

import com.brunoassis.corachallenge.entity.Buyer
import com.brunoassis.corachallenge.entity.ChargeInformation
import com.brunoassis.corachallenge.entity.CreditCard
import com.brunoassis.corachallenge.entity.Payment
import com.brunoassis.corachallenge.entity.PaymentInformation
import com.brunoassis.corachallenge.entity.PaymentStatus
import com.brunoassis.corachallenge.entity.PaymentStatusType
import com.brunoassis.corachallenge.entity.PaymentType
import java.math.BigDecimal
import java.time.LocalDate

object Fixtures {
    internal val buyer = Buyer(
        name = "John Doe",
        email = "john.doe@gmail.com",
        cpf = "12345678901"
    )

    internal val boletoChargeInformation = ChargeInformation(
        amount = BigDecimal.TEN,
        type = PaymentType.BOLETO
    )

    internal val creditCard = CreditCard(
        holderName = "John Doe",
        number = "4567890123456789",
        expirationDate = LocalDate.now().plusYears(1),
        cvv = "666"
    )
    internal val creditCardChargeInformation = ChargeInformation(
        amount = BigDecimal.TEN,
        type = PaymentType.CREDIT_CARD,
        creditCard = creditCard
    )

    internal val boletoPaymentInformation = PaymentInformation(
        buyer = buyer,
        chargeInformation = boletoChargeInformation
    )

    internal val creditCardPaymentInformation = PaymentInformation(
        buyer = buyer,
        chargeInformation = creditCardChargeInformation
    )

    internal fun payment(id: Long? = 1) = Payment(
        id = id,
        status = PaymentStatus(PaymentStatusType.NOT_PAID),
        information = boletoPaymentInformation
    )
}
