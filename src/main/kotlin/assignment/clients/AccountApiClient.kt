package assignment.clients

import assignment.helpers.allure.AllureReportHttpInterceptor
import assignment.models.ChangeLicensesTeamRequestBody
import assignment.models.LicenseAssignRequestBody
import assignment.models.LicenseResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * API Reference - https://account.jetbrains.com/openapi
 */
interface AccountApiClient {
    @POST("api/v1/customer/licenses/assign")
    fun assignLicense(@Body body: LicenseAssignRequestBody): Call<Any?>

    @POST("api/v1/customer/changeLicensesTeam")
    fun changeLicensesTeam(@Body body: ChangeLicensesTeamRequestBody): Call<Any?>

    @GET("api/v1/customer/licenses")
    fun getLicenses(
        @Query("productCode") productCode: String? = null,
        @Query("page") page: Int? = null,
        @Query("perPage") perPage: Int? = null,
        @Query("assigned") assigned: Boolean? = null,
        @Query("assigneeEmail") assigneeEmail: String? = null,
    ): Call<List<LicenseResponse>>

    @GET("api/v1/customer/licenses/{licenseId}")
    fun getLicenseById(
        @Path("licenseId") licenseId: String? = null,
    ): Call<LicenseResponse>

    companion object {
        @JvmStatic
        fun withHeaders(apiKey: String?, customerCode: String?): AccountApiClient {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()

                    customerCode?.let { requestBuilder.addHeader("X-Customer-Code", it) }
                    apiKey?.let { requestBuilder.addHeader("X-API-Key", it) }

                    chain.proceed(requestBuilder.build())
                }
                .addInterceptor(AllureReportHttpInterceptor())
                .build()

            val gsonConverterFactory = GsonConverterFactory.create()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://account.jetbrains.com/")
                .addConverterFactory(NullOnEmptyConverterFactory(gsonConverterFactory))
                .addConverterFactory(gsonConverterFactory)
                .client(client)
                .build()

            return retrofit.create(AccountApiClient::class.java)
        }
    }
}