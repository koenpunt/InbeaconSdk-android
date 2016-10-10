# Using the SDK
## InbeaconManager class

This is the main manager class that does all the heavy lifting. Notice that the inbeaconApplication class handles all initialisations of the InbeaconManagerclass. 

### initialize()
Initialize the SDK with your credentials. This will create a singleton instance that is always accessible using `InbeaconManager.getSharedInstance()` 

```java
static InbeaconManagerInterface initialize(Context context, String clientId, String clientSecret)
```
Initialize the SDK with your clientID and clientSecret. These credentials are used for communication with the server.
You can find your client-ID and client-Secret in your [account overview](http://console.inbeacon.nl/accmgr) 

>Example:
>Initialize the SDK in your Appliction object in the `onCreate` method as follows:
>
```java
public class myApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InbeaconManager.initialize(this, "<<your client-ID>", "<<your client-Secret>>");    
        ...
    }
	...
}    
```


### getSharedInstance()
Retrieves the shared InbeaconManager singleton class 

```java
static InbeaconManagerInterface getSharedInstance();
```
>Example: At any moment it is possible to obtain the inBeaconManager class, for instance from an Activity:
>
```java
...
InbeaconManager inbManager = InbeaconManager.getSharedInstance();
inbManager.attachUser(user);
```

### attachUser()

```java
void attachUser(HashMap<String, String> user);
```

The inBeacon backend has user information for each device. The user information are properties that fall in any of the 2 categories:

* Fixed properties. These always exist and control specific functionality. These are the fixed properties
  - `name`: Full user name, both first and family name. Example ‘Dwight Schulz’
  - `email`: User email. Example: ‘dwight@a-team.com’
  - `gender`: User gender: male, female or unknown
  - `country`: ISO3166 country code
  - `id`: inBeacon unique user id (read-only)
  - `avatar`: URL to user avatar

* Custom properties. You can define other properties, like "facebook-ID" or “age”

>Example:
>
```java
HashMap<String, String> user=new HashMap<String, String>(); 
user.put("name","Dwight Schulz");
user.put("email","dwight@ateam.com");
InbeaconManager.getSharedInstance().attachUser(user);
```

You can also call AttachUser from any other class, for instance from the main acitivity using the shared instance.


### detachUser()
Removes all local device user properties.

```java
void detachUser();
```

Log out the current current user. From now only anonymous info is send to inBeacon server.

>Example:
>
```java
InbeaconManager.getSharedInstance().detachUser();
```

### refresh()
This starts all services and obtains new information from the server. You should call Refresh every once in a while to make sure server information is updated.

```java
    void refresh();
```

Obtain fresh trigger and region information from the inBeacon platform. Best practice is to call this when the app is 

* started

* returned to the foreground

so info is kept updated. 

>Example:
>
```java
	InbeaconManager.getSharedInstance().refresh();
```
	
### verifyCapabilities()</td>
Checks for correct hardware and SDK version.

```java
    VerifiedCapability verifyCapabilities();
```

Returns one of the following values: 

```java
Enum com.inbeacon.sdk.InbeaconManager.VerifiedCapability
VerifiedCapability.CAP_OK 
VerifiedCapability.CAP_SDK_TOO_OLD
VerifiedCapability.CAP_BLUETOOTH_DISABLED
VerifiedCapability.CAP_BLUETOOTH_LE_NOT_AVAILABLE
```

### askPermissions(Activity activity)
Convenience method to ask COARSE_LOCATION permissions for SDK 23, needed for the use of beacons
(safe to call with SDK 22 and lower, in this case it does nothing)

```java
    void askPermissions(Activity activity);
```

See example code if you want to roll your own.


>SDK 22 and older - proguard warnings

>If you generate apk’s with proguard with TargetSDK 22 or lower, you need to disable warnings for the new permission checks. Add the following line to your proguard-rules.pro file:
>
```
# inbeacon
-dontwarn com.inbeacon.sdk.**
```


## Receiving inBeaconSDK events 

The inBeacon event mechanism uses a LocalBroadcastManager and intents with actions. To listen to specific events, you need to create an intentfilter and a MessageReceiver like this: 

```java
IntentFilter myIntentFilter=new IntentFilter("com.inbeacon.sdk.event.enterregion");

LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,myIntentFilter);
```
The messageReceiver class can be defined like this:

```java
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras=intent.getExtras();
            Log.w("receiver", "Got action="+intent.getAction()+" extras="+extras);
        }
    };
```
also you could include more than one inbeacon event in the intentfilter to receive more than one action, like this (this intentfilter listens to all available inbeacon messages):

```java
IntentFilter myIntentFilter=new IntentFilter();

myIntentFilter.addAction("com.inbeacon.sdk.event.enterregion");     // user entered a region
myIntentFilter.addAction("com.inbeacon.sdk.event.exitregion");      // user left a region
myIntentFilter.addAction("com.inbeacon.sdk.event.enterlocation");   // user entered a location
myIntentFilter.addAction("com.inbeacon.sdk.event.exitlocation");    // user left a location
myIntentFilter.addAction("com.inbeacon.sdk.event.regionsupdate");   // region definitions were updated
myIntentFilter.addAction("com.inbeacon.sdk.event.enterproximity");  // user entered a beacon proximity
myIntentFilter.addAction("com.inbeacon.sdk.event.exitproximity");   // user left a beacon proximity
myIntentFilter.addAction("com.inbeacon.sdk.event.proximity");       // low level proximity update, once every second when beacons are around
myIntentFilter.addAction("com.inbeacon.sdk.event.appevent");        // defined in the backend for special app-specific pages to show
myIntentFilter.addAction("com.inbeacon.sdk.event.appaction");       // defined in the backend to handle your own local notifications
```
>Example:
>
```java
public class sdkTest extends Activity { 
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
}
```

### com.inbeacon.sdk.event.enterregion
Fired when device enters a region 

* intent Extras: region information

### com.inbeacon.sdk.event.exitregion

Fired when device leaves a region 

* intent Extras: region information

### com.inbeacon.sdk.event.enterlocation

Fired when device enters a location 

* intent Extras: location information

### com.inbeacon.sdk.event.exitlocation

Fired when device leaves a location 

* intent Extras: location information

### com.inbeacon.sdk.event.enterproximity

Fired when device enters a beacon proximity 

* intent Extra: Beacon and proximity information (distance to beacon)

### com.inbeacon.sdk.event.leaveproximity

Fired when device leaves a beacon proximity

* intent Extra: Beacon and proximity information (distance to beacon)


### com.inbeacon.sdk.event.proximity

>Low level beacon proximity event.

Fired when device proximity to a defined Beacon changes. This event is normally fired once every second when a beacon is in range

* intent Extras: Beacon information and proximity to beacon (near/far/immediate)

### com.inbeacon.sdk.event.regionsupdate

Fired when the region definition has been retrieved and updated from the server

* intent Extras: none.

### com.inbeacon.sdk.event.appevent

Special case: It is possible to have inBeacon fire a trigger that uses the app logic to handle the Activity that is activated when the user enters the notification. 

* intent Extras: Page information from this trigger with app-argument given in the backend:

![image alt text](image_4.png)

---