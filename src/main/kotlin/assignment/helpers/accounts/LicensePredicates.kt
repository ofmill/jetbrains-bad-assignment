package assignment.helpers.accounts

import assignment.models.LicenseResponse
import java.util.function.Predicate

fun byTeamId(id: Int) = Predicate<LicenseResponse> {
    it.team?.id == id
}

fun byAbilityToAssign(isAvailableToAssign: Boolean) = Predicate<LicenseResponse> {
    it.isAvailableToAssign == isAvailableToAssign
}

fun byPerpetualProperty(isPerpetual: Boolean) = Predicate<LicenseResponse> {
    (it.perpetual != null) == isPerpetual
}

fun byAssigneePresence(hasAssignee: Boolean) = Predicate<LicenseResponse> {
    (it.assignee != null) == hasAssignee
}

fun byOutdatedState(isOutdated: Boolean) = Predicate<LicenseResponse> {
    it.subscription?.isOutdated == isOutdated
}

fun byAbilityToTransfer(isTransferable: Boolean) = Predicate<LicenseResponse> {
    it.isTransferableBetweenTeams == isTransferable
}
