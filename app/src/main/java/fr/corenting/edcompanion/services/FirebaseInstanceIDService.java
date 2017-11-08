package fr.corenting.edcompanion.services;


import com.google.firebase.iid.FirebaseInstanceIdService;

import fr.corenting.edcompanion.utils.NotificationsUtils;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        NotificationsUtils.refreshPushSubscriptions(this);
    }
}
