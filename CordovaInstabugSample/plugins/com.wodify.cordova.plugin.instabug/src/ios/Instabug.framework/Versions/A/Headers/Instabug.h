/*
 File:       Instabug/Instabug.h

 Contains:   API for using Instabug's SDK.

 Copyright:  (c) 2014 by Instabug, Inc., all rights reserved.

 Version:    5.0.0
 */

//===========================================================================================================================================
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "IBGEnums.h"
//===========================================================================================================================================

/**
 *  This is the API for using Instabug's SDK, for more details about the SDK integration, please visit http://instabug.com/sdk-integration
 */
@interface Instabug : NSObject

//===========================================================================================================================================
/** @name SDK Initialization */
//===========================================================================================================================================
/**
 *  Starts the SDK
 *
 *  This is the main SDK method that does all the magic. This is the only method that SHOULD be called.
 *  Should be called at the end of the method application:didFinishLaunchingWithOptions:
 *  
 *  @param token           The token that identifies the app, you can find it on your dashboard
 *  @param invocationEvent The event that invocates the feedback form
 */
+ (void)startWithToken:(NSString *)token invocationEvent:(IBGInvocationEvent)invocationEvent;

//===========================================================================================================================================

//===========================================================================================================================================
/** @name SDK Manual Invocation */
//===========================================================================================================================================

/**
 *  Shows the form with the prompt asking whether to report a bug or send feedback
 */
+ (void)invoke;

/**
 *  Invoke the SDK's either bug reporter or feedback sender UIs
 *
 *  @param invocationMode invocationMode
 */
+ (void)invokeWithInvocationMode:(IBGInvocationMode)invocationMode;

//===========================================================================================================================================

//===========================================================================================================================================
/** @name SDK Pro Features */
//===========================================================================================================================================

/**
 *  Attaches a new copy of this file with each bug report sent with a maximum size of 1 MB. Calling this method several times overrides the file to be attached. The file has to be stored locally at the location provided
 *
 *  @param fileLocation fileLocation
 */
+ (void)setFileAttachment:(NSString *)fileLocation;

/**
 *  Sets the user data that's attached with each bug report sent. Maximum size of the string is 1000 characters
 *
 *  @param userData userData
 */
+ (void)setUserData:(NSString *)userData;

/**
 *  Adds custom logs that will be sent with each report
 *
 *  @param format format
 *  @param ...    ...
 */
void IBGLog(NSString *format, ...);

/**
 *  Adds custom logs that will be sent with each report. A convenience method for Swift, identical to IBGLog().
 *
 *  Put following function in your swift project:
 *  func IBGLog(str: String, _ arguments: CVarArgType...) -> Void {
 *      return withVaList(arguments) { Instabug.IBGLog(str, withArguments :$0) }
 *  }
 *  And use it like this IBGLog("%@ - %d - %0.1f", "foo", 3, 3.0)
 *
 *  @param format    format
 *  @param arguments arguments
 */
+ (void)IBGLog:(NSString *)format withArguments:(va_list)arguments;

/**
 *  Sets whether to track the user's steps while using the app or not
 *
 *  Default = YES
 *  @param isUserStepsEnabled isUserStepsEnabled
 */
+ (void)setUserStepsEnabled:(BOOL)isUserStepsEnabled;

/**
 *  Sets whether to track the crashes in the app or not
 *
 *  Default = YES
 *  @param isReportingCrashes isReportingCrashes
 */
+ (void)setCrashReportingEnabled:(BOOL)isReportingCrashes;

/**
 *  Sets the block of code that gets executed just before sending the bug report.
 *
 *  @param preSendingBlock preSendingBlock
 */
+ (void)setPreSendingBlock:(void (^)())preSendingBlock;

/**
 * Presents a quick tip UI educating the user on how to invoke SDK with the currently set invocation event
 */
+ (void)showIntroMessage;

/**
 *  Sets the default value of the email field and hides the email field from the reporting UI
 *
 *  Default = @""
 *  @param userEmail userEmail
 */
+ (void)setUserEmail:(NSString *)userEmail;

/**
 *  Sets the user name that is used in the dashboard's contacts
 *
 *  Default = @""
 *  @param userName userName
 */
+ (void)setUserName:(NSString *)userName;

//===========================================================================================================================================

//===========================================================================================================================================
/** @name SDK Settings */
//===========================================================================================================================================

