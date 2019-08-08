package com.brunoassis.corachallenge.domain.validation

import com.brunoassis.corachallenge.entity.Client
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ClientValidatorTest {
    private val clientValidator = ClientValidator()
    private val valid = Client(1)

    @Nested
    inner class Validate {
        @Test
        fun `return successful Validation when Client is valid`() {
            assertEquals(Validation(true), clientValidator.validate(valid))
        }

        @Test
        fun `return failed Validation when Client id is less than 1`() {
            val invalid = Client(0)
            assertEquals(
                Validation(false, listOf("Client.id should be greater than zero. Received 0")),
                clientValidator.validate(invalid)
            )
        }
    }
}