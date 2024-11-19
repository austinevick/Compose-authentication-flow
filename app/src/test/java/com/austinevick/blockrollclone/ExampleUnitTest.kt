package com.austinevick.blockrollclone

import com.austinevick.blockrollclone.common.isValidEmail
import com.austinevick.blockrollclone.common.isValidPassword
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
   fun validatePassword(){
       assertTrue(isValidPassword("Victor123@"))
   }
}