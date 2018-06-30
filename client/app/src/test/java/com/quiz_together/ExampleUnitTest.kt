package com.quiz_together

import android.util.Log
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val TAG = "ExampleUnitTest"

    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)

        val aaa:String? = "asdf"
//        aaa?.let {
//            print("AA")
//        }?:let {
//            print("BB")
//        }

        aaa?.run {
            print("CC")
        }?:run {
            print("DD")
        }


    }
}
