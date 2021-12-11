#import <Cordova/CDVPlugin.h>

@interface IBGPlugin : CDVPlugin

- (void)activate:(CDVInvokedUrlCommand *)command;

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

- (void)setChatsEnabled:(CDVInvokedUrlCommand *)command;

- (void)setRepliesEnabled:(CDVInvokedUrlCommand *)command;

- (void)showChats:(CDVInvokedUrlCommand *)command;

- (void)hasChats:(CDVInvokedUrlCommand *)command;

- (void)showReplies:(CDVInvokedUrlCommand *)command;

- (void)identifyUserWithEmail:(CDVInvokedUrlCommand *)command;

- (void)setUserAttribute:(CDVInvokedUrlCommand *)command;

- (void)removeUserAttribute:(CDVInvokedUrlCommand *)command;

- (void)getUserAttribute:(CDVInvokedUrlCommand *)command;

- (void)getAllUserAttributes:(CDVInvokedUrlCommand *)command;

@end
