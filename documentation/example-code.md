## Sample app

You will find a sample Android Studio project in the SDK download package, and on github:

[https://github.com/inbeacon/inbeacon-sdk-android-example](https://github.com/inbeacon/inbeacon-sdk-android-example)

# Implementing the SDK

## Minimal implementation

You need to create your own application class to enable working with the inBeacon SDK.

In this class, initialize the inBeacon SDK from the onCreate of the application:

```java
import android.app.Application;
import com.inbeacon.sdk.InbeaconManager;
import java.util.HashMap;

public class myApp extends Application {

   @Override
	public void onCreate() {

       super.onCreate();

		// initialize with your ClientID and Secret.
		InbeaconManager.initialize(this, "<<your client Id>>", "<<client Secret>>");

		// refresh data from server. 
		InbeaconManager.getSharedInstance().refresh();
   }
}
```

You can find your client-ID and client-Secret in your account overview on [http://console.inbeacon.nl/accmgr ](http://console.inbeacon.nl/accmgr)

Now you have to make sure MyApplication is used as the application class by adding the following to your application in AndroidManifest.xml (in bold):

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.inbeacon.inbeaconsdkaartest" >

    <application android:name=".MyApp">
		...
    </application>

</manifest>
```

If you compile against Android SDK 23 (compileSdkVersion 23) you also need to ask the user permission to use the device COARSE_LOCATION. For convenience, the inBeacon SDK contains a method **askPermissions** to do this. Include this statement in your main activity (in bold):

```java
public class MyActivity extends Activity  { 

@Override

protected void onCreate(Bundle savedInstanceState) {
		...
		InbeaconManager.getSharedInstance().askPermissions(this);

```
**_This is all you have to do for a basic inBeacon SDK integration._** From now on you can manage proximity notifications with Beacons that you define in the inbeacon backend.  

Notice that even if you close the app or force-close the app, the inBeacon SDK service will restart at the moment you plug in or remove your charger or reboot the device. This means that Beacons that you define in the inBeacon backend are detected even if the user has put the app in the background or terminated the app. 

