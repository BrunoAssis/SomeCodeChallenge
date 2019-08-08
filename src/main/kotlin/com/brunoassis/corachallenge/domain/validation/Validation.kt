package com.brunoassis.corachallenge.domain.validation

data class Validation(val successful: Boolean, val errors: List<String> = emptyList()) {
    fun errorMessage() = errors.joinToString(", ")
}