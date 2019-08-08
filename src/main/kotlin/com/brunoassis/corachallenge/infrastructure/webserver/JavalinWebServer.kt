package com.brunoassis.corachallenge.infrastructure.webserver

import com.brunoassis.corachallenge.domain.service.PaymentService
import com.brunoassis.corachallenge.entity.error.ErrorResponse
import com.brunoassis.corachallenge.infrastructure.webserver.controller.CheckPaymentStatusController
import com.brunoassis.corachallenge.infrastructure.webserver.controller.CreatePaymentController
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.javalin.Javalin
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.HandlerType
import io.javalin.plugin.json.JavalinJackson
import org.eclipse.jetty.http.HttpStatus
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat

class JavalinWebServer(private val paymentService: PaymentService) {
    companion object {
        private const val SERVER_PORT = 7000
        private const val ERROR_404_MESSAGE = "404 - Not found."
    }

    private val logger = LoggerFactory.getLogger(JavalinWebServer::class.java)

    fun start() {
        val routes = listOf(
            Route(
                "/payments",
                HandlerType.POST,
                CreatePaymentController(paymentService)
            ),
            Route(
                "/payments/:payment-id",
                HandlerType.GET,
                CheckPaymentStatusController(paymentService)
            )
        )

        val app = startApp()

        routes.forEach {
            app.addHandler(it.httpMethod, it.path, it.handler)
        }

        // Configuring JSON mapper to format dates and remove nulls (it could be injected for modularity, no need here)
        val mapper = JavalinJackson.getObjectMapper()
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.registerModule(JavaTimeModule())
        mapper.dateFormat = SimpleDateFormat("yyyy-MM-dd")
        JavalinJackson.configure(mapper)

        // Defining root path as a kind of route discovery
        app.get("/") { it.json(routes) }
    }

    private fun startApp(): Javalin {
        return Javalin.create().apply {
            exception(BadRequestResponse::class.java, ::handleException)
            exception(Exception::class.java, ::handleException)
            error(HttpStatus.NOT_FOUND_404) { it.json(ErrorResponse(ERROR_404_MESSAGE)) }
        }.start(SERVER_PORT)
    }

    private fun handleException(exception: Exception, context: Context) {
        logger.error(exception.localizedMessage, exception);
        context.status(HttpStatus.BAD_REQUEST_400)
        context.json(ErrorResponse(exception.localizedMessage))
    }
}