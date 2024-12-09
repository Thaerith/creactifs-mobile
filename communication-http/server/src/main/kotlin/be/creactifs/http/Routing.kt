package be.creactifs.http

import be.creactifs.http.api.PostPosition
import be.creactifs.http.position.addPosition
import be.creactifs.http.position.getPositions
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        route("/positions") {
            get {
                val positions = getPositions()
                call.respond(HttpStatusCode.OK, positions)
            }

            post {
                val request = call.receive<PostPosition>()
                val answer = addPosition(request)
                call.respond(HttpStatusCode.OK, answer)
            }
        }
    }
}