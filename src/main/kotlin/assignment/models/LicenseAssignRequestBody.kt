package assignment.models

data class LicenseAssignRequestBody(
    val contact: Contact? = null,
    val includeOfflineActivationCode: Boolean? = null,
    val license: License? = null,
    val licenseId: String? = null,
    val sendEmail: Boolean? = null
)

data class Contact(
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
)

data class License(
    val productCode: String? = null,
    val team: Int? = null,
)