package assignment.helpers.accounts

import assignment.clients.AccountApiClient
import assignment.models.LicenseResponse
import java.util.function.Predicate

class AccountApiHelper(private val accountApiClient: AccountApiClient) {
    fun getLicenses(): List<LicenseResponse> {
        val response = accountApiClient.getLicenses().execute()
        if (!response.isSuccessful) {
            throw AssertionError("Failed to get list of licenses: ${response.code()} ${response.message()}")
        }

        return response.body() ?: throw AssertionError("Failed to get licenses - response body is null.")
    }

    fun selectLicenses(vararg predicates: Predicate<LicenseResponse>): List<LicenseResponse> {
        val allLicenses = getLicenses()
        if (predicates.isEmpty()) {
            return allLicenses
        }
        val combinedPredicate = predicates.reduce { accumulate, p -> accumulate.and(p) }
        return allLicenses.filter { combinedPredicate.test(it) }
    }

    fun selectFirstMatchingLicense(vararg predicates: Predicate<LicenseResponse>): LicenseResponse {
        return selectLicenses(*predicates).firstOrNull()
            ?: throw AssertionError("No license found by given predicates.")
    }

    fun getLicenseById(licenseId: String): LicenseResponse {
        val response = accountApiClient.getLicenseById(licenseId).execute()
        if (!response.isSuccessful) {
            throw AssertionError("Failed to get license by ID $licenseId: ${response.code()} ${response.message()}")
        }
        return response.body() ?: throw AssertionError("Failed to get license - response body is null.")
    }
}