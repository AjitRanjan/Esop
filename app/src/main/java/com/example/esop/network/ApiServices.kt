package com.example.esop.network


import com.example.esop.FunctionaryDropdown.AssignRoleResponse
import com.example.esop.OrgnazationDropdown.RoleResponse
import com.example.esop.ProcessGroup.ProcessGroupItem
import com.example.esop.district.DistrictResponse
//import com.example.esop.ProcessGroup.ProcessGroupResponse
import com.example.esop.login.LoginRequest
import com.example.esop.login.LoginResponse
import com.example.esop.profile.ProfileRequest
import com.example.esop.profile.ProfileResponse
import com.example.esop.profile.UpdateProfileResponse
import com.example.esop.profile.request.UpadteProfileRequest
import com.example.esop.quetions_esop.QuestionResponse
import com.example.esop.token.GetToken
import com.example.esop.token.TokenRequest
import com.example.esop.state.StateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import signup.SignupResponse
import signup.request.SignupRequest

interface ApiServices {





    @POST("esop/api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>




    @POST("esop/api/signup")
    suspend fun signupUser(
        @Body request: SignupRequest
    ): SignupResponse




    @GET("esop/api/admin-levels")
    suspend fun getProcessGroupList(): List<ProcessGroupItem>

    @GET("esop/api/roles/{id}")
    suspend fun getRoles(@Path("id") id: String): RoleResponse




//   https://kaushal.dord.gov.in/demobackend/esop/api/exam


    @GET("esop/api/assign-roles/{orgId}")
    suspend fun getFunctionaries(

        @Path("orgId") orgId: String
    ): AssignRoleResponse

//    @GET("esop/api/state")
//    suspend fun getStateList(): List<Item>



    @GET("esop/api/state")
    suspend fun getStateList(): StateResponse



    @GET("esop/api/distict")
    suspend fun getDistrict(
        @Query("statecode") statecode: String
    ): DistrictResponse


    @GET("esop/api/getinsertProfile")
    suspend fun insertProfile(
        @Query("statecode") statecode: String
    ): DistrictResponse


    @POST("esop/api/generateToken")
    suspend fun getToken(
        @Body request: TokenRequest
    ): Response<GetToken>

    @POST("esop/api/getinsertProfile")
    suspend fun getProfile(
//        @Header("Authorization") token: String,
        @Body request: ProfileRequest
    ): Response<ProfileResponse>

    @POST("esop/api/insertProfile")
    suspend fun UpdateUser(
        @Body request: UpadteProfileRequest
    ): UpdateProfileResponse


    @GET("esop/api/exam/{id}")
    suspend fun getQuestions(
        @Path("id") id: String
    ): QuestionResponse
}