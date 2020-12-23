package com.transportervendor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class FirebaseMessage extends FirebaseMessagingService {
    SharedPreferences sp = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String>map = remoteMessage.getData();
        String title = map.get("title");
        String description = map.get("body");
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "Test channel";
        String channelName = "Test";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder nb = new NotificationCompat.Builder(getApplicationContext(),channelId);
        nb.setContentTitle(title);
        nb.setContentText(description);
        nb.setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(1,nb.build());
    }
}
