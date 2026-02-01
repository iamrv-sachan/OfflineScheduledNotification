package com.example.schedulednotification.fcm

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.schedulednotification.fcm.MyFirebaseMessageService.Companion.NOTIFICATION_MESSAGE
import com.example.schedulednotification.fcm.MyFirebaseMessageService.Companion.NOTIFICATION_TITLE
import com.example.schedulednotification.util.NotificationUtil

class ScheduledWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val title = inputData.getString(NOTIFICATION_TITLE)
        val message = inputData.getString(NOTIFICATION_MESSAGE)

        NotificationUtil(applicationContext).showNotification(title!!, message!!)

        return Result.success()
    }
}