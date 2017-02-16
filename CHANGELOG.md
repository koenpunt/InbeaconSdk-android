#### 2.0.5 (16-Feb-2017)
- dependency update with upstream altbeacon 2.9.2 


#### 2.0.4 (07-Feb-2017)
- some issues for older (pre-SDK23) versions fixed
 

#### 2.0.3 (07-Feb-2017)
- some issues for older android versions fixed (Adnroid 5 and lower)
 

#### 2.0.2 (06-Feb-2017)
- some minor issues solved


#### 2.0.1 (06-Feb-2017)
- Uses V5 API with host
- Locations and Geofences now use play-services-location
- okhttp replaces loopj async-http
- persistent user properties
- user tags
 

#### 1.7.3 (13-Jan-2017)
- upstream merge with altbeacon library 2.9.2
 

#### 1.7.2 (31-Oct-2016)
- Examples streamlined with 1.7 version
 

#### 1.7.1 (20-Oct-2016)
- Logging which device params are used 

#### 1.7.0 (20-Oct-2016)
- New distance calculation method
- Device parameter database now hosted by inbeacon
- Library crash due to unsufficient bluetooth permissions catched
- Distance calculation fix for ranges less than 1 meter


#### 1.6.4 (10-Oct-2016)
- markdown documentation in repository
- example in repository

#### 1.6.3 (10-Oct-2016)
- test 


#### 1.6.3 (07-Oct-2016)
- Added Release and Changelog info

#### 1.6.2
- Test availability of bluetooth before activating the SDK. This allows uses-permission-sdk-23 for BLUETOOTH and BLUETOOTH_ADMIN in order to enable the SDK only for android 6 and up.

#### version 1.6.1
- Android N / SDK 24 support
- Offline triggers without internet connection do not wait on server timeout for notification

#### version 1.4.0
- inBeacon v4 mobile api
- inBeacon Account setttings persisted on device
- New mobile endpoint  https://mapi.inbeacon.nl  used

#### version 1.3.21
- Utf-8 bugfix. User data with non-ASCII characters (utf-8 multibyte) was not transferred correctly SDK version 1.3.12
- Compile SDK 23. Support for android 6

#### version 1.3.11
- Shrink resources will not remove custom sounds SDK version 1.3.7 1.3.8 1.3.9 1.3.10
- bug fix: Various workarounds for android pendingintent bug
- new cha-ching and sadtrombone sounds

#### version 1.3.6
- bug fix: Back to default Holo theme when notification activity is started SDK version 1.3.5
- bug fix: async response on dead thread

#### version 1.3.4
- Regular Server updates when in region (ibeacon/update)
- Custom sounds, icons and colors of notifications
- Custom titlebar colors
- advertisement ID included in device/sync
- refresh when entering region.

#### version 1.3.3
- wakeup and background modes
- bluetooth timing & tuning
- Custom sounds

#### version 1.3.2
- bugfixes SDK version 1.3.1
- Support for fast bluetooth handling on Android 5

#### version 1.3.0
- inBeacon v3 mobile API
- updated android tools and API level 22
- use of jcenter for dependencies
- removal of inBeaconApplication class
- Android API 22

#### version 1.0.3
- bugfixes

#### version 1.0.1
- initial release


