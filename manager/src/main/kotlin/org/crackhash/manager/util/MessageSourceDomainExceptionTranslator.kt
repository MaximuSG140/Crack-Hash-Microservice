package org.crackhash.manager.util

import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URISyntaxException
import java.util.*

@Component
class MessageSourceDomainExceptionTranslator(private val message: MessageSource): DomainExceptionTranslator {
    override fun toProblemDetail(httpStatusCode: HttpStatusCode, exception: DomainException): ProblemDetail =
        run {
            check(httpStatusCode is HttpStatus) { "Unexpected inheritor to HttpStatusCode" }
            val problemDetail = ProblemDetail.forStatus(httpStatusCode)
            problemDetail.type = toType(exception.errorCode)
            problemDetail.title = toTitle(exception.errorCode, exception.args, httpStatusCode.reasonPhrase, Locale.getDefault())
            problemDetail.detail = toDetail(exception.errorCode, exception.args, httpStatusCode.reasonPhrase, Locale.getDefault())
            problemDetail
    }

    private fun toType(errorCode: String): URI =
        try { URI(errorCode.replace('.', ':')) }
        catch (exception: URISyntaxException) { throw IllegalStateException(exception) }

    private fun toTitle(errorCode: String, args: Array<String>, defaultMessage: String, locale: Locale): String? =
        message.getMessage("$errorCode.title", args, defaultMessage, locale)

    private fun toDetail(errorCode: String, args: Array<String>, defaultMessage: String, locale: Locale): String? =
        message.getMessage("$errorCode.detail", args, defaultMessage, locale)
}