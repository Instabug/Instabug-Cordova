Cordova Instabug Plugin
=================================

The purpose of this plugin is to simplify the process of integrating the Instabug SDK in a hybrid application, as well as to provide an interface to interfacing with the SDK through JavaScript.

### Supported Platforms
- __iOS__ (SDK >= ?)
- __Android__ (SDK >= 10)

### Installation
Currently, this plugin can only be installed via the Command-Line Interface.
```
cordova plugin add https://github.com/Instabug/instabug-cordova
```
## Usage
To initialize Instabug in your app, you need to do the following: 

### Android
Change the name of the application class in your manifest file to ```android:name="com.instabug.cordova.plugin.MyApplication"```. You only need to add your app token in the __MyApplication__ class, by replacing ```YOUR_ANDROID_TOKEN```. You can change the invocation event by changing this line ```InstabugInvocationEvent.FLOATING_BUTTON``` in the __MyApplication__ class to any of the following: 

```InstabugInvocationEvent.SHAKE```, ```InstabugInvocationEvent.SCREENSHOT_GESTURE```, ```InstabugInvocationEvent.TWO_FINGER_SWIPE_LEFT```, or ```InstabugInvocationEvent.NONE```. 

### iOS
You can initialize the SDK by using this method in your JS class
```
cordova.plugins.instabug.activate(
    {
        android: 'MY_ANDROID_TOKEN',
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
 You can change the invocation event with any of the following: ```'button'```, ```'screenshot'```, ```'swipe'```, or ```'swipe'```.

## Tips & tricks
1. You should initialize Instabug as soon as you can in your app. For Ionic users, the best place is in the run method of your module.
2. For Android, if you use other plugins that open dialogs (like InAppBrowser) that you will want included in your screenshots, you'll need to modify the plugin code itself to call Instabug.setDialog(Dialog dialog) when it is opened. See Instabug official documentation for more info on usage (or, you can always check out my <a href="https://github.com/elizabethrego/cordova-plugin-inappbrowser-instabug">InAppBrowser fork</a>, which includes this functionality as well as other useful things like hiding an open browser).
3. The Instabug SDK also offers a reportException method that is not accessible through JavaScript but may be useful for adding these exceptions to your reports if you modify plugins to call it.

## Contributing (please do!)

1. Fork this repo.
2. Create your own branch. (`git checkout -b my-feature`)
3. Commit your changes. (`git commit -am 'Add my awesome feature'`)
4. Push to your branch. (`git push origin my-feature`)
5. Create new pull request.


## License

This software is released under the <a href="http://opensource.org/licenses/Apache-2.0">Apache 2.0 License</a>.

© 2016 Instabug. All rights reserved.

