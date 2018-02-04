package com.imagesoftware.anubhav.vacmet.notifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Anubhav-Singh on 04-02-2018.
 */

public class FirebaseTokenService extends FirebaseInstanceIdService {
    public FirebaseTokenService() {
        super();
    }

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("", "Refreshed token: " + refreshedToken);
    }
}
