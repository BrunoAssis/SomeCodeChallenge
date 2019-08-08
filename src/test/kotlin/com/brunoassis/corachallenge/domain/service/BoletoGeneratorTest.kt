package com.brunoassis.corachallenge.domain.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class BoletoGeneratorTest {
    private val boletoGenerator = BoletoGenerator()

    @Nested
    inner class Generate {
        @Test
        fun `returns a Boleto with random information`() {
            val boleto = boletoGenerator.generate()
            assertEquals(48, boleto.typableNumber.length)
            assertTrue(boleto.expirationDate.isAfter(LocalDate.now()))
        }
    }
}