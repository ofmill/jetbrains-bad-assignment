package assignment.models

data class LicenseResponse(
    val assignee: Assignee? = null,
    val domain: String? = null,
    val isAvailableToAssign: Boolean? = null,
    val isSuspended: Boolean? = null,
    val isTransferableBetweenTeams: Boolean? = null,
    val isTrial: Boolean? = null,
    val lastSeen: LastSeen? = null,
    val licenseId: String? = null,
    val perpetual: Perpetual? = null,
    val product: Product? = null,
    val subscription: Subscription? = null,
    val team: Team? = null,
)

data class Assignee(
    val email: String? = null,
    val name: String? = null,
    val type: AssigneeType? = null,
    val serverType: AssigneeServerType? = null,
    val uid: String? = null,
    val key: String? = null,
    val legacyKey: String? = null,
    val registrationName: String? = null,
)

enum class AssigneeType {
    USER,
    SERVER,
    LICENSE_KEY
}

enum class AssigneeServerType {
    LICENSE_SERVER,
    LICENSE_VAULT
}

data class LastSeen(
    val isOfflineCodeGenerated: Boolean? = null,
    val lastAssignmentDate: String? = null,
    val lastSeenDate: String? = null,
)

data class Perpetual(
    val isOutdated: Boolean? = null,
    val upgradeDueDate: String? = null,
)

data class Product(
    val code: String? = null,
    val name: String? = null,
)

data class Subscription(
    val isAutomaticallyRenewed: Boolean? = null,
    val isOutdated: Boolean? = null,
    val subscriptionPackRef: String? = null,
    val validUntilDate: String? = null,
)

data class Team(
    val id: Int? = null,
    val name: String? = null,
)
