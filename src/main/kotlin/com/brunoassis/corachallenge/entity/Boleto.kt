package com.brunoassis.corachallenge.entity

import java.time.LocalDate

/*
In a real-word project there would be much more information here, such as bank, the amount itself
and additional information that helps generating the typable number.
*/
data class Boleto(val typableNumber: String, val expirationDate: LocalDate)