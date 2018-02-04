package com.imagesoftware.anubhav.vacmet.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Anubhav-Singh on 04-02-2018.
 */

public class CustomNotificationService extends FirebaseMessagingService {

    public CustomNotificationService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
