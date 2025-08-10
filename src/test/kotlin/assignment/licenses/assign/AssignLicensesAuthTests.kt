package assignment.licenses.assign

import assignment.*
import assignment.clients.AccountApiClient
import assignment.helpers.accounts.byAbilityToAssign
import assignment.helpers.accounts.byTeamId
import assignment.helpers.allure.AllureReportHelper.step
import assignment.models.AssigneeType
import assignment.models.Contact
import assignment.models.License
import assignment.models.LicenseAssignRequestBody
import org.testng.annotations.Test
import org.testng.asserts.SoftAssert

class AssignLicensesAuthTests : AbstractAccountsTests() {
    @Test(description = "Team Admin can assign license from his team by license ID")
    fun teamAdminCanAssignLicenseOfHisTeamTest() {
        val licenseBefore = ApiClients.orgAdminAccountsApiHelper.selectFirstMatchingLicense(
            byTeamId(team1Id),
            byAbilityToAssign(true)
        )

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = licenseBefore.licenseId,
            sendEmail = false
        )

        val response = ApiClients.team1AdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertLicenseAssignedSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpStatusCode = 200,
            licenseId = licenseBefore.licenseId!!,
            expectedAssigneeType = AssigneeType.USER,
            expectedAssigneeName = "Roman Loskutov",
            expectedAssigneeEmail = "ofmgreen@gmail.com"
        )
        softAssert.assertAll()
    }

    @Test(description = "Viewer is unable to assign license from his team")
    fun teamViewerCanNotAssignLicenseOfHisTeamTest() {
        val licenseBefore = ApiClients.orgAdminAccountsApiHelper.selectFirstMatchingLicense(
            byTeamId(team1Id),
            byAbilityToAssign(true)
        )

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = licenseBefore.licenseId,
            sendEmail = false
        )

        val response = ApiClients.team1ViewerAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 403,
            expectedCode = "INSUFFICIENT_PERMISSIONS",
            expectedDescription = "Missing Edit permission on customer $customerCode or on team with id $team1Id"
        )

        softAssert.assertAll()
    }

    @Test(description = "Admin is unable to assign license from not his team by license ID")
    fun teamAdminCanNotAssignLicenseOfAnotherTeamByLicenseIdTest() {
        val licenseBefore = ApiClients.orgAdminAccountsApiHelper.selectFirstMatchingLicense(
            byTeamId(team2Id),
            byAbilityToAssign(true)
        )

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = licenseBefore.licenseId,
            sendEmail = false
        )

        val response = ApiClients.team1AdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 403,
            expectedCode = "TEAM_MISMATCH",
            expectedDescription = "Token was generated for team with id $team1Id"
        )

        val licenseAfter = ApiClients.orgAdminAccountsApiHelper.getLicenseById(licenseBefore.licenseId!!)
        softAssert.assertNull(licenseAfter.assignee, "Assignee expected to be null after failed license assignment.")

        softAssert.assertAll()
    }

    @Test(description = "Admin is unable to assign license from not his team by product code and team ID")
    fun teamAdminCanNotAssignLicenseOfAnotherTeamByProductAndTeamTest() {
        val licenseBefore = ApiClients.orgAdminAccountsApiHelper.selectFirstMatchingLicense(
            byTeamId(team2Id),
            byAbilityToAssign(true)
        )

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            license = License(
                productCode = licenseBefore.product?.code,
                team = licenseBefore.team?.id
            ),
            sendEmail = false
        )

        val response = ApiClients.team1AdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 403,
            expectedCode = "TEAM_MISMATCH",
            expectedDescription = "Token was generated for team with id $team1Id"
        )
        softAssert.assertAll()
    }

    @Test(description = "X-Api-Key header should be not null to assign license")
    fun apiTokenCanNotBeNullToAssignLicenseTest() {
        val licenseBefore = step("Select license for test") {
            ApiClients.orgAdminAccountsApiHelper.selectFirstMatchingLicense(
                byTeamId(team1Id),
                byAbilityToAssign(true)
            )
        }

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = licenseBefore.licenseId,
            sendEmail = false
        )

        val apiClientWithoutToken = AccountApiClient.withHeaders(apiKey = null, customerCode = customerCode)

        val response = apiClientWithoutToken.assignLicense(requestBody).execute()

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

    @Test(description = "X-Api-Key header should be valid to assign license")
    fun apiTokenShouldBeValidToAssignLicenseAssignTest() {
        val licenseBefore = step("Select license for test") {
            ApiClients.orgAdminAccountsApiHelper.selectFirstMatchingLicense(
                byTeamId(team1Id),
                byAbilityToAssign(true)
            )
        }

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = licenseBefore.licenseId,
            sendEmail = false
        )

        val apiClientWithoutToken = AccountApiClient.withHeaders(apiKey = "something", customerCode = customerCode)

        val response = apiClientWithoutToken.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 401,
            expectedCode = "INVALID_TOKEN",
            expectedDescription = "The token provided is invalid"
        )
        softAssert.assertAll()
    }

    @Test(description = "X-Customer-Code header should be not null to assign license")
    fun customerCodeCanNotBeNullToAssignLicenseTest() {
        val licenseBefore = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byTeamId(team1Id),
                byAbilityToAssign(true)
            )

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = licenseBefore.licenseId,
            sendEmail = false
        )

        val apiClientWithoutToken = AccountApiClient.withHeaders(apiKey = orgAdminApiKey, customerCode = null)

        val response = apiClientWithoutToken.assignLicense(requestBody).execute()

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

    @Test(description = "X-Customer-Code should be valid to assign license")
    fun customerCodeShouldBeValidToAssignLicenseTest() {
        val licenseBefore = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byTeamId(team1Id),
                byAbilityToAssign(true)
            )

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = licenseBefore.licenseId,
            sendEmail = false
        )

        val apiClientWithoutToken = AccountApiClient
            .withHeaders(apiKey = orgAdminApiKey, customerCode = Int.MAX_VALUE.toString())

        val response = apiClientWithoutToken.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 401,
            expectedCode = "INVALID_TOKEN",
            expectedDescription = "Some informative description"
        )
        softAssert.assertAll()
    }
}