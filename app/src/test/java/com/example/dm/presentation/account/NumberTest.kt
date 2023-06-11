package com.example.dm.presentation.account

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.dm.presentation.account.Number

class NumberTest{

    lateinit var number: Number
    @Before
    fun setUp() {
        number = Number()
    }

    @Test
    fun testEmptyEditText() {
        val editText = ""

        val result = number.checkEditText(editText)

        assertFalse(result)
    }


    @Test
    fun testValidEditText() {

        val editText = "8766516233"

        val result = number.checkEditText(editText)

        assertTrue(result)

    }

    @Test
    fun testInvalidTest() {
        val editText = "8464"

        val result = number.checkEditText(editText)

        assertFalse(result)
    }
}