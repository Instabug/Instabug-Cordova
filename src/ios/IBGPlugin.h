#import <Cordova/CDVPlugin.h>
#import "ArgsRegistry.h"

@interface IBGPlugin : CDVPlugin

- (void)start:(CDVInvokedUrlCommand *)command;

- (void)setPrimaryColor:(CDVInvokedUrlCommand *)command;

- (void)setUserData:(CDVInvokedUrlCommand *)command;

- (void)addFile:(CDVInvokedUrlCommand *)command;

- (void)addLog:(CDVInvokedUrlCommand *)command;

- (void)changeInvocationEvent:(CDVInvokedUrlCommand *)command;

- (void)setLocale:(CDVInvokedUrlCommand *)command;

- (void)setReportTypes:(CDVInvokedUrlCommand *)command;

- (void)show:(CDVInvokedUrlCommand *)command;

- (void)showBugReportingWithReportTypeAndOptions:(CDVInvokedUrlCommand *)command;

- (void)setBugReportingEnabled:(CDVInvokedUrlCommand *)command;

- (void)setRepliesEnabled:(CDVInvokedUrlCommand *)command;

- (void)hasChats:(CDVInvokedUrlCommand *)command;

- (void)showReplies:(CDVInvokedUrlCommand *)command;

- (void)identifyUserWithEmail:(CDVInvokedUrlCommand *)command;

- (void)setUserAttribute:(CDVInvokedUrlCommand *)command;

- (void)removeUserAttribute:(CDVInvokedUrlCommand *)command;

- (void)getUserAttribute:(CDVInvokedUrlCommand *)command;

- (void)getAllUserAttributes:(CDVInvokedUrlCommand *)command;

- (void)setString:(CDVInvokedUrlCommand *)command;

- (void)init:(CDVInvokedUrlCommand *)command;

- (IBGSDKDebugLogsLevel)parseLogLevel:(NSString*)logLevel;

@end
