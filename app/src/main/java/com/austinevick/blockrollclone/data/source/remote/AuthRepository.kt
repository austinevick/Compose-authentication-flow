package com.austinevick.blockrollclone.data.source.remote

import com.austinevick.blockrollclone.data.model.GeneralResponseModel
import com.austinevick.blockrollclone.data.model.auth.GenerateTrustTokenModel
import com.austinevick.blockrollclone.data.model.auth.LoginModel
import com.austinevick.blockrollclone.data.model.auth.PasscodeLoginModel
import com.austinevick.blockrollclone.data.model.auth.PasscodeModel
import com.austinevick.blockrollclone.data.model.auth.RegisterModel
import com.austinevick.blockrollclone.data.model.auth.TrustTokenResponseModel
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

    @POST("/auth/generate-trust-token")
    suspend fun generateTrustToken(
        @Body generateTrustTokenModel: GenerateTrustTokenModel
    ): Response<TrustTokenResponseModel>

    @POST("/auth/passcode-login")
    suspend fun passcodeLogin(
        @Body passcodeLoginModel: PasscodeLoginModel
    ): Response<GeneralResponseModel>

    @POST("/auth/biometric-login")
    suspend fun biometricLogin(
        @Body passcodeLoginModel: PasscodeLoginModel
    ): Response<GeneralResponseModel>

}