package com.quiz_together.data.remote

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.quiz_together.R
import com.quiz_together.ui.main.MainActivity


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

            Log.i(TAG, "## GET MSG FROM FIREBASE >> " + gsObj.get("pushType").asString)
            if (gsObj.get("pushType").asString == "NOTICE_MESSAGE") {//PushType.NOTICE_MESSAGE.name) {

                sendNotification("QX : 실시간 퀴즈쇼", gsObj.get("title").asString,
                        gsObj.get("broadcastId").asString, gsObj.get("userId").asString)
                return
            }

            val intent = Intent(FMC_ACTION)
            intent.putExtra(FMC_IN_QUIZING, gsObj.toString())
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    fun sendNotification(title: String, msg: String, broadcastId: String, userId: String) {
//
//        Log.i(TAG,"$title  $msg $broadcastId $userId")
//
//        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra(MainActivity.BROADCAST_ID, broadcastId)
//        intent.putExtra(MainActivity.USER_ID, userId)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT)
//
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this)
//                .setLargeIcon(BitmapFactory.decodeResource(resources, android.R.drawable.ic_dialog_info))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.notify(100000 /* ID of notification */, notificationBuilder.build())

        var channelId = "channel"
        var channelName = "Channel Name"

        var notifManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, channelName, importance)
            notifManager.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.putExtra(MainActivity.BROADCAST_ID, broadcastId)
        notificationIntent.putExtra(MainActivity.USER_ID, userId)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val requestID = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(applicationContext, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentTitle(title) // required
                .setContentText(msg)  // required
                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                .setAutoCancel(true) // 알림 터치시 반응
                .setSound(RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(android.R.drawable.btn_star)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icc_add))
                .setBadgeIconType(R.drawable.icc_add)
                .setContentIntent(pendingIntent)

        notifManager.notify(0, builder.build())
    }


    companion object {
        const val FMC_IN_QUIZING = "FMC_IN_QUIZING"
        const val FMC_ACTION = "FMC_ACTION"
        const val FIREBASE_KEY_FOR_EVERYONE = "quiztogether"

    }


}