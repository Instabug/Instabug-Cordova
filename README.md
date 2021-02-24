# Instabug Cordova Plugin

The purpose of this plugin is to simplify the process of integrating the Instabug SDK in a hybrid application, as well as to provide an interface to interact with the SDK through JavaScript.

Instabug is a bug reporting and in-app feedback tool that provides your testers and users with easy “shake to send” feedback from within the app, to [report bugs and issues easily](https://instabug.com/bug-reporting). You’ll get attached screenshots, screen recordings, annotations, network logs to help you take insightful decisions. You’ll also receive all the device details, environment snapshots and bug reproduction steps so that you can fix bugs and iterate faster.

For more info, visit [Instabug.com](https://instabug.com).

## Installation

### Cordova:

```
cordova plugin add instabug-cordova
```

### Ionic:

```
ionic cordova plugin add instabug-cordova
```

## Android Integration Steps

1. Change the name of the application class in your AndroidManifest.xml file to `android:name="com.instabug.cordova.plugin.MyApplication"`.

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

⚠️ Starting from Instabug-Cordova v9, we require the `compileSdkVersion` to be set to a minimum of `29`. It can be set inside the app's `build.gradle` file as below:  

	android {
	    compileSdkVersion 29
	}

## iOS Integration Steps

You can initialize the SDK by using this method in your App JS file.

```
cordova.plugins.instabug.activate(
    {
        ios: 'MY_IOS_TOKEN'
    },
    'shake',
    {
    	commentRequired: true,
    	colorTheme: 'light'
    },
    function () {
        console.log('Instabug initialized.');
    },
    function (error) {
        console.log('Instabug could not be initialized - ' + error);
    }
);
```

You can change the invocation event with any of the following: `'button'`, `'screenshot'`, `'swipe'`, or `'shake'`.

⚠️  TypeScript users, make sure you declare `cordova` at the beginning of your app class (app.component.ts):

	declare let cordova: any;

## Features Not Yet Supported
- User steps.
- Repro steps.
- Network logging.
- View hierarchy.
- Crash reporting.
- Push notification for in-app messaging.

## License

This software is released under the <a href="http://opensource.org/licenses/Apache-2.0">Apache 2.0 License</a>.

© 2016 Instabug. All rights reserved.
