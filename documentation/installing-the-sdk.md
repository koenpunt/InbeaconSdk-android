## Integrating inBeacon in an Android Studio project

### JCenter (recommended)

Add JCenter to your build file's list of repositories, and include

```groovy
dependencies {
	compile('com.inbeacon:android.sdk:1.+@aar'){ transitive = true }
}
```
to your gradle dependencies. See bintray for details: [https://bintray.com/inbeacon/maven/android.sdk/view](https://bintray.com/inbeacon/maven/android.sdk/view)

You can use a dynamic version (1.+) to get the latest 1.x version of the SDK (recommended)

### Binary release 

If you don’t want to use the JCenter repository, you can download and include all files manually from the [repository](https://github.com/inbeacon/InbeaconSdk-android).

To include the .aar in your android studio project, copy the aar file to the app/libs directory and include:

```groovy
repositories {
   flatDir {
       dirs 'libs'
   }
}

compile 'com.android.support:support-v4:22.2.0'
compile 'org.altbeacon:android-beacon-library:2.8.1'
compile 'com.loopj.android:android-async-http:1.4.9'
compile(name:'android.sdk-release', ext:'aar')
```

In this case you need to specify some dependencies by hand.

Now you are set to go. Try to compile and see that your app is still working. Now you need to add some code to start the inBeacon SDK.