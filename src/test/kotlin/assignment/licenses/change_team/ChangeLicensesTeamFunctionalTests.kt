package assignment.licenses.change_team

import assignment.ApiClients
import assignment.assertGeneralErrorResponse
import assignment.assertLicenseTransferredSuccessfully
import assignment.helpers.accounts.*
import assignment.helpers.allure.AllureReportHelper.step
import assignment.models.ChangeLicensesTeamRequestBody
import assignment.models.Contact
import assignment.models.LicenseAssignRequestBody
import org.testng.annotations.Test
import org.testng.asserts.SoftAssert
import java.util.*

class ChangeLicensesTeamFunctionalTests : AbstractChangeLicensesTeamTests() {
    @Test(description = "Single unassigned license can can be transferred to another team")
    fun singleUnassignedLicenseCanBeTransferredTest() {
        val licenseBefore = selectLicenseToTransfer(team2Id)

        val body = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(licenseBefore.licenseId),
            targetTeamId = team1Id
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(body).execute()
        licensesToTransferBack.add(Pair(licenseBefore, team2Id))

        val softAssert = SoftAssert()
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = licenseBefore.licenseId!!,
            expectedTeamId = team1Id
        )
        softAssert.assertAll()
    }

    @Test
    fun severalUnassignedLicensesCouldBeTransferredTest() {
        val licenses = step("Select all licenses to assign from Team 2") {
            ApiClients.orgAdminAccountsApiHelper
                .selectLicenses(
                    byAssigneePresence(false),
                    byAbilityToTransfer(true),
                    byTeamId(team2Id)
                )
        }

        if (licenses.size < 2) {
            throw AssertionError("Not enough licenses to perform test")
        }

        val license1 = licenses[0]
        val license2 = licenses[1]

        val body = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(license1.licenseId, license2.licenseId),
            targetTeamId = team1Id
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(body).execute()
        licensesToTransferBack.add(Pair(license1, team2Id))
        licensesToTransferBack.add(Pair(license2, team1Id))

        val softAssert = SoftAssert()
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = license1.licenseId!!,
            expectedTeamId = team1Id
        )
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = license2.licenseId!!,
            expectedTeamId = team1Id
        )
        softAssert.assertAll()
    }

    @Test
    fun unableToTransferLicenseWithNoTeamSpecifiedTest() {
        val licenseBefore = selectLicenseToTransfer(team2Id)

        val body = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(licenseBefore.licenseId),
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(body).execute()
        licensesToTransferBack.add(Pair(licenseBefore, team2Id))

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "TEAM_NOT_FOUND",
            expectedDescription = "Some informative description"
        )

        val licenseAfter = ApiClients.orgAdminAccountsApiHelper.getLicenseById(licenseBefore.licenseId!!)
        softAssert.assertEquals(licenseAfter.team?.id, team2Id)

        softAssert.assertAll()
    }

    @Test
    fun licenseTransferToTheSameTeamTest() {
        val license = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byAssigneePresence(false),
                byAbilityToTransfer(true),
                byTeamId(team2Id)
            )

        val body = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(license.licenseId),
            targetTeamId = team2Id
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(body).execute()
        licensesToTransferBack.add(Pair(license, team2Id))

        val softAssert = SoftAssert()
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = license.licenseId!!,
            expectedTeamId = team2Id
        )
        softAssert.assertAll()
    }

    @Test
    fun possibleToTransferAssignedLicenseToAnotherTeamTest() {
        val license = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byAbilityToAssign(true),
                byAbilityToTransfer(true),
                byTeamId(team2Id)
            )

        val assignRequestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val assignResponse = ApiClients.orgAdminAccountApiClient.assignLicense(assignRequestBody).execute()
        if (!assignResponse.isSuccessful) {
            throw AssertionError("Failed to get assigned license for test.")
        }

        val transferRequestBody = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(license.licenseId),
            targetTeamId = team1Id
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(transferRequestBody).execute()
        licensesToTransferBack.add(Pair(license, team2Id))

        val softAssert = SoftAssert()
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = license.licenseId!!,
            expectedTeamId = team1Id
        )
        softAssert.assertAll()
    }

    @Test
    fun possibleToTransferExpiredLicenseToAnotherTeamTest() {
        val license = step("Select expired license from Team 2") {
            ApiClients.orgAdminAccountsApiHelper
                .selectFirstMatchingLicense(
                    byOutdatedState(true),
                    byAbilityToTransfer(true),
                    byTeamId(team2Id)
                )
        }

        val requestBody = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(license.licenseId),
            targetTeamId = team1Id
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(requestBody).execute()
        licensesToTransferBack.add(Pair(license, team2Id))

        val softAssert = SoftAssert()
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = license.licenseId!!,
            expectedTeamId = team1Id
        )
        softAssert.assertAll()
    }

    @Test
    fun possibleToTransferActiveLicenseToServerTeamTest() {
        val license = selectLicenseToTransfer(team2Id)

        val requestBody = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(license.licenseId),
            targetTeamId = licenseServerTeamId
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(requestBody).execute()
        licensesToTransferBack.add(Pair(license, team2Id))

        val softAssert = SoftAssert()
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = license.licenseId!!,
            expectedTeamId = licenseServerTeamId
        )
        softAssert.assertAll()
    }

    @Test
    fun possibleToTransferActiveLicenseFromServerTeamTest() {
        val license = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byAbilityToTransfer(true),
                byTeamId(licenseServerTeamId)
            )

        val requestBody = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(license.licenseId),
            targetTeamId = team2Id
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(requestBody).execute()
        licensesToTransferBack.add(Pair(license, licenseServerTeamId))

        val softAssert = SoftAssert()
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = license.licenseId!!,
            expectedTeamId = team2Id
        )
        softAssert.assertAll()
    }

    @Test
    fun possibleToTransferActiveLicensesFromDifferentTeamsTest() {
        val license1 = selectLicenseToTransfer(team1Id)

        val license2 = step("Select license to transfer from Team 2") {
            ApiClients.orgAdminAccountsApiHelper
                .selectFirstMatchingLicense(
                    byOutdatedState(false),
                    byAbilityToTransfer(true),
                    byTeamId(team2Id)
                )
        }

        val requestBody = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(license1.licenseId, license2.licenseId),
            targetTeamId = licenseServerTeamId
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(requestBody).execute()
        licensesToTransferBack.add(Pair(license1, team1Id))
        licensesToTransferBack.add(Pair(license2, team2Id))

        val softAssert = SoftAssert()
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = license1.licenseId!!,
            expectedTeamId = licenseServerTeamId
        )
        assertLicenseTransferredSuccessfully(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 200,
            licenseId = license2.licenseId!!,
            expectedTeamId = licenseServerTeamId
        )
        softAssert.assertAll()
    }

    @Test
    fun unableToTransferLicenseToNotExistingTeamTest() {
        val licenseBefore = selectLicenseToTransfer(team2Id)

        val body = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(licenseBefore.licenseId),
            targetTeamId = Int.MAX_VALUE
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(body).execute()
        licensesToTransferBack.add(Pair(licenseBefore, team2Id))

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "TEAM_NOT_FOUND",
            expectedDescription = "Some informative description"
        )

        val licenseAfter = ApiClients.orgAdminAccountsApiHelper.getLicenseById(licenseBefore.licenseId!!)
        softAssert.assertEquals(licenseAfter.team?.id, team2Id)

        softAssert.assertAll()
    }

    @Test
    fun unableToTransferNotExistingLicenseTest() {
        val body = ChangeLicensesTeamRequestBody(
            licenseIds = listOf(UUID.randomUUID().toString()),
            targetTeamId = team2Id
        )

        val response = ApiClients.orgAdminAccountApiClient.changeLicensesTeam(body).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "LICENSE_NOT_FOUND",
            expectedDescription = "Some informative description with license ID ${body.licenseIds?.get(0)}"
        )

        softAssert.assertAll()
    }
}