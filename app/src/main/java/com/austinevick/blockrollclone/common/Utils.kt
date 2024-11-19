package com.austinevick.blockrollclone.common

import android.util.Patterns

fun validateEmail(email: String): String {
    if (!Patterns.EMAIL_ADDRESS
            .matcher(email.trim()).matches()
    ){
        return "Enter a valid email"
    }
   if (email.isBlank()){
       return "Field cannot be empty"
   }
    return ""
}
fun isValidEmail(email: String):Boolean{
    return Patterns.EMAIL_ADDRESS
            .matcher(email.trim()).matches()
}
fun isValidPassword(password:String):Boolean{
    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!/%*?+=_^.&])[A-Za-z\\d@\$!%*?+=_^.&]{8,}\$")
    return regex.containsMatchIn(password)
}

fun validateField(value: String): String {
    if (value.isBlank()) return "Field cannot be empty"
    return ""
}