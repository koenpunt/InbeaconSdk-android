package com.inbeacon.inbeacon_sdk_android_example;

import android.app.Application;
import com.inbeacon.sdk.InbeaconManager;
import java.util.HashMap;

public class myApp extends Application {
    private static final String TAG = "myApp";

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize with your ClientID and Secret.
        InbeaconManager.initialize(this, "demo", "QmE3WWlMNUluUnp2Y2h1MUF4NFpJQ01aZ2ZCRnVGbng");
        //
        // If you have user credentials somewhere in your app, you can attach the account
        //        HashMap<String, String> user=new HashMap<String, String>();
        //        user.put("name","Dwight Schulz");                 // example only! don't use these in your own app
        //        user.put("email","dwight@ateam.com");
        //        InbeaconManager.getSharedInstance().attachUser(user);

        // refresh data from server. Call this after attachuser, so everything is updated.
        InbeaconManager.getSharedInstance().refresh();
    }

}
