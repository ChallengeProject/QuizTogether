package com.quiz_together.data.remote

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

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
            Log.i(TAG, "Message data payload: " + remoteMessage.getData())
        }

        remoteMessage.notification.let {
            Log.i(TAG, "Message Notification Body 1: ${it?.body.toString()}");
            val intent = Intent(FMC_ACTION)
            intent.putExtra(FMC_IN_QUIZING, it?.body.toString())

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }


    }

    companion object {
        const val FMC_IN_QUIZING = "FMC_IN_QUIZING"
        const val FMC_ACTION = "FMC_ACTION"
    }


}