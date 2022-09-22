## Unreleased

* Bumps Instabug Android SDK to v11.4.1
* Bumps Instabug iOS SDK to v11.2.0
* Adds TypeScript support

## v11.0.1 (2022-08-24)

* Fixes ArgsRegistry import on iOS
* Fixes a typo in Instabug.setPrimaryColor causing the API not to work

## v11.0.0 (2022-07-07)

* Bumps Instabug native SDKs to v11
* Adds the ability to initialize the Android SDK from JavaScript. Check the migration guide referenced in our docs
* Improves Instabug modules import usage. Check the migration guide referenced in our docs
* Migrates Instabug iOS SDK to be installed through CocoaPods
* Moves Instabug.setVideoRecordingFloatingButtonPosition to the BugReporting module
* Adds Instabug.setString API to allow for SDK text customizations
* Replaces Instabug.setShakingThreshold with BugReporting.setShakingThresholdForAndroid
* Adds new API BugReporting.setShakingThresholdForiPhone
* Adds new API BugReporting.setShakingThresholdForiPad
* Adds new API Instabug.setWelcomeMessageMode
* Adds new API Instabug.setColorTheme
* Adds new API Instabug.setSessionProfilerEnabled
* Adds new API BugReporting.setFloatingButtonEdge
* Fixes an issue with uploading attachments in URL form on iOS
* Fixes an issue with BugReporting.setEnabledAttachmentTypes not working on Android
* Removes the deprecated APIs. Check the migration guide referenced in our docs
* Removes the Android multidex dependency
* Removes Instabug.setAutoScreenRecordingMaxDuration
* Removes Surveys.setThresholdForReshowingSurveyAfterDismiss
* Removes BugReporting.invocationModes enum

## v9.1.7 (2021-05-11)

* Adds support for Azerbaijani and Danish locales
* Removes the use of `android:requestLegacyExternalStorage` attribute on Android
* Bumps iOS SDK to v9.1.7
* Bumps Android SDK to v9.1.8

## v9.1.6 (2020-07-20)

* Bump native SDKs to v9.1.6
* Fixes an issue with crashing UI calls running on background thread
* Fixes an issue where setLocale does not work on Android

## v9.1.0 (2020-03-19)

* Bump native SDKs to v9.1

## v9.0.0 (2020-03-10)

* Bump native SDKs to v9

## v8.6.5 (2019-09-06)

* Fixes various android issues

## v8.6.4 (2019-09-06)

* Fixes missing imports in android.
* Fixes floatingButtonEdge & floatingButtonOffset on android.
* Adds script to automatically add our maven repo to the project level `build.gradle`

## v8.6.3 (2019-09-05)

* Adds initializing android sdk from JS side using `cordova.plugins.instabug.activate`
* Updates native SDK to v8.6.2
