#import <Cordova/CDVPlugin.h>

@interface IBGPlugin : CDVPlugin 

- (void) activate:(CDVInvokedUrlCommand*)command;

- (void) invoke:(CDVInvokedUrlCommand*)command;

- (void) setPrimaryColor:(CDVInvokedUrlCommand*)command;

- (void) setUserData:(CDVInvokedUrlCommand*)command;

- (void) addFile:(CDVInvokedUrlCommand*)command;

- (void) addLog:(CDVInvokedUrlCommand*)command;

- (void) changeInvocationEvent:(CDVInvokedUrlCommand*)command;

- (void) setLocale:(CDVInvokedUrlCommand*)command;

@end