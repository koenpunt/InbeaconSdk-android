# Examples

## Sample app

You will find a larger sample Android Studio project in the repository:

[Example project](https://github.com/inbeacon/InbeaconSdk-android/tree/master/example)


## Minimal implementation
There is only 1 method that is required for a minimal implementation: 

* initialize

You need to create your own **application class** to enable working with the inBeacon SDK.
> You can't initialize in an activity: the SDK will not work correctly in the background in this case

In this class, initialize the inBeacon SDK from the onCreate of the application:

```java
import android.app.Application;
import com.inbeacon.sdk.InbeaconManager;

public class myApp extends Application {

   @Override
	public void onCreate() {

       super.onCreate();

		// initialize with your ClientID and Secret.
		InbeaconManager.initialize(this, "<<your client Id>>", "<<client Secret>>");

   }
}
```

You can find your client-ID and client-Secret in your account overview on [https://console.inbeacon.nl/account ](https://console.inbeacon.nl/account)

Now you have to make sure MyApplication is used as the application class by adding the following to your application in AndroidManifest.xml (in bold):

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.inbeacon.inbeaconsdkaartest" >

    <application android:name=".MyApp">
		...
    </application>

</manifest>
```

You also need to ask the user permission to use the device FINE_LOCATION. For *convenience*, the inBeacon SDK contains a method **askPermissions** to do this. (of course you can roll your own if you want to)
 
Include this statement in your main activity:

```java
public class MyActivity extends Activity  { 

@Override

protected void onCreate(Bundle savedInstanceState) {
		...
		InbeaconManager.getSharedInstance().askPermissions(this);

```

**_This is all you have to do for a basic inBeacon SDK integration._** From now on you can manage proximity notifications with Beacons that you define in the inbeacon backend.  

>Notice that even if you close the app or force-close the app, the inBeacon SDK service will restart at the moment you plug in or remove your charger or reboot the device. This means that Beacons and geofences that you define in the inBeacon backend are detected even if the user has put the app in the background, swipes-out the app or resets the device. 

