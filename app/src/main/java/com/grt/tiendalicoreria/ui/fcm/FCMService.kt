package com.grt.tiendalicoreria.ui.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.grt.tiendalicoreria.R
import com.grt.tiendalicoreria.data.Constants
import com.grt.tiendalicoreria.ui.order.OrderFragment

class FCMService : FirebaseMessagingService() {
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        registerNewTokenLocal(newToken)
    }

    private fun registerNewTokenLocal(newToken: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        preferences.edit {
            putString(Constants.PROP_TOKEN, newToken)
                .apply()
        }

        Log.i("new token", newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()){
            sendNotificationByData(remoteMessage.data)
        }

        remoteMessage.notification?.let {
            val imgUrl = it.imageUrl//"https://facturacionweb.site/blog/wp-content/uploads/2020/08/firebase_android.png"
            if (imgUrl == null){
                sendNotification(it)
            } else {
                Glide.with(applicationContext)
                    .asBitmap()
                    .load(imgUrl)
                    .into(object : CustomTarget<Bitmap?>(){
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            sendNotification(it, resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }
        }
    }

    private fun sendNotification(notification: RemoteMessage.Notification, bitmap: Bitmap? = null){
        val intent = Intent(this, OrderFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.notification_channel_id_default)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setColor(ContextCompat.getColor(this, R.color.orange_500))
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(notification.body))

        bitmap?.let {
            notificationBuilder
                .setLargeIcon(bitmap)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null))
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,
                getString(R.string.notification_channel_name_default),
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendNotificationByData(data: Map<String, String>){
        val intent = Intent(this, OrderFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.notification_channel_id_default)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(data.get("title"))
            .setContentText(data.get("body"))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setColor(ContextCompat.getColor(this, R.color.orange_500))
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(data.get("body")))

        val actionIntent = data.get(Constants.ACTION_INTENT)?.toInt()
        val orderId = data.get(Constants.PROP_ID)
        val status = data.get(Constants.PROP_STATUS)?.toInt()
        val trackIntent = Intent(this, OrderFragment::class.java).apply {
            putExtra(Constants.ACTION_INTENT, actionIntent) //1 = track
            putExtra(Constants.PROP_ID, orderId)
            putExtra(Constants.PROP_STATUS, status)
        }
        val trackPendingIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(),
            trackIntent, 0)
        val action = NotificationCompat.Action.Builder(R.drawable.ic_local_shipping, "Rastrear ahora",
            trackPendingIntent).build()

        notificationBuilder.addAction(action)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId,
                getString(R.string.notification_channel_name_default),
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}