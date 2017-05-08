package com.umiss;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by gustavo on 4/13/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String LOGOUT = "LOGOUT";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> message = remoteMessage.getData();

        if ( message != null && isLogged() ){

            String notificationType = message.get("Type");

            if ( notificationType.equals(LOGOUT) ){

                logOut();
            }else {

                sendNotification(message.get("Title"), message.get("Body"));
            }
        }
    }

    private void sendNotification(String title, String body) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notifiBuilder.build());
    }

    private boolean isLogged(){

        return getSharedPreferences("data",MODE_PRIVATE).getBoolean(LoginActivity.IS_LOGGED, false);
    }

    private void logOut(){

        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean(LoginActivity.IS_LOGGED, false);
        prefEditor.putString(LoginActivity.TOKEN, null);
        prefEditor.commit();

        if ( MyApplication.isActivityVisible() )
            startLoginActivity();
    }

    private void startLoginActivity(){

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
