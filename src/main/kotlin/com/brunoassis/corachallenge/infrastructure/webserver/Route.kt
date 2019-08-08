package com.brunoassis.corachallenge.infrastructure.webserver

import com.fasterxml.jackson.annotation.JsonIgnore
import io.javalin.http.Handler
import io.javalin.http.HandlerType

data class Route(
    val path: String,
    val httpMethod: HandlerType,
    @JsonIgnore
    val handler: Handler
)
