package assignment

import assignment.clients.AccountApiClient
import assignment.helpers.accounts.AccountApiHelper

object ApiClients {
    val orgAdminAccountApiClient = AccountApiClient.withHeaders(orgAdminApiKey, customerCode)

    val team1AdminAccountApiClient = AccountApiClient.withHeaders(team1AdminApiKey, customerCode)

    val team1ViewerAccountApiClient = AccountApiClient.withHeaders(team1ViewerApiKey, customerCode)

    val orgAdminAccountsApiHelper = AccountApiHelper(orgAdminAccountApiClient)
}