package util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.io.StringWriter

/**
 * To use this, just include it in a class like this :
 * ```
 * import util.logger
 *
 * class MyApp {
 *   val logger by logger()
 *   ...
 *   logger.debug("A debug msg")
 * }
 * ```
 */
fun <R : Any> R.logger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(this.javaClass) }
}

/**
 * Useful to get a logger from outside a class context. Example:
 * ```
 * import util.logger
 *
 * private val logger: Logger by logger("Session")
 *
 * fun saveSession(user: String) {
 *    try {
 *      ...
 *    } catch (e: NullPointerException) {
 *      logger.error("Error message")
 *    }
 * }
 * ```
 */
fun logger(name: String): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(name) }
}

fun Throwable.stackTraceAsString(): String {
    val sw = StringWriter()
    printStackTrace(PrintWriter(sw))
    return sw.toString()
}