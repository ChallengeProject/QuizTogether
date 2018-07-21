package com.quiz_together

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.QuestionProp
import com.quiz_together.data.model.QustionList
import com.quiz_together.data.model.CategoryType
import com.quiz_together.data.model.GiftType
import com.quiz_together.data.model.UserRes
import com.quiz_together.data.remote.ApiHelper

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    val TAG = "Example#$#"

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.quiz_together", appContext.packageName)
    }

    fun testRunText(runText:String){
        println("###########################################")
        println("${runText} is START")
    }

    // tt : testText
    fun tt(textText:String) {
        println("### TESTRESULT : ${textText}")
    }

    @Test
    fun testSignup() {
        testRunText("testSignup")

        // bbb
        // u51746133bf56b26c7e0988465c5e8c31

        Repository.signup("aaa","dummy",object:ApiHelper.UserResCallback{
            override fun onLoginLoaded(respLogin: UserRes) {
                tt(respLogin.name)
                tt(respLogin.userId)
            }

            override fun onDataNotAvailable() {
                tt("onDataNotAvailable")

            }
        })
    }

    @Test
    fun testLogin() {
        testRunText("testLogin")

        Repository.login("u51746133bf56b26c7e0988465c5e8c31",object:ApiHelper.UserResCallback{
            override fun onLoginLoaded(respLogin: UserRes) {
                tt(respLogin.name)
                tt(respLogin.userId)
            }

            override fun onDataNotAvailable() {
                tt("onDataNotAvailable")
            }
        })
    }


    @Test
    fun testGetPagingBroadcastList(){
        testRunText("testGetPagingBroadcastList")

        Repository.getPagingBroadcastList(object:ApiHelper.GetBroadcastsCallback{
            override fun onBroadcastsLoaded(broadcasts: List<Broadcast>) {
                broadcasts.forEach{
                    tt(it.toString())
                }
                tt("onBroadcastsLoaded")
            }
            override fun onDataNotAvailable() {
                tt("onDataNotAvailable")
            }
        })

    }

    @Test
    fun testGetBroadcastForUpdateById(){
        testRunText("testGetBroadcastForUpdateById")

        println("###########################################")
        println("testGetBroadcastForUpdateById START")

        Repository.getBroadcastForUpdateById("b82d4e3b1873ef25f7264af5a2113f5a7",
                object : ApiHelper.GetBroadcastCallback{
                    override fun onBroadcastLoaded(broadcasts: Broadcast) {
                        tt(broadcasts.toString())
                    }

                    override fun onDataNotAvailable() {
                        tt("onDataNotAvailable")
                    }

                })



    }

    @Test
    fun testCreateBroadcast() {
        testRunText("testCreateBroadcast")


        val dummyText = "1002"

        val dummyString = "test${dummyText}"
        val dummyInt = dummyText.toInt()
        val dummyLong = dummyText.toLong()

        val ql = mutableListOf<QustionList>()

        val options = arrayListOf<String>()
        options.add(dummyString)
        options.add(dummyString)
        options.add(dummyString)


        ql.add(QustionList(1,null, QuestionProp(dummyString,options),CategoryType.NORMAL))
        ql.add(QustionList(2,null, QuestionProp(dummyString,options),CategoryType.NORMAL))

        Repository.createBroadcast(Broadcast(
                null,
                dummyString,
                dummyString,
                null,
                GiftType.PRIZE,
                dummyLong,
                dummyString,
                "u51746133bf56b26c7e0988465c5e8c31",
                null,
                dummyString,
                null,
                ql,
                0

        ),
        object: ApiHelper.GetSuccessCallback{
            override fun onSuccessLoaded() {
                tt("onSuccessLoaded")
            }

            override fun onDataNotAvailable() {
                tt("onDataNotAvailable")
            }

        })

    }






}