/**
 *  Sets the event that invocates the feedback form
 *
 *  Default is set by startWithToken:invocationEvent:
 *  @param invocationEvent invocationEvent
 */
+ (void)setInvocationEvent:(IBGInvocationEvent)invocationEvent;

/**
 *  Sets the default SDK mode upon invocation
 *
 *  Default = IBGInvocationModeNA
 *  @param invocationMode invocationMode
 */
+ (void)setDefaultInvocationMode:(IBGInvocationMode)invocationMode;

/**
 *  Enable/disable SDK to use push notifications
 *
 *  Default = YES
 *  @param isPushNotificationsEnabled isPushNotificationsEnabled
 *  Note: In order to enable push notifications, UIApplicationDelegate method 
 *  `application:didRegisterForRemoteNotificationsWithDeviceToken:` should be implemented. Also 
 *  `application:didReceiveRemoteNotification` or `application:didReceiveRemoteNotification:fetchCompletionHandler:`
 */
+ (void)setPushNotificationsEnabled:(BOOL)isPushNotificationsEnabled;

/**
 *  Sets the value of whether the email field is validated or not
 *
 *  Default = YES
 *  @param isEmailFieldRequired isEmailFieldRequired
 */
+ (void)setEmailFieldRequired:(BOOL)isEmailFieldRequired;

/**
 *  Sets whether the comment field is validated or not
 *
 *  Default = NO
 *  @param isCommentFieldRequired isCommentFieldRequired
 */
+ (void)setCommentFieldRequired:(BOOL)isCommentFieldRequired;

/**
 *  Sets the threshold value of the shake gesture for iPhone/iPod Touch and iPad
 *
 *  Default iPhone = 2.5
 *  Default iPad = 0.6
 *  @param iPhoneShakingThreshold iPhoneShakingThreshold
 *  @param iPadShakingThreshold iPadShakingThreshold
 */
+ (void)setShakingThresholdForiPhone:(double)iPhoneShakingThreshold foriPad:(double)iPadShakingThreshold;

/**
 *  Sets the default edge and offset from the top at which the floating button will be shown. Different orientations are already handled
 *
 *  Default floatingButtonEdge = CGRectMaxXEdge
 *  Default floatingButtonOffsetFromTop = 50
 *  @param floatingButtonEdge CGRectMaxXEdge(right) or CGRectMinXEdge(left)
 *  @param floatingButtonOffsetFromTop floatingButtonOffsetFromTop
 */
+ (void)setFloatingButtonEdge:(CGRectEdge)floatingButtonEdge withTopOffset:(double)floatingButtonOffsetFromTop;

/**
 *  Sets the locale used to display the strings in the correct language
 *
 *  Default is fetched from the device locale
 *  @param locale locale
 */
+ (void)setLocale:(IBGLocale)locale;

/**
 *  Sets the default value of the intro message that gets shown on launching the app
 *
 *  Default = YES
 *  @param isIntroMessageEnabled isIntroMessageEnabled
 */
+ (void)setIntroMessageEnabled:(BOOL)isIntroMessageEnabled;

/**
 *  Sets the color theme of the whole SDK UI
 *
 *  @param colorTheme colorTheme
 */
+ (void)setColorTheme:(IBGColorTheme)colorTheme;

/**
 *  Sets the primary color of the SDK user interface, mostly indicating interactivity or call to action
 *
 *  @param color color
 */
+ (void)setPrimaryColor:(UIColor *)color;

/**
 *  Manually sets how to capture a screenshot, use it only if you're using OpenGL
 *
 *  @param screenshotCapturingBlock screenshotCapturingBlock
 */
+ (void)setScreenshotCapturingBlock:(CGImageRef (^)())screenshotCapturingBlock;

//===========================================================================================================================================

//===========================================================================================================================================
/** @name SDK Reporting */
//===========================================================================================================================================

/**
 *  Manually reports a bug without showing any UI elements
 *  @param comment comment(optional)
 *  @param screenshot screenshot(optional)
 */
+ (void)reportBugWithComment:(NSString *)comment screenshot:(UIImage *)screenshot;

/**
 *  Manually send a feedback without showing any UI elements
 *  @param comment comment(optional)
 *  @param screenshot screenshot(optional)
 */
+ (void)reportFeedbackWithComment:(NSString *)comment screenshot:(UIImage *)screenshot;

/**
 *  Manually reports an exception
 *  @param exception exception(required)
 */
+ (void)reportException:(NSException *)exception;

//===========================================================================================================================================

@end
