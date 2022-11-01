import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.response.respondFile
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level
import util.logger
import util.stackTraceAsString
import java.io.File

fun main(args: Array<String>) {
    AppArgs().main(args)
}

class AppArgs : CliktCommand() {
    private val port: Int by option("-p", help = "Port").int().default(8080)
    private val ext: String by option("--ext", help = "The extension of image files (like '.jpeg')").required()
    private val ignApi: String by option("--ignApi", help = "The IGN api key").required()
    private val ordnanceSurveyApi: String by option(
        "--ordnanceSurveyApi",
        help = "The Ordnance Survey api key"
    ).required()

    override fun run() {
        val server = embeddedServer(Netty, port = port) {
            WmtsApiApplication(ext, ignApi, ordnanceSurveyApi).apply {
                main()
            }
        }
        server.start(wait = true)
    }
}

class WmtsApiApplication(
    private val ext: String,
    private val ignApi: String,
    private val ordnanceSurveyApi: String
) {
    private val logger by logger()

    fun Application.main() {
        install(CallLogging) {
            level = Level.INFO
        }

        routing {
            get("ign-api") {
                call.respondText(ignApi)
            }
            get("ordnance-survey-api") {
                call.respondText(ordnanceSurveyApi)
            }

            static("trekme-privacy-policy") {
                resources("trekme-privacy-policy")
                defaultResource("index.html", "trekme-privacy-policy")
            }
        }
    }
}

