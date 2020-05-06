// Reference: https://developer.android.com/training/notify-user/build-notification

package com.prisonerprice.simplenotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity {

    private Button basicNotificationBtn;
    private final static String CHANNEL_ID = "CHANNEL_ID";
    public final static String ACTION_SNOOZE = "ACTION_SNOOZE";

    // The basic notification
    private final static int NOTIFICATION_ID_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        basicNotificationBtn = findViewById(R.id.basic_notification_btn);

        // Step 1: Create the Notification channel
        createNotificationChannel();

        // Step 2: Create the Notification
        // Step 3: Set the notification's tap action
        Intent tapIntent = new Intent(this, MainActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent tapPendingIntent = PendingIntent.getActivity(this, 0, tapIntent, 0);

        // Step 4: Set an action button for the notification, this step communicate with the Broadcast Receiver
        Intent snoozeIntent = new Intent(this, MyBroadcaseReceiver.class);
        snoozeIntent.setAction(ACTION_SNOOZE);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("This is the title")
                .setContentText("This is the context")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // set the intent that will fire when the user taps the notification
                .setContentIntent(tapPendingIntent)
                .setAutoCancel(true)
                // add the snooze action
                .addAction(R.drawable.ic_launcher_foreground, "SNOOZE!", snoozePendingIntent);

        // Step 5 show the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        basicNotificationBtn.setOnClickListener((v) -> {
            notificationManagerCompat.notify(NOTIFICATION_ID_1, builder.build());
        });



    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
