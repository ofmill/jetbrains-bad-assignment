package assignment.licenses.assign

import assignment.AbstractAccountsTests
import assignment.ApiClients
import assignment.assertGeneralErrorResponse
import assignment.assertLicenseHasNoAssignee
import assignment.helpers.accounts.byAbilityToAssign
import assignment.helpers.accounts.byTeamId
import assignment.models.Contact
import assignment.models.License
import assignment.models.LicenseAssignRequestBody
import org.testng.annotations.Test
import org.testng.asserts.SoftAssert

class AssignLicensesSchemaTests : AbstractAccountsTests() {
    @Test
    fun emailIsMandatoryTest() {
        val license = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byTeamId(team1Id),
                byAbilityToAssign(true)
            )

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response, softAssert,
            400,
            "CODE_TBD",
            "DESCRIPTION_TBD"
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }

    @Test
    fun firstNameIsMandatoryTest() {
        val license = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byTeamId(team1Id),
                byAbilityToAssign(true)
            )

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = true,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response, softAssert,
            400,
            "CODE_TBD",
            "DESCRIPTION_TBD"
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }

    @Test
    fun lastNameIsMandatoryTest() {
        val license = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byTeamId(team1Id),
                byAbilityToAssign(true)
            )

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
            ),
            includeOfflineActivationCode = true,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response, softAssert,
            400,
            "CODE_TBD",
            "DESCRIPTION_TBD"
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }

    @Test
    fun contactIsMandatoryTest() {
        val license = ApiClients.orgAdminAccountsApiHelper
            .selectFirstMatchingLicense(
                byTeamId(team1Id),
                byAbilityToAssign(true)
            )

        val requestBody = LicenseAssignRequestBody(
            includeOfflineActivationCode = true,
            license = License(
                productCode = license.product?.code,
                team = license.team?.id
            ),
            licenseId = license.licenseId,
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response, softAssert,
            400,
            "CODE_TBD",
            "DESCRIPTION_TBD"
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }

    @Test
    fun sendEmailIsMandatoryTest() {
        val license = ApiClients.orgAdminAccountsApiHelper
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
            license = License(
                productCode = license.product?.code,
                team = license.team?.id
            ),
            licenseId = license.licenseId,
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response, softAssert,
            400,
            "CODE_TBD",
            "DESCRIPTION_TBD"
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }

    @Test
    fun productCodeIsMandatoryTest() {
        val license = ApiClients.orgAdminAccountsApiHelper.selectFirstMatchingLicense(
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
            license = License(
                team = license.team?.id
            )
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response, softAssert,
            400,
            "CODE_TBD",
            "DESCRIPTION_TBD"
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }

    @Test
    fun teamIsMandatoryTest() {
        val license = ApiClients.orgAdminAccountsApiHelper.selectFirstMatchingLicense(
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
            license = License(
                productCode = license.product?.code,
            )
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "TEAM_NOT_FOUND",
            expectedDescription = "Some informative description"
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }

    @Test
    fun firstNameCouldNotBeEmptyTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = false,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "INVALID_CONTACT_NAME",
            expectedDescription = "This field can't be empty."
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }

    @Test
    fun lastNameCouldNotBeEmptyTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "ofmgreen@gmail.com",
                firstName = "Roman",
                lastName = "",
            ),
            includeOfflineActivationCode = false,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()
        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "INVALID_CONTACT_NAME",
            expectedDescription = "This field can't be empty."
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }

    @Test
    fun emailCouldNotBeEmptyTest() {
        val license = selectLicenseToAssignFromTeam1()

        val requestBody = LicenseAssignRequestBody(
            contact = Contact(
                email = "",
                firstName = "Roman",
                lastName = "Loskutov",
            ),
            includeOfflineActivationCode = false,
            licenseId = license.licenseId,
            sendEmail = false
        )

        val response = ApiClients.orgAdminAccountApiClient.assignLicense(requestBody).execute()

        val softAssert = SoftAssert()

        assertGeneralErrorResponse(
            response = response,
            softAssert = softAssert,
            expectedHttpCode = 400,
            expectedCode = "INVALID_CONTACT_EMAIL",
            expectedDescription = "Email field can't be empty."
        )
        assertLicenseHasNoAssignee(softAssert, license.licenseId!!)
        softAssert.assertAll()
    }
}