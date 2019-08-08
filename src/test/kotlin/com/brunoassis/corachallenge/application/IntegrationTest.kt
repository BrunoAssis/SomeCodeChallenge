package com.brunoassis.corachallenge.application

import khttp.get
import khttp.post
import khttp.responses.Response
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class IntegrationTest {
    companion object {
        private const val BASE_URL = "http://localhost:7000"
    }

    private val application = Application()

    @BeforeAll
    fun setUp() {
        application.start()
    }

    @Nested
    inner class General {
        @Test
        fun `visit root`() {
            val response = get("$BASE_URL/")
            val routes = response.jsonArray
            assertEquals(2, routes.count())

            val firstRoute = routes[0] as JSONObject
            assertEquals("/payments", firstRoute["path"])
            assertEquals("POST", firstRoute["httpMethod"])
        }

        @Test
        fun `visit invalid URL`() {
            val response = get("$BASE_URL/non_existent_route")
            assertEquals(404, response.statusCode)

            val errorResponse = response.jsonObject
            assertEquals("404 - Not found.", errorResponse["error"])
        }

        @Test
        fun `sending a bad payload`() {
            val payload = "{'invalid': 'payload'}"
            val response = post(
                url = "$BASE_URL/payments/",
                headers = hashMapOf("Content-type" to "application/json"),
                data = payload
            )

            assertEquals(400, response.statusCode)

            val errorResponse = response.jsonObject
            assertEquals("Couldn't deserialize body to CreatePaymentRequest", errorResponse["error"])
        }
    }

    @Nested
    inner class EndToEnd {
        @Nested
        inner class CreditCard {
            @Test
            fun `create a Credit Card payment and check its status`() {
                val paymentId = createCreditCardPayment()
                checkCreditCardPayment(paymentId)
            }

            private fun createCreditCardPayment(): Int {
                val payload =
                    IntegrationTest::class.java.getResource("/examples/inputs/credit_card_payment.json").readText()
                val response = post(
                    url = "$BASE_URL/payments/",
                    headers = hashMapOf("Content-type" to "application/json"),
                    data = payload
                )

                assertEquals(201, response.statusCode)
                validateCreditCardPaymentResponse(response)
                return response.jsonObject["id"] as Int
            }

            private fun validateCreditCardPaymentResponse(response: Response) {
                val paymentResponse = response.jsonObject
                assertNotNull(paymentResponse["id"])

                val status = paymentResponse["status"] as JSONObject
                assertEquals("NOT_PAID", status["paymentStatusType"])

                val information = paymentResponse["information"] as JSONObject
                val buyer = information["buyer"] as JSONObject
                assertEquals("John Doe", buyer["name"])

                val chargeInformation = information["chargeInformation"] as JSONObject
                assertEquals("CREDIT_CARD", chargeInformation["type"])
            }

            private fun checkCreditCardPayment(paymentId: Int) {
                val response = get("$BASE_URL/payments/$paymentId")
                assertEquals(200, response.statusCode)
                validateCreditCardPaymentResponse(response)
            }
        }

        @Nested
        inner class Boleto {
            @Test
            fun `create a Boleto payment and check its status`() {
                val paymentId = createBoletoPayment()
                checkBoletoPayment(paymentId)
            }

            private fun createBoletoPayment(): Int {
                val payload = IntegrationTest::class.java.getResource("/examples/inputs/boleto_payment.json").readText()
                val response = post(
                    url = "$BASE_URL/payments/",
                    headers = hashMapOf("Content-type" to "application/json"),
                    data = payload
                )

                assertEquals(201, response.statusCode)
                validateBoletoPaymentResponse(response)
                return response.jsonObject["id"] as Int
            }

            private fun validateBoletoPaymentResponse(response: Response) {
                val paymentResponse = response.jsonObject
                assertNotNull(paymentResponse["id"])

                val status = paymentResponse["status"] as JSONObject
                assertEquals("NOT_PAID", status["paymentStatusType"])

                val information = paymentResponse["information"] as JSONObject
                val buyer = information["buyer"] as JSONObject
                assertEquals("John Doe", buyer["name"])

                val chargeInformation = information["chargeInformation"] as JSONObject
                assertEquals("BOLETO", chargeInformation["type"])

                val boleto = chargeInformation["boleto"] as JSONObject
                val expirationDateString = boleto["expirationDate"] as String
                val expirationDate = LocalDate.parse(
                    expirationDateString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
                assertTrue(expirationDate.isAfter(LocalDate.now()))

                val typableNumber = boleto["typableNumber"] as String
                assertEquals(48, typableNumber.length)
            }

            private fun checkBoletoPayment(paymentId: Int) {
                val response = get("$BASE_URL/payments/$paymentId")
                assertEquals(200, response.statusCode)
                validateBoletoPaymentResponse(response)
            }
        }

        @Test
        fun `check non-existent payment`() {
            val response = get("$BASE_URL/payments/999")
            assertEquals(400, response.statusCode)
            val errorResponse = response.jsonObject
            assertEquals("Payment 999 not found.", errorResponse["error"])
        }
    }
}