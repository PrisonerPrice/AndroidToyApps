package com.prisonerprice.simplenotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static com.prisonerprice.simplenotification.MainActivity.ACTION_SNOOZE;

public class MyWorker extends Worker {

    private final static String CHANNEL_ID = "CHANNEL_ID_2";
    private Context context;
    private final static int NOTIFICATION_ID_2 = 2;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        // Step 1: Create the Notification channel
        createNotificationChannel(context);

        // Step 2: Create the Notification
        // Step 3: Set the notification's tap action
        Intent tapIntent = new Intent(context, MainActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent tapPendingIntent = PendingIntent.getActivity(context, 0, tapIntent, 0);

        // Step 4: Set an action button for the notification, this step communicate with the Broadcast Receiver
        Intent snoozeIntent = new Intent(context, MyBroadcaseReceiver.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Scheduled Notification")
                .setContentText("10s passed")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // set the intent that will fire when the user taps the notification
                .setContentIntent(tapPendingIntent)
                .setAutoCancel(true)
                // add the snooze action
                .addAction(R.drawable.ic_launcher_foreground, "SNOOZE!", snoozePendingIntent);

        // Step 5 show the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(NOTIFICATION_ID_2, builder.build());

        return Result.success();
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name_2";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
