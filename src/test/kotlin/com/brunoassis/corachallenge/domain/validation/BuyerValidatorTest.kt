package com.brunoassis.corachallenge.domain.validation

import com.brunoassis.corachallenge.helpers.Fixtures
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BuyerValidatorTest {
    private val cpfValidator = mockk<CPFValidator>()
    private val buyerValidator = BuyerValidator(cpfValidator = cpfValidator)
    private val valid = Fixtures.buyer

    @BeforeEach
    fun setUp() {
        every { cpfValidator.validate(any()) } returns Validation(true)
    }

    @Nested
    inner class Validate {
        @Test
        fun `return successful Validation when Buyer is valid`() {
            assertEquals(Validation(true), buyerValidator.validate(valid))
        }

        @Test
        fun `return failed Validation when Buyer CPF is invalid`() {
            val expectedValidation = Validation(false, listOf("Invalid CPF"))
            every { cpfValidator.validate(any()) } returns expectedValidation
            assertEquals(expectedValidation, buyerValidator.validate(valid))
        }

        @Test
        fun `return failed Validation when Buyer name is invalid`() {
            val invalid = valid.copy(name = "")
            assertEquals(
                Validation(false, listOf("Buyer's name shouldn't be blank.")),
                buyerValidator.validate(invalid)
            )
        }

        @Test
        fun `return failed Validation when Buyer email is invalid`() {
            val invalid = valid.copy(email = "")
            assertEquals(
                Validation(false, listOf("Buyer's e-mail shouldn't be blank.")),
                buyerValidator.validate(invalid)
            )
        }
    }
}