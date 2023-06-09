package com.dana.merchantapp.domain.notification

import android.util.Log
import com.dana.merchantapp.data.notification.NotificationGenerator
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseMessagingReceiver : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM data", "Message data payload: ${remoteMessage.data}")

        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        val notificationGenerator = NotificationGenerator(this)
        notificationGenerator.generateNotification(title, body)
    }
}