package com.inbeacon.inbeaconexample;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inbeacon.sdk.Base.Constants;
import com.inbeacon.sdk.InbeaconManager;
import com.inbeacon.sdk.InbeaconManagerInterface;
import com.inbeacon.sdk.User.UserPropertyService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    InbeaconManagerInterface inbeaconManager;
    UserPropertyService userPropertyService;
    Context context;

    /**
     * sample broadcastreceiver to show messages coming from the SDK
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "Got action=" + action + " extras=" + intent.getExtras());
        switch(action) {
            case Constants.LocalBroadcasts.EVENT_USERINFO:
                // event received when a userpropery changes.
                // If you change a proerty in the backend (intelligence->users menu)
                // and the device refreshes, you will get this event.

                // update the test property on screen
                ((EditText)findViewById(R.id.editTest)).setText(userPropertyService.getPropertyString("test", ""));
                break;
        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;

        // obtain the inbeaconmanager singleton
        inbeaconManager = InbeaconManager.getInstance();
        // you can manipulate user properties with the userPropertyService
        userPropertyService = inbeaconManager.getUserPropertyService();

        // by setting the userproperty 'name' you can identify this device in the intelligence->users menu in the backend
        userPropertyService.putPropertyString("name","example-app");

        // example permission check.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3771);
            }
        }

        // make sure we get SDK events in our messagereceiver
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.LocalBroadcasts.EVENT_USERINFO);
        myIntentFilter.addAction(Constants.LocalBroadcasts.EVENT_PROXIMITY);
        myIntentFilter.addAction(Constants.LocalBroadcasts.EVENT_GEOFENCE);
        myIntentFilter.addAction(Constants.LocalBroadcasts.EVENT_LOCATION);
        myIntentFilter.addAction(Constants.LocalBroadcasts.EVENT_APPEVENT);
        myIntentFilter.addAction(Constants.LocalBroadcasts.EVENT_GPSFIX);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, myIntentFilter);


        // Force a server sync on the refresh-button.
        // This will get new information from the backend, including changed userproperties, new regions to monitor etc.
        // normally this happens automatically
        Button refreshForceBtn = (Button) findViewById(R.id.id_refresh_force);
        refreshForceBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.w(TAG,"Request a resync");
                inbeaconManager.refreshForced();
            }
        });

        // example userinterface for getting and setting a property.
        Button saveBtn = (Button) findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                userPropertyService.putPropertyString("test", ((EditText)findViewById(R.id.editTest)).getText().toString() );
                Log.w(TAG, "Properties saved");
            }
        });
        ((EditText)findViewById(R.id.editTest)).setText(userPropertyService.getPropertyString("test", ""));


        final EditText tagFld = (EditText) findViewById(R.id.editTag);
        Button setTagBtn = (Button) findViewById(R.id.setTag);
        Button resetTagBtn = (Button) findViewById(R.id.resetTag);
        Button hasTagBtn = (Button) findViewById(R.id.hasTag);
        hasTagBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String tag=tagFld.getText().toString();
                boolean hastag=userPropertyService.hasTag(tag);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Tag "+tag+(hastag ? " is SET":" is NOT set"));
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        setTagBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String tag=tagFld.getText().toString();
                userPropertyService.setTag(tag);
            }

        });
        resetTagBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String tag=tagFld.getText().toString();
                userPropertyService.resetTag(tag);
            }

        });
    }


}
