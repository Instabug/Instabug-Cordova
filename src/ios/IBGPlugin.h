#import <Cordova/CDVPlugin.h>

@interface IBGPlugin : CDVPlugin 

- (void)activate:(CDVInvokedUrlCommand*)command;

- (void)invoke:(CDVInvokedUrlCommand*)command;

- (void)showIntroDialog:(CDVInvokedUrlCommand*)command;

- (void)setPrimaryColor:(CDVInvokedUrlCommand*)command;

- (void)setUserEmail:(CDVInvokedUrlCommand*)command;

- (void)setUserName:(CDVInvokedUrlCommand*)command;

- (void)setUserData:(CDVInvokedUrlCommand*)command;

- (void)addFile:(CDVInvokedUrlCommand*)command;
- (void)clearFileAttachments:(CDVInvokedUrlCommand*)command;

- (void)addLog:(CDVInvokedUrlCommand*)command;
- (void)logVerbose:(CDVInvokedUrlCommand*)command;
- (void)logDebug:(CDVInvokedUrlCommand*)command;
- (void)logInfo:(CDVInvokedUrlCommand*)command;
- (void)logWarn:(CDVInvokedUrlCommand*)command;
- (void)logError:(CDVInvokedUrlCommand*)command;

- (void)changeInvocationEvent:(CDVInvokedUrlCommand*)command;

- (void)setLocale:(CDVInvokedUrlCommand*)command;
- (void)setReportCategoriesWithTitlesAndIcons:(CDVInvokedUrlCommand*)comman;

- (void)setUserAttributes:(CDVInvokedUrlCommand*)command;
- (void)userAttributeForKey:(CDVInvokedUrlCommand*)command;
- (void)removeUserAttributeForKey:(CDVInvokedUrlCommand*)command;

- (void)appendTags:(CDVInvokedUrlCommand*)command;
- (void)resetTags:(CDVInvokedUrlCommand*)command;
- (void)getTags:(CDVInvokedUrlCommand*)command;

- (void)identifyUserWithEmailAndName:(CDVInvokedUrlCommand*)command;
- (void)logout:(CDVInvokedUrlCommand*)command;

- (void)logUserEventWithName:(CDVInvokedUrlCommand*)command;
- (void)logUserEventWithNameAndParams:(CDVInvokedUrlCommand*)command;

@end