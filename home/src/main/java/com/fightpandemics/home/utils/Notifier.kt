package com.fightpandemics.home.utils

/**
 * Utility class for posting notifications.
 * This class creates the notification channel (as necessary) and posts to it when requested.
 */
object Notifier {

    private const val channelId = "Default"

    /*fun init(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                activity.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            val existingChannel = notificationManager.getNotificationChannel(channelId)
            if (existingChannel == null) {
                // Create the NotificationChannel
                val name = activity.getString(R.string.defaultChannel)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel(channelId, name, importance)
                mChannel.description = activity.getString(R.string.notificationDescription)
                notificationManager.createNotificationChannel(mChannel)
            }
        }
    }

    fun postNotification(id: Long, context: Context, intent: PendingIntent) {
        val builder = NotificationCompat.Builder(context, channelId)
        builder.setContentTitle(context.getString(R.string.deepLinkNotificationTitle))
            .setSmallIcon(R.drawable.donut_with_sprinkles)
        val text = context.getString(R.string.addDonutInfo)
        val notification = builder.setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()
        val notificationManager = NotificationManagerCompat.from(context)
        // Remove prior notifications; only allow one at a time to edit the latest item
        notificationManager.cancelAll()
        notificationManager.notify(id.toInt(), notification)
    }*/
}
