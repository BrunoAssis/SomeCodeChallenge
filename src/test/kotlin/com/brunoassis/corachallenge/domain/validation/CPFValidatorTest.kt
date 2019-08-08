package com.brunoassis.corachallenge.domain.validation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class CPFValidatorTest {
    private val cpfValidator = CPFValidator()
    private val valid = "12345678901"

    @Nested
    inner class Validate {
        @Test
        fun `return successful Validation when CPF is valid`() {
            assertEquals(Validation(true), cpfValidator.validate(valid))
        }

        @Test
        fun `return failed Validation when CPF length is smaller than 11`() {
            val invalid = "123"
            assertEquals(
                Validation(false, listOf("CPF should have exactly 11 characters.")),
                cpfValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when CPF length is larger than 11`() {
            val invalid = "1234567890123456"
            assertEquals(
                Validation(false, listOf("CPF should have exactly 11 characters.")),
                cpfValidator.validate(invalid)
            )
        }
    }
}