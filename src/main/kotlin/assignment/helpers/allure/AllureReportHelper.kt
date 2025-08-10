package assignment.helpers.allure

import io.qameta.allure.Allure
import io.qameta.allure.Step

object AllureReportHelper {
    @Step("{description}")
    fun <T> step(description: String, unit: () -> T): T {
        return unit.invoke()
    }

    fun attachPlainText(name: String, text: String) {
        Allure.addAttachment(name, "text/plain", text)
    }

    fun attachJson(name: String, json: String) {
        Allure.addAttachment(name, "application/json", json, "json")
    }
}