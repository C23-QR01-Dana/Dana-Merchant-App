package com.dana.merchantapp.data.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.dana.merchantapp.R
import com.dana.merchantapp.presentation.main.MainActivity

class NotificationGenerator (private val context: Context) {
    fun generateNotification(title: String?, body: String?) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )

        val notificationBuilder = NotificationCompat.Builder(context, "PAYMENT_1")
            .setSmallIcon(R.drawable.baseline_attach_money_24)
            .setContentTitle(title)
            .setContentText(body)
            .setColor(ContextCompat.getColor(context, R.color.black))
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(0, notificationBuilder.build())
        }
    }
}