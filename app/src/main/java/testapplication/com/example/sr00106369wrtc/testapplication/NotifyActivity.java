package testapplication.com.example.sr00106369wrtc.testapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

public class NotifyActivity extends AppCompatActivity {

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;
    int notifyNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notifyicon)
                .setContentTitle("Text Message")
                .setContentText("PersonName" + ":" + "Message");

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        String[] events = new String[6];
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("IM from PersonName:");

// Moves events into the expanded layout
        for (int i = 0; i < events.length; i++) {

            events[i] = "Person" + i + ":" + "Message";
            inboxStyle.addLine(events[i]);
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

//Cancels the notification after opening
        mBuilder.setAutoCancel(true);

//Sets the LED lights for the notification
        mBuilder.setLights(Color.BLUE, 500, 500);

//Sets the vibrator for 5seconds
        long[] time = {500};
        mBuilder.setVibrate(time);

//Sets the default notification sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this,NetworkActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NetworkActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setNumber(++notifyNumber);
        mNotificationManager.notify(0, mBuilder.build());
        finish();
    }
}
