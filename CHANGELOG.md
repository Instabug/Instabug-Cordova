
## 11.8.0 (2025-02-04)
* Bumps Instabug iOS SDK to `v12.1.0`

## 11.7.0 (2023-02-02)

* Bumps Instabug Android SDK to `v11.8.0`
* Bumps Instabug iOS SDK to `v11.7.0`
* Adds new APIs:
  1. `BugReporting.setDisclaimerText`
  2. `BugReporting.setCommentMinimumCharacterCount`
* Adds support for more locales:
  1. Czech
  2. Finnish
  3. Hungarian
  4. Indonesian (iOS)
  5. Norwegian
  6. PortuguesePortugal
  7. Romanian
  8. Slovak
* Adds new string keys:
  1. insufficientContentMessage
  2. insufficientContentTitle (iOS)
  3. screenRecording
* Adds missing mapping for the below string keys on Android:
  1. audio
  2. image
  3. messagesNotificationAndOthers
  4. okButtonTitle

## 11.3.0 (2022-10-05)

* Bumps Instabug Android SDK to `v11.5.1`
* Bumps Instabug iOS SDK to `v11.3.0`

## 11.2.0 (2022-09-25)

* Bumps Instabug Android SDK to `v11.4.1`
* Bumps Instabug iOS SDK to `v11.2.0`
* Adds TypeScript support

## v11.0.1 (2022-08-24)

* Fixes ArgsRegistry import on iOS
* Fixes a typo in `Instabug.setPrimaryColor` causing the API not to work

## v11.0.0 (2022-07-07)

* Bumps Instabug native SDKs to `v11`
* Adds the ability to initialize the Android SDK from JavaScript. Check the [migration guide][migration-guide-v11] for further info
* Improves Instabug modules import usage. Check the [migration guide][migration-guide-v11] for further info
* Migrates Instabug iOS SDK to be installed through CocoaPods
* Adds new APIs:
  1. `Instabug.setString` to allow for SDK text customizations
  2. `Instabug.setWelcomeMessageMode`
  3. `Instabug.setColorTheme`
  4. `Instabug.setSessionProfilerEnabled`
  5. `BugReporting.setShakingThresholdForiPhone`
  6. `BugReporting.setShakingThresholdForiPad`
  7. `BugReporting.setFloatingButtonEdge`
* Replaces `Instabug.setShakingThreshold` with `BugReporting.setShakingThresholdForAndroid`
* Moves `Instabug.setVideoRecordingFloatingButtonPosition` to the `BugReporting` module
* Fixes an issue with uploading attachments in URL form on iOS
* Fixes an issue with `BugReporting.setEnabledAttachmentTypes` not working on Android
* Removes the deprecated APIs. Check the [migration guide][migration-guide-v11] for further info
* Removes the Android multidex dependency
* Removes the APIs:
  1. `Instabug.setAutoScreenRecordingMaxDuration`
  2. `Surveys.setThresholdForReshowingSurveyAfterDismiss`
* Removes `BugReporting.invocationModes` enum

[migration-guide-v11]: https://docs.instabug.com/docs/cordova-migration-guide

## v9.1.7 (2021-05-11)

* Bumps iOS SDK to `v9.1.7`
* Bumps Android SDK to `v9.1.8`
* Adds support for Azerbaijani and Danish locales
* Removes the use of `android:requestLegacyExternalStorage` attribute on Android

## v9.1.6 (2020-07-20)

* Bumps native SDKs to `v9.1.6`
* Fixes an issue with crashing UI calls running on background thread
* Fixes an issue where `setLocale` does not work on Android

## v9.1.0 (2020-03-19)

* Bumps native SDKs to `v9.1`

## v9.0.0 (2020-03-10)

* Bumps native SDKs to `v9`

## v8.6.5 (2019-09-06)

* Fixes various Android issues

## v8.6.4 (2019-09-06)

* Fixes missing imports on Android
* Fixes `floatingButtonEdge` & `floatingButtonOffset` on Android
* Adds script to automatically add our maven repo to the project level `build.gradle`

## v8.6.3 (2019-09-05)

* Adds initializing Android SDK from JS side using `cordova.plugins.instabug.activate`
* Updates native SDKs to v8.6.2
