# Examples

## Sample app

You will find a sample Android Studio project in the repository:

[Example project](https://github.com/inbeacon/InbeaconSdk-android/tree/master/example)


## Minimal implementation
There are 3 sdk methods that are required for a minimal implementation: 

* initialize
* refresh
* askPermissions (SDK 23 and up)

You need to create your own **application class** to enable working with the inBeacon SDK.
> You can't initialize in an activity: the SDK will not work correctly in the background in this case

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

You also need to ask the user permission to use the device COARSE_LOCATION. For convenience, the inBeacon SDK contains a method **askPermissions** to do this. Include this statement in your main activity:

```java
public class MyActivity extends Activity  { 

@Override

protected void onCreate(Bundle savedInstanceState) {
		...
		InbeaconManager.getSharedInstance().askPermissions(this);

```

**_This is all you have to do for a basic inBeacon SDK integration._** From now on you can manage proximity notifications with Beacons that you define in the inbeacon backend.  

>Notice that even if you close the app or force-close the app, the inBeacon SDK service will restart at the moment you plug in or remove your charger or reboot the device. This means that Beacons that you define in the inBeacon backend are detected even if the user has put the app in the background or terminated the app. 

## custom code for getting Permission 

If you do not want to use the bare-bones **askPermissions** method provided by the SDK, you can roll your own. You might use this example code to make a more advanced permission request:

```java
public class myActivity extends Activity  { 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		...
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		   // Android M Permission check 
		   if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
		       final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		       builder.setTitle("This app needs location access");
		       builder.setMessage("Please grant location access so this app can detect beacons.");
		       builder.setPositiveButton(android.R.string.ok, null);
		       builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
		           @Override
		           public void onDismiss(DialogInterface dialog) {
		               requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
		           }
		       });
		       builder.show();
		   }
		}
		..
	}
	@Override
	public void onRequestPermissionsResult(int requestCode,
	                                      String permissions[], int[] grantResults) {
	   switch (requestCode) {
	       case PERMISSION_REQUEST_COARSE_LOCATION: {
	           if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	               Log.d(TAG, "coarse location permission granted");
	           } else {
	               final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	               builder.setTitle("Functionality limited");
	               builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
	               builder.setPositiveButton(android.R.string.ok, null);
	               builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
	
	                   @Override
	                   public void onDismiss(DialogInterface dialog) {
	                   }
	
	               });
	               builder.show();
	           }
	           return;
	       }
	   }
	}
	…
}
```
