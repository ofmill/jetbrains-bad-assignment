package assignment.models

data class ChangeLicensesTeamRequest(
    val licenseIds: List<String?>? = null,
    val targetTeamId: Int? = null
)
