package assignment.licenses.change_team

import assignment.AbstractAccountsTests
import assignment.ApiClients
import assignment.helpers.allure.AllureReportHelper.step
import assignment.models.ChangeLicensesTeamRequestBody
import assignment.models.LicenseResponse
import org.testng.annotations.AfterMethod
import org.testng.asserts.SoftAssert
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

abstract class AbstractChangeLicensesTeamTests : AbstractAccountsTests() {
    protected val licensesToTransferBack: Queue<Pair<LicenseResponse, Int>> = LinkedBlockingQueue()

    @AfterMethod(
        description = "Transfer license used for test back to its team",
        alwaysRun = true
    )
    fun transferLicensesBack() {
        val softAssert = SoftAssert()
        while (licensesToTransferBack.isNotEmpty()) {
            val licenseAndTargetTeam = licensesToTransferBack.poll()
            val body = ChangeLicensesTeamRequestBody(
                licenseIds = listOf(licenseAndTargetTeam.first.licenseId),
                targetTeamId = licenseAndTargetTeam.second
            )
            val response = step(
                "transfer license ${licenseAndTargetTeam.first.licenseId} back to team ${licenseAndTargetTeam.second}"
            ) {
                ApiClients.orgAdminAccountApiClient.changeLicensesTeam(body).execute()
            }
            softAssert.assertTrue(
                response.isSuccessful,
                "Failed to transfer license ${licenseAndTargetTeam.first.licenseId} back to team ${licenseAndTargetTeam.second}"
            )
        }
        softAssert.assertAll()
    }
}