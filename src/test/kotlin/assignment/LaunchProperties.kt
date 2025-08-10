package assignment

val customerCode = System.getenv("CUSTOMER_CODE")
    ?: throw AssertionError("CUSTOMER_CODE env var not set")

val orgAdminApiKey = System.getenv("ORG_ADMIN_API_KEY")
    ?: throw AssertionError("ORG_ADMIN_API_KEY env var not set")


val team1AdminApiKey = System.getenv("TEAM_1_ADMIN_API_KEY")
    ?: throw AssertionError("TEAM_1_ADMIN_API_KEY env var not set")


val team1ViewerApiKey = System.getenv("TEAM_1_VIEWER_API_KEY")
    ?: throw AssertionError("TEAM_1_VIEWER_API_KEY env var not set")