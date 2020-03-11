# Instabug Cordova Plugin

The purpose of this plugin is to simplify the process of integrating the Instabug SDK in a hybrid application, as well as to provide an interface to interact with the SDK through JavaScript.

Instabug is a bug reporting and in-app feedback tool that provides your testers and users with easy “shake to send” feedback from within the app, to [report bugs and issues easily](https://instabug.com/bug-reporting). You’ll get attached screenshots, screen recordings, annotations, network logs to help you take insightful decisions. You’ll also receive all the device details, environment snapshots and bug reproduction steps so that you can fix bugs and iterate faster.

For more info, visit [Instabug.com](https://instabug.com).

### Installation

Currently, this plugin can only be installed via the Command-Line Interface.

```
cordova plugin add instabug-cordova
```

##### Ionic

If you're using this plugin with Ionic, you can install using this command.

```
ionic cordova plugin add instabug-cordova
```

## Usage

To initialize Instabug in your app, you need to do the following:

### Android

1. Change the name of the application class in your AndroidManifest.xml file to `android:name="com.instabug.cordova.plugin.MyApplication"`.

Since `Cordova Android 7.0.0` AndroidManifest.xml position has changed and it is placed under `app/src/main/AndroidManifest.xml` ([read more](http://cordova.apache.org/announcements/2017/12/04/cordova-android-7.0.0.html)). You have to point it in your `.gradle` file in `platforms\android\com.instabug.cordova.plugin` directory in this way:

```
    sourceSets {
        main {
            manifest.srcFile 'src/main/AndroidManifest.xml'
        }
    }
```

2. You need to add your app token in the **MyApplication** class, by replacing `YOUR_ANDROID_TOKEN`. (You can find this class under this path `YourProjectName/platforms/android/app/src/main/java/com.instabug.cordova.plugin/MyApplication.java`)

3. You can change the invocation event by changing this line `InstabugInvocationEvent.SHAKE` in the **MyApplication** class to any of the following:

`InstabugInvocationEvent.FLOATING_BUTTON`, `InstabugInvocationEvent.SCREENSHOT_GESTURE`, `InstabugInvocationEvent.TWO_FINGER_SWIPE_LEFT`, or `InstabugInvocationEvent.NONE`.

4.  Make sure the following snippet is added to your project level `build.gradle`, if not you can manually add it as follows:.

```dart
    allprojects {
	repositories {
	    maven {
	        url "https://sdks.instabug.com/nexus/repository/instabug-cp"
	    }
	}
    }
```

### iOS

You can initialize the SDK by using this method in your JS class

```
cordova.plugins.instabug.activate(
    {
        ios: 'MY_IOS_TOKEN'
    },
    'shake',
    function () {
        console.log('Instabug initialized.');
    },
    function (error) {
        console.log('Instabug could not be initialized - ' + error);
    }
);
```

You can change the invocation event with any of the following: `'button'`, `'screenshot'`, `'swipe'`, or `'shake'`.

##### Ionic

If you're using this plugin with Ionic, you need to add this line at the beginning of your **app.component.ts** class.

```
declare let cordova: any;
```

## License

This software is released under the <a href="http://opensource.org/licenses/Apache-2.0">Apache 2.0 License</a>.

© 2016 Instabug. All rights reserved.
