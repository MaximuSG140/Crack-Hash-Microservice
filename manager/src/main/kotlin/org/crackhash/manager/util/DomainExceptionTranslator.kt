package org.crackhash.manager.util

import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail

interface DomainExceptionTranslator {

    fun toProblemDetail(httpStatusCode: HttpStatusCode, exception: DomainException): ProblemDetail
}