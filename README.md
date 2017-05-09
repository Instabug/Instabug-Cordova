Cordova Instabug Plugin
=================================

The purpose of this plugin is to simplify the process of integrating the Instabug SDK in a hybrid application, as well as to provide an interface to interfacing with the SDK through JavaScript.

### Supported Platforms
- __iOS__ (SDK >= ?)
- __Android__ (SDK >= 10)


### iOS preinstallation 

Open the terminal then navigate to your project's directory then type `npm i xcode`

### Installation
Currently, this plugin can only be installed via the Command-Line Interface.
```
cordova plugin add https://github.com/Instabug/instabug-cordova
```
or
```
cordova plugin add instabug-cordova
```
### After uninstalling Instabug plugin

Open your ios `*.xcworkspace` then navigate to Build Phases in your project's target and remove the following two phases:
* Embed Frameworks com.instabug.cordova.plugin
* StripFrameworkScript

## Usage
To initialize Instabug in your app, you only need to make one call to the plugin: __activate__. This method requires your app's token and your desired invocation event, and can take a wide range of optional parameters for configuration.

Your app's token (available on your Instabug dashboard online) can be specified as a string or an object containing properties according to platform:

```javascript
var token = 'YOUR_TOKEN_HERE';
```

or

```javascript
var tokens = {
android: 'YOUR_ANDROID_TOKEN_HERE',
ios: 'YOUR_IOS_TOKEN_HERE'
};
```

The invocation event can be specified as one of the following values:


| value | native equivalent | description | iOS only? |
|:------------:|:-------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------:|
| 'shake' | InstabugInvocationEvent.SHAKE | Shaking the device while in any screen to show the feedback form. |  |
| 'button' | InstabugInvocationEvent.FLOATING_BUTTON | Shows a floating button on top of all views, when pressed it takes a screenshot. |  |
| 'screenshot' | InstabugInvocationEvent.SCREENSHOT_GESTURE | Taking a screenshot using the Home+Lock buttons while in any screen to show the feedback form, substituted with IBGInvocationEventShake on iOS 6.1.3 and earlier. | √ |
| 'swipe' | InstabugInvocationEvent.TWO_FINGER_SWIPE_LEFT | Swiping two fingers left while in any screen to show the feedback form. | √ |
| 'none' | InstabugInvocationEvent.NONE | No event will be registered to show the feedback form, you'll need to code your own and call the method invoke. |  |
Like the token, the value itself can be provided, or you can give an object with values according to platforms using the same syntax as above.

The final parameter to the activate method (besides success and error callback functions) is an optional object containing properties to set a variety of configurations. None of these configurations are required. See the table below for their specifications.

| property | description | possible values | default value | Android? | iOS? |
|--------------------------|-------------------------------------------------------------------------------------------|----------------------------------|---------------|----------|------|
| emailRequired | Sets whether email field is required or not when submitting bug/feedback | true, false | true | √ | √ |
| commentRequired | Sets whether comment field is required or not when submitting bug/feedback | true, false | false | √ | √ |
| defaultInvocationMode | Sets which invocation mode should the SDK use by default when the invocation event occurs | 'bug','feedback', 'na' | 'na' | √ | √ |
| shakingThresholdAndroid | Sets the threshold value of the shake gesture on the device | String representation of int | '11' | √ |  |
| shakingThresholdIPhone | Sets the threshold value of the shake gesture on the device | String representation of double | '2.5' |  | √ |
| shakingThresholdIPad | Sets the threshold value of the shake gesture on the device | String representation of double | '0.6' |  | √ |
| floatingButtonEdge | Sets the default edge at which the floating button will be shown | 'right', 'left' | 'right' | √ | √ |
| floatingButtonOffset | Sets the default offset of the floating button from the top of the screen | String representation of integer | '-1' | √ | √ |
| enableDebug | Enable/Disable debug logs from Instabug SDK | true, false | false | √ |  |
| enableConsoleLogs | Enable/disable console log to be added to reports | true, false | true | √ |  |
| enableInstabugLogs | Enable/disable Instabug log to be added to reports | true, false | true | √ |  |
| enableTrackingUserSteps* | Enable/disable automatic user steps tracking | true, false | true | √ | √ |
| enableCrashReporting* | Enable/disable crash reporting | true, false | true | √ | √ |
| enableInAppMessaging | Enable/Disable in-app messaging | true, false | true | √ |  |
| enableConversationSounds | Set whether new messages received will trigger a small sound notification or not | true, false | false | √ |  |
| enablePushNotifications | Enable/disable push notifications feature | true, false | true | √ | √ |
| enableIntroDialog | Enable/disable intro dialog shown the first time app is opened | true, false | true | √ | √ |
| enableUserData | Enable/disable user data to be added to reports | true, false | true | √ |  |
| colorTheme | Set which color theme to use for the SDK's UI | 'light', 'dark' | 'light' | √ |  |
| bugReportEnabled | A boolean to indicate whether bug reports are enabled or disabled. | true, false | true | √ |  |
| feedbackEnabled | A boolean to indicate whether feedback is enabled or disabled. | true, false | true | √ |  |
| chatEnabled |  A boolean to indicate whether chat is enabled or disabled. | true, false | true | √ |  |
| viewHierarchyEnabled | A boolean to set whether view hierarchy are enabled or disabled. | true, false | true | √ |  |
| willSkipScreenShot | A boolean to set whether screenshot view is shown or not. Passing YES will show | true, false | true | √ |  |
| isPostSendingDialogEnabled | A boolean to indicate whether the dialog is enabled or not.| true, false | true | √ |  |
| screenShot | A boolean to enable or disable screenshot attachments. | true, false | true | √ |  |
| extraScreenShot | A boolean to enable or disable extra screenshot attachments. | true, false | true | √ |  |
| galleryImage | A boolean to enable or disable gallery image attachments. In iOS 10+, NSPhotoLibraryUsageDescription should be set in info.plist to enable gallery image attachments. | true, false | true | √ |  |
| voiceNote | A boolean to enable or disable voice note attachments. In iOS 10+, NSMicrophoneUsageDescription should be set in info.plist to enable voiceNote attachments. | true, false | true | √ |  |
| screenRecording | A boolean to enable or disable screen recording attachments. | true, false | true | √ |  |
| chatNotificationEnabled | A boolean to set whether notifications are enabled or disabled. | true, false | true | √ |  |
*Pro feature

