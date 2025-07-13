
package com.mobiletracker.app.data.models

data class OtpUser(
    val mobile_country_code: String,
    val mobile_number: String
)

data class SigninUser(
    val mobile_country_code: String,
    val mobile_number: String,
    val otp: String
)

data class RefreshTokenRequest(
    val refresh_token: String
)

data class RefreshTokenResponse(
    val refresh_token: String,
    val mobile_country_code: String,
    val mobile_number: String,
    val access_token: String,
    val token_type: String
)

data class Profile(
    val firstname: String? = null,
    val lastname: String? = null,
    val mobile_country_code: String = "+91",
    val mobile_number: String = "0",
    val gender: String? = null,
    val dob: String? = null,
    val age: Int? = 0,
    val active: Boolean? = true,
    val user_id: Int? = 0
)

data class Group(
    val id: Int,
    val group_name: String,
    val group_display_name: String,
    val admin_profile_id: Int
)

data class GroupMember(
    val id: Int,
    val group_id: Int,
    val group_display_name: String,
    val profile_id: Int,
    val firstname: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val updated_on: String,
    val created_on: String
)

data class Invitation(
    val id: Int,
    val group_id: Int,
    val group_name: String,
    val sender_profile_id: Int,
    val receiver_name: String,
    val receiver_mobile_country_code: String,
    val receiver_mobile_number: String,
    val status: String,
    val created_on: String,
    val updated_on: String
)

data class InvitationCreate(
    val group_id: Int,
    val receiver_mobile_country_code: String,
    val receiver_mobile_number: String,
    val receiver_name: String
)

data class InvitationAction(
    val id: Int,
    val status: String
)

data class GroupCreate(
    val group_name: String
)

data class GroupMemberLocationUpdate(
    val latitude: Double,
    val longitude: Double
)

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T? = null
)
