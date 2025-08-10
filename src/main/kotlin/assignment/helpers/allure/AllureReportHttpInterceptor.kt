package assignment.helpers.allure

import assignment.helpers.allure.AllureReportHelper.step
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class AllureReportHttpInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return step("HTTP Call: ${request.method} on ${request.url}") {
            val stringBuilder = StringBuilder()
            stringBuilder.append("${request.method} ${request.url}")
            stringBuilder.append("\n")
            stringBuilder.append("${request.headers}")
            stringBuilder.append("\n")
            request.body?.let { body ->
                val buffer = okio.Buffer()
                body.writeTo(buffer)
                stringBuilder.append(buffer.readUtf8())
            }

            AllureReportHelper.attachPlainText("Request", stringBuilder.toString())

            stringBuilder.clear()

            val response = chain.proceed(request)

            stringBuilder.append("${response.code} ${response.message}")
            stringBuilder.append("\n")
            stringBuilder.append("Headers: ${response.headers}")
            stringBuilder.append("\n")

            val responseBody = response.body
            val content = responseBody?.string() ?: ""

            stringBuilder.append(content)
            AllureReportHelper.attachPlainText("Response", stringBuilder.toString())

            val newBody = content.toResponseBody(responseBody?.contentType())

            return@step response.newBuilder()
                .code(response.code)
                .message(response.message)
                .headers(response.headers)
                .body(newBody)
                .build()
        }
    }
}