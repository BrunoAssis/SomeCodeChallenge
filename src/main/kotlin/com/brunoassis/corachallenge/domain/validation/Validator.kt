package com.brunoassis.corachallenge.domain.validation

interface Validator<T> {
    fun validate(toBeValidated: T) : Validation
}
