package com.brunoassis.corachallenge.domain.validation

import com.brunoassis.corachallenge.entity.Buyer

class BuyerValidator(private val cpfValidator: CPFValidator) : Validator<Buyer> {

    /*
    Here we could have more specific validation logic related to buyers,
    such as existing in the database.
    If this validation grows to be more complex, e.g., check if buyer is fraudulent,
    we'd need some kind of BuyerService.
    */
    override fun validate(toBeValidated: Buyer): Validation {
        val errors = arrayListOf<String>()

        val cpfValidation = cpfValidator.validate(toBeValidated.cpf)
        if (!cpfValidation.successful) {
            errors.addAll(cpfValidation.errors)
        }

        val email = toBeValidated.email
        if (email.isBlank()) { // Additional logic such as a Regex that checks a valid RFC 6530 format
            errors.add("Buyer's e-mail shouldn't be blank.")
        }

        val name = toBeValidated.name
        if (name.isBlank()) {
            errors.add("Buyer's name shouldn't be blank.")
        }

        return Validation(successful = errors.isEmpty(), errors = errors)
    }
}
