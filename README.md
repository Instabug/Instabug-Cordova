Instabug Cordova Plugin
=================================

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
1. You can initialize the SDK by using this method in your JS class
```
cordova.plugins.instabug.activate(
    {
        token: 'MY_TOKEN'
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

2.  Make sure to add the following snippet to your project level `build.gradle`.
```dart
allprojects {
	repositories {
	    maven {
	        url "https://sdks.instabug.com/nexus/repository/instabug-cp"
	    }
	}
}
```
 ##### Ionic
If you're using this plugin with Ionic, you need to add this line at the beginning of your __app.component.ts__ class.

```
declare let cordova: any;
```

## License

This software is released under the <a href="http://opensource.org/licenses/Apache-2.0">Apache 2.0 License</a>.

© 2016 Instabug. All rights reserved.
