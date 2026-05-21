package com.example.esop.network


import com.example.esop.FunctionaryDropdown.AssignRoleResponse
import com.example.esop.OrgnazationDropdown.RoleResponse
import com.example.esop.ProcessGroup.ProcessGroupItem
//import com.example.esop.ProcessGroup.ProcessGroupResponse
import com.example.esop.login.LoginRequest
import com.example.esop.login.LoginResponse
import com.example.esop.state.StateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url
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




//    @GET("esop/api/assign-roles/{processId}/{orgId}")
    @GET("esop/api/assign-roles/{orgId}")
    suspend fun getFunctionaries(

        @Path("orgId") orgId: String
    ): AssignRoleResponse

//    @GET("esop/api/state")
//    suspend fun getStateList(): List<Item>



    @GET("esop/api/state")
    suspend fun getStateList(): StateResponse
}