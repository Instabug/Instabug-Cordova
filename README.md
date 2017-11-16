Cordova Instabug Plugin
=================================

The purpose of this plugin is to simplify the process of integrating the Instabug SDK in a hybrid application, as well as to provide an interface to interact with the SDK through JavaScript.


### Installation
Currently, this plugin can only be installed via the Command-Line Interface.
```
cordova plugin add https://github.com/Instabug/instabug-cordova
```

## Usage
To initialize Instabug in your app, you need to do the following:

### Android
1. Change the name of the application class in your AndroidManifest.xml file to ```android:name="com.instabug.cordova.plugin.MyApplication"```.

2. You need to add your app token in the __MyApplication__ class, by replacing ```YOUR_ANDROID_TOKEN```. (You can find this class under this path  ```YourProjectName/plugins/com.instabug.cordova.plugin/src/android/MyApplication.java```)

3. You can change the invocation event by changing this line ```InstabugInvocationEvent.SHAKE``` in the __MyApplication__ class to any of the following:

```InstabugInvocationEvent.FLOATING_BUTTON```, ```InstabugInvocationEvent.SCREENSHOT_GESTURE```, ```InstabugInvocationEvent.TWO_FINGER_SWIPE_LEFT```, or ```InstabugInvocationEvent.NONE```.

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
 You can change the invocation event with any of the following: ```'button'```, ```'screenshot'```, ```'swipe'```, or ```'shake'```.

## License

This software is released under the <a href="http://opensource.org/licenses/Apache-2.0">Apache 2.0 License</a>.

© 2016 Instabug. All rights reserved.
