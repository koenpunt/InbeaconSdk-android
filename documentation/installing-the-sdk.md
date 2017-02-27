## Integrating inBeacon in an Android Studio project

### JCenter (recommended)

Add JCenter and the inbeacon repository to your build file's list of repositories:

```groovy
    repositories {
        jcenter()
        maven { url "https://dl.bintray.com/inbeacon/maven" }
        ..
    }
```
> you can have more than one maven {url ..} line  in your repositories list

Now include:    

```groovy
dependencies {
	compile('com.inbeacon:android.sdk:2.+@aar'){ transitive = true }
}
```
to your gradle dependencies. See bintray for details: [https://bintray.com/inbeacon/maven/android.sdk/view](https://bintray.com/inbeacon/maven/android.sdk/view)

You can use a dynamic version (2.+) to get the latest 2.x version of the SDK (recommended) 
>Don't forget transitive = true. Because we specify @aar, transitive no longer defaults to true

### Binary release 

If you don’t want to use the JCenter repository, you can download and include all files manually from the [repository](https://github.com/inbeacon/InbeaconSdk-android).

To include the .aar in your android studio project, copy the aar file to the app/libs directory and include:

```groovy
repositories {
   flatDir {
       dirs 'libs'
   }
}

compile 'com.android.support:support-v4:25.0.0'         
compile 'com.inbeacon:android-beacon-library:2.9.118'
compile 'com.google.code.gson:gson:2.8.0'
compile 'com.squareup.okhttp3:okhttp:3.4.2'
compile 'com.squareup.okhttp3:logging-interceptor:3.4.2'
compile 'com.google.android.gms:play-services-location:10.0.1'
compile(name:'android.sdk-release', ext:'aar')
```

In this case you need to specify some dependencies by hand.

Now you are set to go. Try to compile and see that your app is still working. Now you need to add some code to start the inBeacon SDK.

### Proguard rules

The SDK proguard rules are automatically merged into your project.