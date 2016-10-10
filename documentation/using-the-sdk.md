## Using the core SDK classes and methods

There are 4 sdk methods that are required for normal inBeacon integration: 

* initialize

* attach a user

* detach a user

* refresh

Additional methods exist, but they are not required for normal operation. 

Also you can receive LocalBroadcasts for specific events, but these are also not required for normal operation.

## The InbeaconManager class

This is the main manager class that does all the heavy lifting. Notice that the inbeaconApplication class handles all initialisations of the InbeaconManagerclass. 

<table>
  <tr>
    <td>Static Methods</td>
    <td></td>
  </tr>
  <tr>
    <td>getSharedInstance()</td>
    <td>retrieves the shared InbeaconManager class </td>
  </tr>
</table>


<table>
  <tr>
    <td>Methods</td>
    <td></td>
  </tr>
  <tr>
    <td>attachUser(HashMap<String,String> user) </td>
    <td>Attach local (device) user information to inbeacon.</td>
  </tr>
  <tr>
    <td>detachUser()</td>
    <td>Removes local device user.</td>
  </tr>
  <tr>
    <td>refresh()</td>
    <td>This starts all services and obtains new information from the server. You should call Refresh every once in a while to make sure server information is updated.</td>
  </tr>
  <tr>
    <td>verifyCapabilities()</td>
    <td>Checks for correct hardware and SDK version.
Returns one of the following values: 
Enum com.inbeacon.sdk.InbeaconManager.VerifiedCapability
VerifiedCapability.CAP_OK 
VerifiedCapability.CAP_SDK_TOO_OLD
VerifiedCapability.CAP_BLUETOOTH_DISABLED
VerifiedCapability.CAP_BLUETOOTH_LE_NOT_AVAILABLE
</td>
  </tr>
  <tr>
    <td>askPermissions(Activity activity)
</td>
    <td>Convenience method to ask COARSE_LOCATION permissions for SDK 23, needed for the use of beacons
(safe to call with SDK 22 and lower, in this case it does nothing)

</td>
  </tr>
</table>


### InbeaconManager initialize

Initialize the SDK with your clientID and clientSecret. These credentials are used for communication with the server. 

	**in****itialize****(context,<your clientid>,<your clientsecret>);**

### attachUser

Attach local userinformation to inbeacon. For instance, the user might enter account information in the app. It is also possible not to attach a user, in that case the device is anonymous. The device id is always communicated however on iOS this uuid is no longer related to the hardware UUID.

There are a lot of fields avaiable for userinformation, however none of them are required. Just supply the available information.

// User object can contain 

//	'name', 'email', 'customerid' (your own id, any string, for reference), 

//	‘address’, ‘gender’ ("male" | “female”) ,

//    ‘zip’, ‘city’, ‘country’, ‘birth’ (string format yyyymmdd), ‘phone_mobile’,

// 	’phone_home’, ‘phone_work’,

//    ‘social_facebook_id’,’social_twitter_id’,’social_linkedin_id’

	// EXAMPLE:

HashMap<String, String> user=new HashMap<String, String>(); 

user.put("name","Dwight Schulz");

user.put("email","dwight@ateam.com");

InbeaconManager.getSharedInstance().attachUser(user);	

You can also call AttachUser from any other class, for instance from the main acitivity using the shared instance.

	

### DetachUser

Log out the current current user. From now only anonymous info is send to inBeacon server.

InbeaconManager.getSharedInstance().**d****etachUser();**

### Refresh

Obtain fresh trigger and region information from the inBeacon platform. Best practice is to call this when the app is 

* started

* returned to the foreground

so info is kept updated. 

	InbeaconManager.*getSharedInstance*().refresh();

### SDK 23 - getting Permission

If you do not want to use the bare-bones **askPermissions** method provided by the SDK, you can roll your own. 

You might use this example code to make a more advanced permission request:

**public class **myActivity **extends **Activity  { 

@Override

**protected void **onCreate(Bundle savedInstanceState) {

**super**.onCreate(savedInstanceState);

		...

**if **(Build.VERSION.**_SDK_INT _**>= Build.VERSION_CODES.**_M_**) {

   *// Android M Permission check *

*   ***if **(**this**.checkSelfPermission(Manifest.permission.**_ACCESS_COARSE_LOCATION_**) != PackageManager.**_PERMISSION_GRANTED_**) {

       **final **AlertDialog.Builder builder = **new **AlertDialog.Builder(**this**);

       builder.setTitle(**"This app needs location access"**);

       builder.setMessage(**"Please grant location access so this app can detect beacons."**);

       builder.setPositiveButton(android.R.string.**_ok_**, **null**);

       builder.setOnDismissListener(**new **DialogInterface.OnDismissListener() {

           @Override

           **public void **onDismiss(DialogInterface dialog) {

               requestPermissions(**new **String[]{Manifest.permission.**_ACCESS_COARSE_LOCATION_**}, **_PERMISSION_REQUEST_COARSE_LOCATION_**);

           }

       });

       builder.show();

   }

}

		..

	}

@Override

**public void **onRequestPermissionsResult(**int **requestCode,

                                      String permissions[], **int**[] grantResults) {

   **switch **(requestCode) {

       **case ****_PERMISSION_REQUEST_COARSE_LOCATION_**: {

           **if **(grantResults[0] == PackageManager.**_PERMISSION_GRANTED_**) {

               Log.*d*(**_TAG_**, **"coarse location permission granted"**);

           } **else **{

               **final **AlertDialog.Builder builder = **new **AlertDialog.Builder(**this**);

               builder.setTitle(**"Functionality limited"**);

               builder.setMessage(**"Since location access has not been granted, this app will not be able to discover beacons when in the background."**);

               builder.setPositiveButton(android.R.string.**_ok_**, **null**);

               builder.setOnDismissListener(**new **DialogInterface.OnDismissListener() {

                   @Override

                   **public void **onDismiss(DialogInterface dialog) {

                   }

               });

               builder.show();

           }

           **return**;

       }

   }

}

	…

}

