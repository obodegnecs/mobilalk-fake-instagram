package com.example.inspigram;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.nio.channels.Channel;

public class NotificationHandler {

    private static final String CHANNEL_ID = "notification_id";
    private final int NOTIFICATION_ID = 0;

    private Context context;
    private NotificationManager notificationManager;

    public NotificationHandler(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel () {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        } else {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Inspigram Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setDescription("Inspigram notification!");
            this.notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void send(String s) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Inspigram")
                .setContentText("Lájkoltad " + s + " képét!")
                .setSmallIcon(R.drawable.like2);

        this.notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
