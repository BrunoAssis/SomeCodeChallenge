package com.brunoassis.corachallenge.application

import com.brunoassis.corachallenge.domain.repository.PaymentRepository
import com.brunoassis.corachallenge.domain.service.BoletoGenerator
import com.brunoassis.corachallenge.domain.service.PaymentService
import com.brunoassis.corachallenge.domain.service.paymentprocessor.BoletoPaymentProcessor
import com.brunoassis.corachallenge.domain.service.paymentprocessor.CreditCardPaymentProcessor
import com.brunoassis.corachallenge.domain.validation.BoletoPaymentValidator
import com.brunoassis.corachallenge.domain.validation.BuyerValidator
import com.brunoassis.corachallenge.domain.validation.CPFValidator
import com.brunoassis.corachallenge.domain.validation.ClientValidator
import com.brunoassis.corachallenge.domain.validation.CreditCardPaymentValidator
import com.brunoassis.corachallenge.entity.PaymentType
import com.brunoassis.corachallenge.infrastructure.dao.PaymentDao
import com.brunoassis.corachallenge.infrastructure.webserver.JavalinWebServer

/*
    This is the entry-point to the application.
    In a real-world application, it would use some dependency injection library to construct
    the application initialization tree.

    Here, for simplicity and to reduce the number of dependencies,
    I'm instantiating everything manually.
 */
fun main(args: Array<String>) {
    val application = Application()
    application.start()
}

class Application {
    fun start() {
        // Validators
        val clientValidator = ClientValidator()
        val cpfValidator = CPFValidator()
        val buyerValidator = BuyerValidator(cpfValidator = cpfValidator)
        val boletoPaymentValidator = BoletoPaymentValidator()
        val creditCardPaymentValidator = CreditCardPaymentValidator()

        // Daos & Repositories
        val paymentDao = PaymentDao()
        val paymentRepository = PaymentRepository(paymentDao = paymentDao)

        // Payment Processors
        val boletoGenerator = BoletoGenerator()
        val boletoPaymentProcessor = BoletoPaymentProcessor(
            boletoGenerator = boletoGenerator,
            boletoPaymentValidator = boletoPaymentValidator,
            buyerValidator = buyerValidator,
            paymentRepository = paymentRepository
        )

        val creditCardPaymentProcessor =
            CreditCardPaymentProcessor(
                creditCardPaymentValidator = creditCardPaymentValidator,
                buyerValidator = buyerValidator,
                paymentRepository = paymentRepository
            )

        val paymentProcessors = hashMapOf(
            PaymentType.BOLETO to boletoPaymentProcessor,
            PaymentType.CREDIT_CARD to creditCardPaymentProcessor
        )

        // Services
        val paymentService = PaymentService(
            clientValidator = clientValidator,
            paymentRepository = paymentRepository,
            paymentProcessors = paymentProcessors
        )

        val javalinWebServer = JavalinWebServer(paymentService)
        javalinWebServer.start()
    }
}