package com.quiz_together.data.remote

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import android.R.attr.keySet
import com.google.gson.JsonParser
import com.google.gson.JsonObject



class FirebaseHelper : FirebaseMessagingService() {

    val TAG = "MyFirebaseMessagi...#$#"

    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)

        Log.i(TAG,newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {

            val gsObj = JsonObject()
            val jsonParser = JsonParser()
            val map = remoteMessage.data
            var str: String

            for (key in map.keys) {
                str = map[key]!!
                try {
                    gsObj.add(key, jsonParser.parse(str))
                } catch (e: Exception) {
                    gsObj.addProperty(key, str)
                }
            }

            Log.i(TAG, "## GET MSG FROM FIREBASE >> " + gsObj.toString())



            val intent = Intent(FMC_ACTION)
            intent.putExtra(FMC_IN_QUIZING, gsObj.toString())
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    companion object {
        const val FMC_IN_QUIZING = "FMC_IN_QUIZING"
        const val FMC_ACTION = "FMC_ACTION"
    }


}