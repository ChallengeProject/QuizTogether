package com.quiz_together

import org.junit.Test

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

        val aaa:String? = null
//        aaa?.let {
//            print("AA")
//        }?:let {
//            print("BB")
//        }

        aaa?.run {
            print("call when value is not null")
        }?:run {
            print("call when value is null")
        }


    }





}
