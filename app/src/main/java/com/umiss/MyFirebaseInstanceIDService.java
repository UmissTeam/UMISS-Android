package com.umiss;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh(){

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // save the token to third party server
    }
}
