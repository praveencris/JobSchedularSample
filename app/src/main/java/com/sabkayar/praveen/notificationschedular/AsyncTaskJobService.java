package com.sabkayar.praveen.notificationschedular;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

public class AsyncTaskJobService extends JobService {
    @Override
    public boolean onStartJob(final JobParameters params) {

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
