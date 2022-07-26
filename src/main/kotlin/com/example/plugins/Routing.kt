package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    routing {
        route("/") {
            get {
                call.respondText("Hello")
            }
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
