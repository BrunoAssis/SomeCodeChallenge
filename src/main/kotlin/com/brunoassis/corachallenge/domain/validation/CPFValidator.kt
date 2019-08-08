package com.brunoassis.corachallenge.domain.validation

class CPFValidator : Validator<String> {
    override fun validate(toBeValidated: String): Validation {

        /*
        Here we could add proper CPF validation, and also check if CPF is being passed
        with or without punctuation. For simplicity sake and challenge purposes,
        I'm only checking its length.
        */
        return if (toBeValidated.length != 11) {
            Validation(successful = false, errors = listOf("CPF should have exactly 11 characters."))
        } else {
            Validation(successful = true)
        }
    }
}
