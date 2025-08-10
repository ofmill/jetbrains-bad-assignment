package assignment.licenses.assign

import assignment.AbstractAccountsTests
import assignment.ApiClients
import assignment.assertGeneralErrorResponse
import assignment.assertLicenseAssignedSuccessfully
import assignment.helpers.accounts.byOutdatedState
import assignment.helpers.accounts.byPerpetualProperty
import assignment.helpers.accounts.byTeamId
import assignment.models.AssigneeType
import assignment.models.Contact
import assignment.models.License
import assignment.models.LicenseAssignRequestBody
import org.testng.annotations.Test
import org.testng.asserts.SoftAssert
import java.util.*

class AssignLicensesFunctionalTests : AbstractAccountsTests() {
    @Test
    fun licenseCouldBeAssignedToContactWithoutJBAccountTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "${UUID.randomUUID()}@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertLicenseAssignedSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpStatusCode = 200,
            licenseId = license.licenseId!!,
            expectedAssigneeType = AssigneeType.USER,
            expectedAssigneeName = "Ro*** Lo******",
            expectedAssigneeEmail = requestBody.contact?.email!!
        )
        softAssert.assertAll()
    }

    @Test
    fun licenseCouldBeAssignedByLicenseIdSpecifiedTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertLicenseAssignedSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpStatusCode = 200,
            licenseId = license.licenseId!!,
            expectedAssigneeType = AssigneeType.USER,
            expectedAssigneeName = "Roman Loskutov",
            expectedAssigneeEmail = requestBody.contact?.email!!
        )
        softAssert.assertAll()
    }

    @Test
    fun licenseCouldBeAssignedWithoutOfflineActivationCodeTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = false,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertLicenseAssignedSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpStatusCode = 200,
            licenseId = license.licenseId!!,
            expectedAssigneeType = AssigneeType.USER,
            expectedAssigneeName = "Roman Loskutov",
            expectedAssigneeEmail = requestBody.contact?.email!!
        )
        softAssert.assertAll()
    }

    @Test
    fun nameFromJBAccountIsUsedWhenLicenseAssignedTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "It Could Be",
                lastName = "Any Name",
            ),
            includeOfflineActivationCode = false,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertLicenseAssignedSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpStatusCode = 200,
            licenseId = license.licenseId!!,
            expectedAssigneeType = AssigneeType.USER,
            expectedAssigneeName = "Roman Loskutov",
            expectedAssigneeEmail = requestBody.contact?.email!!
        )
        softAssert.assertAll()
    }

    @Test
    fun shortNameCouldBeUsedWhenLicenseAssignedTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "${UUID.randomUUID()}@gmail.com",
                firstName = "Ro",
                lastName = "Lo",
            ),
            includeOfflineActivationCode = false,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertLicenseAssignedSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpStatusCode = 200,
            licenseId = license.licenseId!!,
            expectedAssigneeType = AssigneeType.USER,
            expectedAssigneeName = "R* L*",
            expectedAssigneeEmail = requestBody.contact?.email!!
        )
        softAssert.assertAll()
    }

    @Test
    fun evenShorterNameCouldBeUsedWhenLicenseAssignedTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "${UUID.randomUUID()}@gmail.com",
                firstName = "R",
                lastName = "L",
            ),
            includeOfflineActivationCode = false,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertLicenseAssignedSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpStatusCode = 200,
            licenseId = license.licenseId!!,
            expectedAssigneeType = AssigneeType.USER,
            expectedAssigneeName = "* *",
            expectedAssigneeEmail = requestBody.contact?.email!!
        )
        softAssert.assertAll()
    }

    @Test
    fun licenseCouldBeAssignedByTeamAndProductSpecifiedTest() {
        val licenseBefore = selectLicenseToAssignFromTeam1()

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

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        softAssert.assertEquals(
            response.code(), 200,
            "Status code in response for assign license differs from expected"
        )

        softAssert.assertNotNull(response.body(), "Response body expected to be not null.")

        softAssert.assertAll()
    }

    @Test
    fun productAndTeamIgnoredIfLicenseIdSpecifiedTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            license = License(
                productCode = UUID.randomUUID().toString(),
                team = Int.MAX_VALUE
            ),
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertLicenseAssignedSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpStatusCode = 200,
            licenseId = license.licenseId!!,
            expectedAssigneeType = AssigneeType.USER,
            expectedAssigneeName = "Roman Loskutov",
            expectedAssigneeEmail = requestBody.contact?.email!!
        )
        softAssert.assertAll()
    }

    @Test
    fun unableToAssignLicenseIfNoAvailableLicensesTest() {
        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            license = License(
                productCode = "II",
                team = teamEmptyId
            ),
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "NO_AVAILABLE_LICENSE_TO_ASSIGN",
            expectedDescription = "No available license found to assign in the team $teamEmptyId with product II"
        )

        softAssert.assertAll()
    }

    @Test
    fun unableToAssignNonExistingLicenseTest() {
        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = UUID.randomUUID().toString(),
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "LICENSE_NOT_FOUND",
            expectedDescription = "${requestBody.licenseId}"
        )

        softAssert.assertAll()
    }

    @Test
    fun unableToAssignLicenseIfTeamNotExistsTest() {
        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            license = License(
                productCode = "WS",
                team = Int.MAX_VALUE
            ),
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "TEAM_NOT_FOUND",
            expectedDescription = "${requestBody.license!!.team!!}"
        )

        softAssert.assertAll()
    }

    @Test
    fun unableToAssignAlreadyAssignedLicenseTest() {
        val licenseBefore = selectLicenseToAssignFromTeam1()

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

        val firstAssignResponse = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        if (!firstAssignResponse.isSuccessful) {
            throw AssertionError("Failed to assign license: ${firstAssignResponse.code()} ${firstAssignResponse.errorBody()}")
        }

        val secondAssignResponse = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = secondAssignResponse,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN",
            expectedDescription = "ALLOCATED"
        )

        softAssert.assertAll()
    }

    @Test
    fun unableToAssignExpiredLicenseTest() {
        val licenseBefore = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byOutdatedState(true),
                byTeamId(team1Id),
                byOutdatedState(true)
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

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN",
            expectedDescription = "EXPIRED_WITHOUT_FALLBACK"
        )

        softAssert.assertAll()
    }

    @Test
    fun unableToAssignPerpetualLicenseTest() {
        val licenseBefore = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byTeamId(team1Id),
                byPerpetualProperty(true)
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

        ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()
        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "LICENSE_IS_NOT_AVAILABLE_TO_ASSIGN",
            expectedDescription = "NON_PER_USER"
        )

        softAssert.assertAll()
    }
}