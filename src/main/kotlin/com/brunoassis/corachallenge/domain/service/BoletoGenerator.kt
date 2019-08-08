package com.brunoassis.corachallenge.domain.service

import com.brunoassis.corachallenge.entity.Boleto
import java.time.LocalDate

class BoletoGenerator {
    companion object {
        private const val BOLETO_NUMBER_DIGIT_COUNT = 48
    }

    fun generate() = Boleto(
        typableNumber = generateTypableNumber(),
        expirationDate = generateExpirationDate()
    )

    private fun generateTypableNumber(): String {
        return (1..BOLETO_NUMBER_DIGIT_COUNT).joinToString("") {
            (0..9).shuffled().first().toString()
        }
    }

    private fun generateExpirationDate(): LocalDate {
        return LocalDate.now().plusDays(7L)
    }
}
