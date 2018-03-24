package com.pankajmehtanewdemo.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pankajmehtanewdemo.MenuActivity;
import com.pankajmehtanewdemo.NotificationActivity;
import com.pankajmehtanewdemo.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SNT Soutions on 8/29/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Bitmap bitmap;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional

        //Calling method to generate notification

        if (remoteMessage.getNotification() != null) {
            Log.d("FCM", "Notification Message Body: " + remoteMessage.getNotification().getBody());
            Log.i("Tag", "Tag :" + remoteMessage.getNotification().getBody());
        }

     if (remoteMessage.getData().containsKey("post_title")) {
        sendNotification(remoteMessage.getData().get("post_title"),remoteMessage.getData().get("post_msg"));
  }


    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String title,String message) {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        Bitmap bitmap = getBitmapFromURL("https://lh3.googleusercontent.com/-OLQfBl9i8g8/VoUTikP5yQI/AAAAAAAAAZY/3UA8-PYcScQMTSpCGfpX9J6ZQvw0K2eXQCL0B/w533-h534-no/yash.jpg");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.pmlogo)
                .setContentTitle(title+"")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);   // .setLargeIcon(bitmap)

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }




    private void sendAttendanceNotification(String messageBody) {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        Bitmap bitmap = getBitmapFromURL("https://lh3.googleusercontent.com/-OLQfBl9i8g8/VoUTikP5yQI/AAAAAAAAAZY/3UA8-PYcScQMTSpCGfpX9J6ZQvw0K2eXQCL0B/w533-h534-no/yash.jpg");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name)+"")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)

                .setContentIntent(pendingIntent);   // .setLargeIcon(bitmap)

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}