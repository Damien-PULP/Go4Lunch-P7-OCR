package com.delombaertdamien.go4lunch.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.delombaertdamien.go4lunch.R;
import com.delombaertdamien.go4lunch.models.Users;
import com.delombaertdamien.go4lunch.ui.activity.LogActivity;
import com.delombaertdamien.go4lunch.ui.activity.MainActivity;
import com.delombaertdamien.go4lunch.utils.FirestoreCall;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.RemoteMessage;


import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService implements FirestoreCall.CallbackGetAllInformationToConstructNotification {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FirebaseMessageService", "Message received");
        FirestoreCall.getAllInformationToConstructNotification(this);
    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserHelper.updateToken(uid, s);
    }

    private void showNotification(String messageBody) {
        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, LogActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notification_title));
        inboxStyle.setSummaryText(messageBody);
        //inboxStyle.addLine(messageBody);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(messageBody);

        // 3 - Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_logo_go4lunch_blank)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setSubText(messageBody)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(bigTextStyle);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // 7 - Show notification
        String NOTIFICATION_TAG = "FIREBASEOC";
        int NOTIFICATION_ID = 007;
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());

    }

    @Override
    public void onSuccessGetAllInformationToConstructNotification(String name, String nameRestaurant, List<Users> usersLunchByUser) {
        String allUsers = "";
        if(usersLunchByUser != null) {
            for (int i = 0; i < usersLunchByUser.size(); i++) {
                if(!usersLunchByUser.get(i).getUsername().equals(name)) {
                    allUsers += usersLunchByUser.get(i).getUsername();
                    if (usersLunchByUser.size() < i - 1) {
                        allUsers += ", ";
                    }
                }
            }
        }
        String  msg;
        if(!allUsers.equals("")) {
            msg = "Salut " + name + ", aujourd'hui tu manges au " + nameRestaurant + " avec " + allUsers;
        }else{
            msg = "Salut " + name + ", aujourd'hui tu manges au " + nameRestaurant;
        }
        Log.d("FirebaseMessaging", msg);
        showNotification(msg);
    }
    @Override
    public void onFailureGetAllInformationToConstructNotification(Exception e) {
        Log.e("FirebaseMessaging", e.getMessage());
    }
}
