package com.austinevick.blockrollclone.data.repository

import com.austinevick.blockrollclone.data.model.GeneralResponseModel
import com.austinevick.blockrollclone.data.model.auth.LoginModel
import com.austinevick.blockrollclone.data.model.auth.PasscodeModel
import com.austinevick.blockrollclone.data.model.auth.RegisterModel
import com.austinevick.blockrollclone.data.model.auth.UsernameModel
import com.austinevick.blockrollclone.data.model.auth.ValidateEmailModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthRepository {

    @POST("/auth/login")
    suspend fun login(
        @Body loginModel: LoginModel
    ): Response<GeneralResponseModel>

    @POST("/auth/sign-up")
    suspend fun signup(
        @Body registerModel: RegisterModel
    ): Response<GeneralResponseModel>

    @POST("/auth/otp/verify?withToken=true")
    suspend fun validateEmail(
        @Body validateEmailModel: ValidateEmailModel
    ): Response<GeneralResponseModel>

    @GET("/user/check-username-availablity")
    suspend fun checkUsernameAvailability(
        @Query("username") username: String
    ):Response<GeneralResponseModel>

    @POST("/user/username")
    suspend fun createUsername(
        @Body usernameModel: UsernameModel
    ): Response<GeneralResponseModel>

    @POST("/user/create-passcode")
    suspend fun createPasscode(
        @Body passcodeModel: PasscodeModel
    ): Response<GeneralResponseModel>


}