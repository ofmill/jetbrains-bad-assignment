package assignment

import assignment.helpers.accounts.byAbilityToAssign
import assignment.helpers.accounts.byAbilityToTransfer
import assignment.helpers.accounts.byAssigneePresence
import assignment.helpers.accounts.byTeamId
import assignment.helpers.allure.AllureReportHelper
import assignment.helpers.allure.AllureReportHelper.step
import assignment.models.LicenseResponse

abstract class AbstractAccountsTests {
    val team1Id = 2573297
    val team2Id = 2717496
    val licenseServerTeamId = 2046529
    val teamEmptyId = 2733494

    protected fun selectLicenseToAssignFromTeam1(): LicenseResponse {
        return step("Select license to assign from Team 1") {
            val license = ApiClients.orgAdminAccountsApiHelper
                .selectFirstMatchingLicense(
                    byTeamId(team1Id),
                    byAbilityToAssign(true)
                )
            AllureReportHelper.attachJson("License ${license.licenseId}", license.toString())

            return@step license
        }
    }

    protected fun selectLicenseToTransfer(fromTeamId: Int): LicenseResponse {
        return step("Select license to assign from Team 1") {
            val license = ApiClients.orgAdminAccountsApiHelper
                .selectFirstMatchingLicense(
                    byAssigneePresence(false),
                    byAbilityToTransfer(true),
                    byTeamId(fromTeamId)
                )
            AllureReportHelper.attachJson("License ${license.licenseId}", license.toString())

            return@step license
        }
    }
}