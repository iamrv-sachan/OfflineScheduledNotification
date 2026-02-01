package com.example.schedulednotification.fcm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.schedulednotification.util.NotificationUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.let

class MyFirebaseMessageService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        const val NOTIFICATION_TITLE = "notification_title"
        const val NOTIFICATION_MESSAGE = "notification_message"
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onMessageReceived(message: RemoteMessage) {

        val title = message.data["title"]
        val description = message.data["body"]
        Log.d(TAG, "onMessageReceived: $title   ${message.data}")
        val isScheduled = message.data["isScheduled"]?.toBoolean()
        isScheduled?.let { scheduled ->
            if (scheduled) {
                val scheduledTime = message.data["scheduledTime"]
                Log.d(TAG, "onMessageReceived: $scheduledTime")
                scheduleAlarm(scheduledTime, title, description)
            } else {
                NotificationUtil(this).showNotification(title!!, description!!)
            }
        }
    }

    private fun scheduleAlarm(
        time: String?,
        title: String?,
        description: String?,
    ) {
        val alarmMag = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, NotificationBroadcastReceiver::class.java).let { intent ->
            intent.putExtra(NOTIFICATION_TITLE, title)
            intent.putExtra(NOTIFICATION_MESSAGE, description)
            PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        val schTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .parse(time!!)
        Log.d(TAG, "scheduleAlarm: $schTime")
        schTime?.let {
            alarmMag.set(
                AlarmManager.RTC_WAKEUP,
                it.time,
                alarmIntent,
            )
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "token: ")
    }
}