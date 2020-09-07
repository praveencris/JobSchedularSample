package com.sabkayar.praveen.notificationschedular;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class NotificationJobService extends JobService {
    NotificationManager mNotifyManager;
    //Notification channel ID
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    //Notification ID(Must be unique within same app) to replace the existing notification
    private static final int NOTIFICATION_ID = 0;

    private AsyncTask<Void, Void, String> mAsyncTask;

    @Override
    public boolean onStartJob(final JobParameters params) {
        //Create the notification channel
        createNotificationChannel();

        //Set up the notification content intent to launch the app when clicked
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0
                , new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Job Service")
                .setContentText("Your Job is running!")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_job_running)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        mAsyncTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... para) {
                try {
                    Thread.sleep(5000);
                    mNotifyManager.notify(NOTIFICATION_ID, builder.build());
                    jobFinished(params, false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        mAsyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Toast.makeText(this, "Job Failed", Toast.LENGTH_SHORT).show();
        mAsyncTask.cancel(true);
        return true;
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Define notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Job Service notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifications from Job Service");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
}
