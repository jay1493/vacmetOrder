package com.imagesoftware.anubhav.vacmet.globalReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.imagesoftware.anubhav.vacmet.LoginActivity;



/**
 * Created by Anubhav-Singh on 12-01-2018.
 */

public class UserAccessReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, LoginActivity.class);
        context.startActivity(intent1);
    }
}
