package com.quiz_together.util

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "MyFirebaseMessagi...#$#"

    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)

        Log.i(TAG,newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.getData());


        }

        remoteMessage.notification.let {
            Log.i(TAG, "Message Notification Body 1: ${it?.body.toString()}");
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.i(TAG, "Message Notification Body 2: ${remoteMessage.notification!!.body.toString()}");
        }


    }



}