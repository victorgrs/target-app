package com.target.android

import com.target.android.util.extensions.isEmail
import org.junit.Assert.assertEquals
import org.junit.Test

public class ValidationTests {
    @Test
    fun checkEmailTest() {
        assertEquals(true, "email@mkdi.com".isEmail())
        assertEquals(false, "email@mkdi".isEmail())
        assertEquals(false, "email".isEmail())
        assertEquals(false, "email.com".isEmail())
    }
}
