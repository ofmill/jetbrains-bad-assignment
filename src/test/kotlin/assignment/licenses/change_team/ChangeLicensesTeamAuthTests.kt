package assignment.licenses.change_team

import assignment.ApiClients
import assignment.assertGeneralErrorResponse
import assignment.clients.AccountApiClient
import assignment.customerCode
import assignment.models.ChangeLicensesTeamRequest
import assignment.orgAdminApiKey
import org.testng.annotations.Test
import org.testng.asserts.SoftAssert

class ChangeLicensesTeamAuthTests : AbstractChangeLicensesTeamTests() {
    @Test
    fun teamAdminIsUnableToTransferLicensesTest() {
        val licenseBefore = selectLicenseToTransfer(team2Id)

        val requestBody = ChangeLicensesTeamRequest(
            licenseIds = listOf(licenseBefore.licenseId),
            targetTeamId = team1Id
        )

        val response = ApiClients.team1AdminAccountApiClient.changeLicensesTeam(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response, softAssert,
            403,
            "TOKEN_TYPE_MISMATCH",
            "Changing team is not possible with a token that was generated for a specific team"
        )
        softAssert.assertAll()
    }

    @Test
    fun teamViewerIsUnableToTransferLicensesTest() {
        val licenseBefore = selectLicenseToTransfer(team2Id)

        val requestBody = ChangeLicensesTeamRequest(
            licenseIds = listOf(licenseBefore.licenseId),
            targetTeamId = team1Id
        )

        val response = ApiClients.team1ViewerAccountApiClient.changeLicensesTeam(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response, softAssert,
            403,
            "TOKEN_TYPE_MISMATCH",
            "Changing team is not possible with a token that was generated for a specific team"
        )
        softAssert.assertAll()
    }

    @Test
    fun apiTokenRequiredForLicenseAssign() {
        val licenseBefore = selectLicenseToTransfer(team2Id)

        val requestBody = ChangeLicensesTeamRequest(
            licenseIds = listOf(licenseBefore.licenseId),
            targetTeamId = team1Id
        )

        val apiClientWithoutToken = AccountApiClient.withHeaders(apiKey = null, customerCode = customerCode)

        val response = apiClientWithoutToken.changeLicensesTeam(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 401,
            expectedCode = "MISSING_TOKEN_HEADER",
            expectedDescription = "X-Api-Key header is required"
        )
        softAssert.assertAll()
    }

    @Test
    fun customerCodeRequiredForLicenseAssign() {
        val licenseBefore = selectLicenseToTransfer(team2Id)

        val requestBody = ChangeLicensesTeamRequest(
            licenseIds = listOf(licenseBefore.licenseId),
            targetTeamId = team1Id
        )

        val apiClientWithoutToken = AccountApiClient.withHeaders(apiKey = orgAdminApiKey, customerCode = null)

        val response = apiClientWithoutToken.changeLicensesTeam(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 401,
            expectedCode = "MISSING_CUSTOMER_HEADER",
            expectedDescription = "X-Customer-Code header is required"
        )
        softAssert.assertAll()
    }
}