package com.example.schedulednotification.fcm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.schedulednotification.fcm.MyFirebaseMessageService.Companion.NOTIFICATION_MESSAGE
import com.example.schedulednotification.fcm.MyFirebaseMessageService.Companion.NOTIFICATION_TITLE

class NotificationBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent?) {
        intent?.let {
            val title = it.getStringExtra(NOTIFICATION_TITLE)
            val message = it.getStringExtra(NOTIFICATION_MESSAGE)

            val notificationData = Data.Builder()
                .putString(NOTIFICATION_TITLE, title)
                .putString(NOTIFICATION_MESSAGE, message)
                .build()

            val worker = OneTimeWorkRequest.Builder(ScheduledWorker::class.java)
                .setInputData(notificationData)
                .build()

            p0?.let { context -> WorkManager.getInstance(context) }?.beginWith(worker)?.enqueue()
        }
    }
}