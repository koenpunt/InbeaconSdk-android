# Considerations for integration

Before starting, lets take a look at some important considerations

## Permissions 

For the SDK to work, the app needs the following (extra) permissions:

* `BLUETOOTH`
* `BLUETOOTH_ADMIN`
* `RECEIVE_BOOT_COMPLETED`
* `ACCESS_COARSE_LOCATION`  (api 23 / android 6 only)

How these permissions impact installation and use of the app differs per android version and per targeted API of the app.

#### Older android versions
* Android versions 5 (lollipop) and lower 
* **OR** Apps targeted at API versions 22 and lower 
	
Android versions 5 and lower ask for all permissions during install. 
	
Also, on android 6, older apps targeted for API versions 22 and lower will have the same behaviour, as these are grandfathered into the new adroid version.
	
##### Installation
	
On installation, the app asks for the extra permission "Bluetooth" 
	
##### App update
	
If the app is updated and did not yet have permission for one of the bluetooth values, the app is **NOT** automatically updated because of the extra permission needed. Updating leads to the following question

#### Android 6 and higher
* Android versions 6 (marshmallow) and up
* **AND** with apps targeted at API versions 23 onwards

>Installs and updates for the these apps are done without asking for permissions. 

>Permissions are asked during use of the app. In the "read more" section, it is mentioned that the app “shares location” because of the extra COARSE_LOCATION permission requirement.

![image alt text](image_1.png)

And in the "permission details" menu, it is mentioned that the app can use bluetooth functionaties and run at startup. However this list must requested by the user and is not shown upon install by default.

![image alt text](image_2.png)

When the app is run, permission for `COARSE_LOCATION` (or `FINE_LOCATION`) needs to be requested in order to detect beacons:

![image alt text](image_3.png)

## App in the background and locked (screen off) devices

Special care is taken to allow beacon scanning to continue in the background. 

* When the app activity is closed (back-button) the SDK goes into background mode. In this mode, beacon scanning is slower to preserve battery, but still every beacon is detected within 10 seconds

* When the phone is locked (screen off) the SDK works in background mode

* When the app is swiped-out from the task menu, an alert is set to wake the app again after 5 minutes. In this case the app is started in background mode only, without activity so it is not shown on the task menu again

* Unplugging or plugging in the USB charger has the same effect as the 5 minutes alert. It will restart the app in background mode immdiately after it is swiped-out from the task menu

* When the app is force-stopped the SDK becomes inactive. The 5 minute resume or the usb plugin trigger are disabled. Scanning will restart only after a fresh app restart.

* DOZE mode. Android 6 Marshmellow introduces doze mode where the device cycles through `IDLE` and `IDLE_MAINTENANCE` mode when the device is inactive and on battery. In IDLE mode, internet connection is off, so apps have to wait for an `IDLE_MAINTENANCE` window in order to connect to backends. Doze mode does not have any impact on beacon scanning, but without internet connection devices will not be able to process on-line trigger events.

However because Doze-mode is only activated for devices that are not moving (GPS and accelerometer) (for instance lying on a table) we expect doze mode not to have any practical impact on the SDK as people will probably will be on the move during interactions.  

## Battery life

The inbeacon SDK uses sleep-mode whenever there are no beacons with your ID around. This means that we can limit extra battery usage during normal operation. Bluetooth scanning takes place only for a few seconds every minute. On average we see a decrease of battery life of aboutf 1-2% for the device. 

When beacons with your ID are within range (approx 30 meters),  bluetooth scanning is done continuously, and battery use increases as well. Of course, only a few people will be within the ranges of your beacons, and also for a short time. 

>The inbeacon SDK only wakes up for **_your beacons_** (beacon UUID’s defined in the region table of your account)  Other beacons have no influence on the inbeacon SDK.

## Dex method footprint

Because of the 65K method limit for android applications, large applications that add a lot of SDK’s might become too large containing more than 65K methods

The number of methods used by the inBeaconSDK is 793 methods (version 1.3.4)

This is excluding the dexcount of the dependencies as they will probably already be used:

* com.android.support:support-v4
* com.loopj.android:android-async-http

## Memory footprint

classes.jar: 50k

```
4.0K	contents//aapt
  0B	contents//aidl
  0B	contents//assets
8.0K	contents//res/drawable-hdpi
4.0K	contents//res/drawable-ldpi
 68K	contents//res/drawable-mdpi
 84K	contents//res/drawable-xhdpi
104K	contents//res/drawable-xxhdpi
188K	contents//res/drawable-xxxhdpi
4.0K	contents//res/layout
680K	contents//res/raw
1.1M	contents//res
1.2M	contents/
```
Difference between compiled apk with and without inBeaconSDK: 970k (including icon and sound resources)

