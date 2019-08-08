package com.brunoassis.corachallenge.domain.validation

import com.brunoassis.corachallenge.entity.Client

class ClientValidator : Validator<Client> {

    /*
    Here we could have more specific validation logic related to clients,
    such as existing in the database.
    If this validation grows to be more complex, e.g., check if client has current paid plan,
    we'd need some kind of ClientService.
    */
    override fun validate(toBeValidated: Client): Validation {
        val clientId = toBeValidated.id
        return if (clientId > 0) {
            Validation(successful = true)
        } else {
            Validation(
                successful = false,
                errors = listOf("Client.id should be greater than zero. Received $clientId")
            )
        }
    }
}
