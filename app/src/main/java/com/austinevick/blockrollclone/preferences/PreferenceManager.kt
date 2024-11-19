package com.austinevick.blockrollclone.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    private val preference: SharedPreferences
) {
    private val userId: String = "userId"
    private val token: String = "token"
    private val username: String = "username"
    private val hasPasscode: String = "hasPasscode"

    /////-------- Token -------///////
    fun saveToken(value: String) {
        val editor = preference.edit()
        editor.putString(token, value)
        editor.apply()
    }

    fun getToken(): String {
        return preference.getString(token, "") ?: ""
    }

    /////-------- UserId -------///////
    fun saveUserId(value: String) {
        val editor = preference.edit()
        editor.putString(userId, value)
        editor.apply()
    }

    fun getUserId(): String {
        return preference.getString(userId, "") ?: ""
    }

    /////-------- PasscodeValue -------///////
    fun saveUserPasscodeValue(value: Boolean) {
        val editor = preference.edit()
        editor.putBoolean(hasPasscode, value)
        editor.apply()
    }

    fun getUserPasscodeValue(): Boolean {
        return preference.getBoolean(hasPasscode, false)
    }

    /////-------- Username -------///////
    fun saveUsername(value: String) {
        val editor = preference.edit()
        editor.putString(username, value)
        editor.apply()
    }

    fun getUsername(): String {
        return preference.getString(username, "") ?: ""
    }


}