## Sample
The sample demonstrates how to initiate Instabug.

```javascript
cordova.plugins.instabug.activate(
{
android: 'MY_ANDROID_TOKEN',
ios: 'MY_IOS_TOKEN'
},
'shake',
{
commentRequired: true,
colorTheme: 'dark',
shakingThresholdIPhone: '1.5',
shakingThresholdIPad: '0.6',
enableIntroDialog: false
},
function () {
console.log('Instabug initialized.');
},
function (error) {
console.log('Instabug could not be initialized - ' + error);
}
);
```

## Other actions
After you've initialized Instabug, you can call a variety of other methods on the plugin object. Each of these methods takes success and error callback function parameters, specified *after* any parameters detailed in the table below. If a parameter is listed as optional and you do not wish to supply it, pass null instead, otherwise your callback functions won't run.

| method | description | parameters | Android? | iOS? |
|-----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|------|
| invoke | Show the Instabug dialog so user can choose to report a bug, or submit feedback | optional: 'bug', 'feedback', or 'na' | √ | √ |
| showIntro* | Display the discovery dialog explaining the shake gesture or the two finger swipe gesture | none | √ | √ |
| setPrimaryColor | Set the primary color that the SDK will use to tint certain UI elements in the SDK | integer representation of a color . | √ | √ |
| setUserEmail* | If your app already acquires the user's email address and you provide it to this method, Instabug will pre-fill the user email in reports | email address | √ | √ |
| setUserName* | Sets the user name that is used in the dashboard's contact | name | √ | √ |
| setUserData* | Adds specific user data that you need to be added to the reports | any string | √ | √ |
| addFile* | Uploads file along upcoming reports | Path to desired file on device - can't use path of file relative to your application files. Can be specified as a string or an object with properties by platform. | √ | √ |
| addLog* | Appends a log message to Instabug internal log. These logs are then sent along the next uploaded report. All log messages are timestamped. Logs aren't cleared per single application run. If you wish to reset the logs, use clearLog(). Note: logs passed to this method are NOT printed to Logcat. | any string | √ | √ |
| logVerbose | Adds custom logs with the verbose log level. Logs will be sent with each report. | none |  | √ |
| logDebug | Adds custom logs with the debug log level. Logs will be sent with each report. | none |  | √ |
| logInfo | Adds custom logs with the info log level. Logs will be sent with each report. | none |  | √ |
| logWarn | Adds custom logs with the warn log level. Logs will be sent with each report. | none |  | √ |
| logError | Adds custom logs with the error log level. Logs will be sent with each report. | none |  | √ |
| setUserAttributes | Set custom user attributes that are going to be sent with each feedback, bug or crash. | none |  | √ |
| userAttributeForKey | Returns the user attribute associated with a given key. | none |  | √ |
| removeUserAttributeForKey | Removes a given key and its associated value from user attributes. | none |  | √ |
| appendTags | Appends a set of tags to previously added tags of reported feedback, bug or crash. | none |  | √ |
| resetTags | Manually removes all tags of reported feedback, bug or crash. | none |  | √ |
| getTags | Gets all tags of reported feedback, bug or crash. | none |  | √ |
| identifyUserWithEmailAndName | Sets the user email and name for all sent reports. Also hides the email field from the reporting UI. | none |  | √ |
| logout | Sets the default value of the user's email to nil and show email field and remove user name from all reports | none |  | √ |
| logUserEventWithName | Logs a user event that happens through the lifecycle of the application. | none |  | √ |
| logUserEventWithNameAndParams | Logs a user event that happens through the lifecycle of the application. | none |  | √ |
| setReportCategoriesWithTitles | Sets an array of report categories to be shown for users to select from before reporting a bug or sending feedback. | none |  | √ |
| clearLog | Clears Instabug internal log | none | √ |  |
| changeInvocationEvent | Changes the event used to invoke Instabug SDK | 'shake', 'button', 'screenshot', 'swipe', or 'none' (see first table on page) | √ | √ |
| disable | Disables all Instabug functionality | none | √ |  |
| enable | Enables all Instabug functionality | none | √ |  |
| isEnabled | Returns true if Instabug is enabled, false if it's disabled | none | √ |  |
| isInvoked | Returns if Instabug is currently invoked (shown) or not | none | √ |  |
| isDebugEnabled | Returns if Instabug SDK debug logs will be added to LogCat logs or not | none | √ |  |
| setLocale | Set the locale used to display the strings in the correct language | 'arabic', 'chineseSimplified', 'chineseTraditional', 'czech', 'danish', 'dutch', 'english', 'french', 'german', 'italian', 'japanese', 'korean', 'norwegian', 'polish', 'portugueseBrazil', 'russian', 'slovak', 'spanish', 'swedish', or 'turkish' |  | √ |
*Pro feature

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