### SDK 22 and older - proguard warnings

If you generate apk’s with proguard with TargetSDK 22 or lower, you need to disable warnings for the new permission checks. Add the following line to your proguard-rules.pro file:

# inbeacon

-dontwarn com.inbeacon.sdk.**

## Calling the inBeaconSDK manager from an Activity or any other class 

At any moment it is possible to obtain the inBeaconManager class by using:

InbeaconManager inbManager = InbeaconManager.getSharedInstance();

For instance you normally want to refresh the inBeacon information when your main Activity starts. This can be done like this:

@Override

protected void onCreate(Bundle savedInstanceState) {

super.onCreate(savedInstanceState);

setContentView(R.layout.activity_sdk_test);

...

**HashMap<String, String> user=new HashMap<String, String>();**

**user.put("name","Dwight Schulz");		// EXAMPLE!!**

**user.put("email","****[dwight@ateam.co**m](mailto:dwight@ateam.com)**");	// EXAMPLE!!**

**InbeaconManager.getSharedInstance().attachUser(user);	// attach the user**

**InbeaconManager.getSharedInstance().refresh();		// refresh all info**

		...

## Receiving inBeaconSDK events 

The inBeacon event mechanism uses a LocalBroadcastManager and intents with actions. To listen to specific events, you need to create an intentfilter and a MessageReceiver like this: 

IntentFilter myIntentFilter=new IntentFilter("com.inbeacon.sdk.event.enterregion");

LocalBroadcastManager.getInstance(this).registerReceiver(	mMessageReceiver,

           								myIntentFilter);

The messageReceiver class can be defined like this:

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            Bundle extras=intent.getExtras();

            Log.w("receiver", "Got action="+intent.getAction()+" extras="+extras);

        }

    };

also you could include more than one inbeacon event in the intentfilter to receive more than one action, like this (this intentfilter listens to all available inbeacon messages):

IntentFilter myIntentFilter=new IntentFilter();

myIntentFilter.addAction(**"com.inbeacon.sdk.event.enterregion"**);     *// user entered a region*

myIntentFilter.addAction(**"com.inbeacon.sdk.event.exitregion"**);      *// user left a region*

myIntentFilter.addAction(**"com.inbeacon.sdk.event.enterlocation"**);   *// user entered a location*

myIntentFilter.addAction(**"com.inbeacon.sdk.event.exitlocation"**);    *// user left a location*

myIntentFilter.addAction(**"com.inbeacon.sdk.event.regionsupdate"**);   *// region definitions were updated*

myIntentFilter.addAction(**"com.inbeacon.sdk.event.enterproximity"**);  *// user entered a beacon proximity*

myIntentFilter.addAction(**"com.inbeacon.sdk.event.exitproximity"**);   *// user left a beacon proximity*

myIntentFilter.addAction(**"com.inbeacon.sdk.event.proximity"**);       *// low level proximity update, once every second when beacons are around*

myIntentFilter.addAction(**"com.inbeacon.sdk.event.appevent"**);        *// defined in the backend for special app-specific pages to show*

myIntentFilter.addAction(**"com.inbeacon.sdk.event.appaction"**);       *// defined in the backend to handle your own local notifications*

Example:

<table>
  <tr>
    <td>public class sdkTest extends Activity { 
    private static final String TAG = "sdkTestActivity";
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras=intent.getExtras();
            Log.w(TAG, "Got action:"+intent.getAction()+" extras:"+extras);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk_test);

        IntentFilter myIntentFilter=new IntentFilter();
        myIntentFilter.addAction("com.inbeacon.sdk.event.appaction");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,myIntentFilter);
    }
}</td>
  </tr>
</table>


### com.inbeacon.sdk.event.enterregion

**com.inbeacon.sdk.event.exitregion**

Fired when device enters or leaves a region (single UUID)

intent Extras:region information

### com.inbeacon.sdk.event.enterlocation

**com.inbeacon.sdk.event.exitlocation**

Fired when device enters or leaves a location (UUID, Major)

intent Extras:location information

### com.inbeacon.sdk.event.enterproximity

**com.inbeacon.sdk.event.leaveproximity**

Fired when device enters or leaves a beacon proximity (UUID, Major, minor, proximity )

### intent Extras: proximity information

### com.inbeacon.sdk.event.proximity

Low level beacon proximity event.

Fired when device proximity to a defined Beacon changes. This event is normally fired once every second when a beacon is in range

intent Extras: Beacon information and proximity to beacon (near/far/immediate)

### com.inbeacon.sdk.event.regionsupdate

Fired when the region definition has been retrieved and updated from the server

intent Extras: none.

### com.inbeacon.sdk.event.appaction

Special case: It is possible to have inBeacon fire a trigger that uses the app logic to handle the Local Notification. The app is responsible for handling trigger event and showing a notifcation or doing other things. 

intent Extras: Action information from this trigger that is normally used to define the Local Notification

### com.inbeacon.sdk.event.appevent

Special case: It is possible to have inBeacon fire a trigger that uses the app logic to handle the Activity that is activated when the user enters the notification. 

intent Extras: Page information from this trigger with app-argument given in the backend:

![image alt text](image_4.png)
