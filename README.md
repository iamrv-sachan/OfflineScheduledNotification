# Offline Scheduled Notification with FCM & Alarm Manager

This project demonstrates how to implement a hybrid notification system for Android. It uses **Firebase Cloud Messaging (FCM)** to receive data payloads and **Alarm Manager** to schedule notifications for a specific future time, ensuring they trigger even if the device is offline at the scheduled moment.

---

## 🚀 Features

* **Real-time Notifications:** Immediate delivery when the "scheduled" flag is set to false.
* **Offline Scheduling:** Uses `AlarmManager` to trigger notifications at a precise time, even without an active internet connection.
* **WorkManager Integration:** Hand-off from the broadcast receiver to `WorkManager` to ensure notification delivery is handled reliably in the background.
* **Android 13+ Ready:** Includes necessary permissions for POST_NOTIFICATIONS.

---

## 🛠️ How it Works

1. **FCM Data Payload:** The app listens for a data message from Firebase.
2. **Decision Logic:** * If `isScheduled` is `false`: Show the notification immediately.
* If `isScheduled` is `true`: Parse the `scheduledTime` and set an alarm.


3. **Alarm Trigger:** When the time arrives, the `AlarmManager` fires a broadcast to `NotificationBroadcastReceiver`.
4. **Background Processing:** The receiver triggers a `OneTimeWorkRequest` via `WorkManager`.
5. **Notification Display:** `ScheduledWorker` calls `NotificationUtil` to build and display the notification to the user.

---

## 📋 Payload Structure

To test this, send a **Data Message** (not a Notification Message) from the Firebase Console or via API with the following keys:

| Key | Value Example | Description |
| --- | --- | --- |
| `title` | "Limited Offer!" | The notification title. |
| `body` | "Get 50% off today." | The notification content. |
| `isScheduled` | "true" | Determines if it should be delayed. |
| `scheduledTime` | "2026-02-01 15:30:00" | Format: `yyyy-MM-dd HH:mm:ss`. |

---

## ⚙️ Setup Instructions

### 1. Firebase Configuration

* Add your `google-services.json` to the `app/` folder.
* Ensure the `com.google.gms:google-services` plugin is applied in your build files.

### 2. Permissions

The app requests the following in the `AndroidManifest.xml`:

* `POST_NOTIFICATIONS`: Required for Android 13 (API 33) and above.
* `SCHEDULE_EXACT_ALARM`: (Optional/Recommended) If you need to target exact timing on newer Android versions.

### 3. Key Components

* **MyFirebaseMessageService:** The entry point for incoming FCM messages.
* **NotificationBroadcastReceiver:** Wakes up the app when the scheduled time is reached.
* **ScheduledWorker:** Handles the actual task of building the notification.
* **NotificationUtil:** A helper class to manage notification channels and styles.

---

## ⚠️ Important Note on Exact Alarms

Starting with Android 12, if you require high-precision timing, you may need to request `android.permission.SCHEDULE_EXACT_ALARM` and check if the app has permission to use it at runtime. For general reminders, the current `AlarmManager.set()` implementation is sufficient.
