package com.imagesoftware.anubhav.vacmet.services;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by Anubhav-Singh on 11-01-2018.
 */

public class UserAccessJobService extends JobService {

    private CustomAsynTaskForJob customAsynTaskForJob;
    private SharedPreferences sharedprefs;
    private final String LoginPrefs = "LoginPrefs";
    private final String LoggedInUser = "LoggedInUser";
    private final String LoggedInUserName = "LoggedInUserName";
    private final String LoggedInUserPassword = "LoggedInUserPassword";

    @Override
    public boolean onStartJob(JobParameters job) {
        /**
         * This method operates on UI Thread.
         * We send the job to another thread, and immediately tells the function, that there is ongoing Job in worker thread,
         * hence onJobFinished would be required to call from the worker thread.
         */
        customAsynTaskForJob = (CustomAsynTaskForJob) new CustomAsynTaskForJob().execute(job);
        sharedprefs = getSharedPreferences(LoginPrefs, MODE_PRIVATE);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        //Only called when job is finished midway, hence clear resources here, and return true, to indicate to reschedule job
        if(customAsynTaskForJob!=null){
            customAsynTaskForJob.cancel(true);
        }
        return true;
    }

    class CustomAsynTaskForJob extends AsyncTask<JobParameters,Void,String>{

        private JobParameters initialJobParams = null;
        private JobParameters finalJobParams = null;
        private FirebaseAuth firebaseAuth;
        private DatabaseReference mDatabase;

        @Override
        protected String doInBackground(JobParameters... jobParameters) {
            initialJobParams = jobParameters[0];
            String user_email = null;

            if(initialJobParams.getExtras()!=null && initialJobParams.getExtras().getString("user_email")!=null){
                user_email = initialJobParams.getExtras().getString("user_email");
            }
            final String final_user = user_email;
            firebaseAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference("users");
            finalJobParams = initialJobParams;
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean user_access = collectData((Map<String, Object>) dataSnapshot.getValue(),final_user);
                    /**Need not to reschedule job, if job is finished, as it is successful now, it will again run based on Criteria
                     * Now fire broadcast, for a successful job
                    */
                    if(!user_access) {
                        SharedPreferences.Editor edit = sharedprefs.edit();
                        edit.putString(LoggedInUser, null);
                        edit.putString(LoggedInUserName, null);
                        edit.putString(LoggedInUserPassword, null);
                        edit.apply();
                        Intent intentFilter = new Intent();
                        intentFilter.setAction("com.imagesoftware.vacmet.user_revoked");
                        sendBroadcast(intentFilter);
                    }
                    jobFinished(finalJobParams,false);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
        private boolean collectData(Map<String, Object> value, String user_email) {
            for (Map.Entry<String, Object> entrySet : value.entrySet()) {
                Map<String, Object> user = (Map<String, Object>) entrySet.getValue();
                /**
                 * Extract user details, which are send from activity to service via Extras to JobService
                 */
                if (((String) user.get("userEmail")).equalsIgnoreCase(user_email.trim())) {
                    //Successfully Matched Records....

                    if (((boolean) user.get("approved"))) {
                        return true;
                    }else{
                        return false;
                    }
                }
            }
            return false;
        }
    }
}
