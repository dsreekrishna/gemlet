package com.mobiletracker.app.data.api

import com.mobiletracker.app.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/v1/send-otp/")
    suspend fun sendOtp(@Body otpUser: OtpUser): Response<ApiResponse<String>>

    @POST("api/v1/signin/")
    suspend fun signin(@Body signinUser: SigninUser): Response<RefreshTokenResponse>

    @POST("api/v1/refresh-token/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

    @GET("api/v1/csrf-token")
    suspend fun getCsrfToken(): Response<Map<String, String>>

    @POST("api/v1/profiles/")
    suspend fun createProfile(
        @Header("Authorization") token: String,
        @Body profile: Profile
    ): Response<Profile>

    @GET("api/v1/profiles/")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<Profile>

    @PUT("api/v1/profiles/")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body profile: Profile
    ): Response<Profile>

    @POST("api/v1/groups/")
    suspend fun createGroup(
        @Header("Authorization") token: String,
        @Body group: Map<String, Any>
    ): Response<Group>

    @GET("api/v1/groups/admin/")
    suspend fun getGroups(
        @Header("Authorization") token: String
    ): Response<List<Group>>

    @GET("api/v1/groups/{group_id}")
    suspend fun getGroup(
        @Header("Authorization") token: String,
        @Path("group_id") groupId: Int
    ): Response<Group>

    @GET("api/v1/group-members/{group_id}")
    suspend fun getGroupMembers(
        @Header("Authorization") token: String,
        @Path("group_id") groupId: Int
    ): Response<List<GroupMember>>

    @PUT("api/v1/group-members/{member_id}/location")
    suspend fun updateLocation(
        @Header("Authorization") token: String,
        @Path("member_id") memberId: Int,
        @Body location: Map<String, Double>
    ): Response<GroupMember>
}