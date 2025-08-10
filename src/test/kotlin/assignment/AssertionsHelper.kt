package assignment

import assignment.helpers.allure.AllureReportHelper.step
import assignment.models.AssigneeType
import assignment.models.ErrorResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.testng.asserts.SoftAssert
import retrofit2.Response

private val gson = Gson()

fun assertLicenseAssignedSuccessfully(
    response: Response<Any?>, softAssert: SoftAssert,
    expectedHttpStatusCode: Int,
    licenseId: String,
    expectedAssigneeType: AssigneeType,
    expectedAssigneeName: String,
    expectedAssigneeEmail: String
) {
    step("Check that license successfully assigned") {
        step("Check that HTTP status code is $expectedHttpStatusCode") {
            softAssert.assertEquals(
                response.code(), expectedHttpStatusCode,
                "Status code in response for assign license differs from expected"
            )
        }

        step("Check license info after assignment") {
            val license = ApiClients.orgAdminAccountsApiHelper.getLicenseById(licenseId)
            val assignee = license.assignee
            step("Check assignee is not null") {
                softAssert.assertNotNull(assignee, "Assignee expected to be not null after license was assigned")
            }
            step("Check assignee type is $expectedAssigneeType") {
                softAssert.assertEquals(assignee?.type, expectedAssigneeType, "Assignee.Type mismatch")
            }
            step("Check assignee name is $expectedAssigneeName") {
                softAssert.assertEquals(assignee?.name, expectedAssigneeName, "Assignee.Name mismatch")
            }
            step("Check assignee email is $expectedAssigneeEmail") {
                softAssert.assertEquals(assignee?.email, expectedAssigneeEmail, "Assignee.Email mismatch")
            }
        }
    }
}

fun assertLicenseHasNoAssignee(softAssert: SoftAssert, licenseId: String) {
    val license = ApiClients.orgAdminAccountsApiHelper.getLicenseById(licenseId)
    val assignee = license.assignee
    softAssert.assertNull(assignee, "Assignee expected to be null.")
}

fun assertGeneralErrorResponse(
    response: Response<Any?>, softAssert: SoftAssert,
    expectedHttpCode: Int,
    expectedCode: String, expectedDescription: String
) {
    step("Assert that HTTP response body") {
        step("Check HTTP response code is $expectedHttpCode") {
            softAssert.assertEquals(
                response.code(), expectedHttpCode,
                "HTTP status code mismatch."
            )
        }

        val errorBody = response.errorBody()
        step("Check HTTP response object has (error) body") {
            softAssert.assertNotNull(errorBody, "Unexpected Error response body.")
        }

        errorBody?.let {
            try {
                val body = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
                step("Check CODE in response body is $expectedCode") {
                    softAssert.assertEquals(
                        body.code, expectedCode,
                        "Mismatching Code field in Error response."
                    )
                }
                step("Check DESCRIPTION in response body is $expectedDescription") {
                    softAssert.assertEquals(
                        body.description, expectedDescription,
                        "Mismatching Description field in Error response."
                    )
                }
            } catch (ex: JsonSyntaxException) {
                softAssert.fail("Failed to parse error details. Cause: ${ex.message}")
            }
        }
    }
}

fun assertLicenseTransferredSuccessfully(
    response: Response<Any?>, softAssert: SoftAssert,
    expectedHttpCode: Int,
    licenseId: String,
    expectedTeamId: Int
) {
    step("Check license successfully transferred") {
        step("Check HTTP status code is $expectedHttpCode") {
            softAssert.assertEquals(
                response.code(), 200,
                "HTTP status code mismatch"
            )
        }

        val license = step("Get license $licenseId actual info") {
            ApiClients.orgAdminAccountsApiHelper.getLicenseById(licenseId)
        }
        step("Check team ID in license is $expectedTeamId") {
            softAssert.assertEquals(license.team?.id, expectedTeamId)
        }
    }
}