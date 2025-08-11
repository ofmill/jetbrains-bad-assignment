package assignment.models

data class ChangeLicensesTeamRequestBody(
    val licenseIds: List<String?>? = null,
    val targetTeamId: Int? = null
)
