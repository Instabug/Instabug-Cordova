#import "IBGPlugin.h"
#import <Instabug/Instabug.h>
#import <Instabug/IBGBugReporting.h>
#import <Instabug/IBGLog.h>
#import <Instabug/IBGSurveys.h>
#import <Instabug/IBGFeatureRequests.h>

/**
 * This plugin initializes Instabug.
 */
@implementation IBGPlugin

/**
 * Intializes Instabug and sets provided options.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) activate:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    NSDictionary* tokensForPlatforms = [command argumentAtIndex:0];

    if (tokensForPlatforms) {
        NSString* token = [tokensForPlatforms objectForKey:@"ios"];
        if (![token length] > 0) {
            token = [tokensForPlatforms objectForKey:@"token"];
        }
        if ([token length] > 0) {
            id invEvent = [command argumentAtIndex:1];
            IBGInvocationEvent invocationEvent = 0;

            if ([invEvent isKindOfClass:[NSString class]]) {
                invocationEvent = [self parseInvocationEvent:invEvent];
            } else if ([invEvent isKindOfClass:[NSDictionary class]]) {
                // Desired invocation event may be different across platforms
                // and can be specified as such
                invocationEvent = [self parseInvocationEvent: [invEvent objectForKey:@"ios"]];
            }

            if (!invocationEvent) {
                // Instabug iOS SDK requires invocation event for initialization
                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                           messageAsString:@"An invocation event must be provided."];
            } else {
                // Initialize Instabug
                [Instabug startWithToken:token invocationEvents:invocationEvent];
                [self setBaseUrlForDeprecationLogs];

                // Apply provided options
                [self applyOptions:[command argumentAtIndex:2]];

                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            }
        } else {
            // Without a token, Instabug cannot be initialized.
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                       messageAsString:@"An application token must be provided."];
        }
    } else {
        // Without a token, Instabug cannot be initialized.
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"An application token must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Intializes Instabug and sets provided options.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) startWithToken:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    NSDictionary* tokensForPlatforms = [command argumentAtIndex:0];

    if (tokensForPlatforms) {
        NSString* token = [tokensForPlatforms objectForKey:@"ios"];

        if ([token length] > 0) {
            NSArray* invEvents = [command argumentAtIndex:1];
            IBGInvocationEvent invocationEvents = 0;

            for (NSString *invEvent in invEvents) {
              IBGInvocationEvent invocationEvent = [self parseInvocationEvent:invEvent];
              invocationEvents |= invocationEvent;
            }
            if (invocationEvents == 0) {
                // Instabug iOS SDK requires invocation event for initialization
                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                           messageAsString:@"An invocation event must be provided."];
            } else {
                // Initialize Instabug
                [Instabug startWithToken:token invocationEvents:invocationEvents];
                [self setBaseUrlForDeprecationLogs];

                // Apply provided options
                [self applyOptions:[command argumentAtIndex:2]];

                result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            }
        } else {
            // Without a token, Instabug cannot be initialized.
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                       messageAsString:@"An application token must be provided."];
        }
    } else {
        // Without a token, Instabug cannot be initialized.
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"An application token must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void) identifyUserWithEmail:(CDVInvokedUrlCommand*)command {
    NSString *email = [command argumentAtIndex:0];
    NSString *name = [command argumentAtIndex:1];
    [Instabug identifyUserWithEmail:email name:name];
    [self sendSuccessResult:command];
}

/**
 * Sets whether users are required to enter an email address or not when doing a certain action `IBGAction`.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setEmailFieldRequiredForFeatureRequests:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    BOOL isEnabled = [command argumentAtIndex:0];
    NSArray* aTypes = [command argumentAtIndex:1];
    
    IBGAction actionTypes = 0;
    
    for (NSString *aType in aTypes) {
        IBGAction actionType = [self parseActionType:aType];
        actionTypes |= actionType;
    }
    
    if (isEnabled && actionTypes != 0) {
        [IBGFeatureRequests setEmailFieldRequired:[[command argumentAtIndex:0] boolValue] forAction:actionTypes];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid action type must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets the primary color of the SDK user interface, mostly
 * indicating interactivity or call to action.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setPrimaryColor:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    NSMutableString* color = [command argumentAtIndex:0];

    if ([color length] > 0) {
        BOOL valid = NO;

        if ([color length] == 6) {
            valid = YES;
        } else if ([color length] == 7 && [color rangeOfString:@"#"].location == 0) {
            valid = YES;
            // '#' char must be removed before parsing
            color = [NSMutableString stringWithString:[color substringFromIndex:1]];
        }

        if (valid) {
            UIColor* uiColor = [self colorFromHexString:color];
            Instabug.tintColor = uiColor;
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        } else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                       messageAsString:[NSString stringWithFormat:
                                                        @"%@ is not a valid hex color.",
                                                        [command argumentAtIndex:0]]];
        }
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A hex color must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Logs a user event that happens through the lifecycle of the application.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) logUserEventWithName:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    NSString* userEvent = [command argumentAtIndex:0];

    if ([userEvent length] > 0) {
        [Instabug logUserEventWithName:userEvent];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A name must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets a block of code to be executed just before the SDK's UI is presented.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setPreInvocationHandler:(CDVInvokedUrlCommand*)command
{
    IBGBugReporting.willInvokeHandler = ^{
        CDVPluginResult* result;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [result setKeepCallbackAsBool:true];
        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    };
}

/**
 * Sets a block of code to be executed right after the SDK's UI is dismissed.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setPostInvocationHandler:(CDVInvokedUrlCommand*)command
{
    IBGBugReporting.didDismissHandler = ^(IBGDismissType dismissType, IBGReportType reportType){
        CDVPluginResult* result;
        NSString *dismissTypeString = [self parseDismissType:dismissType];
        NSString *reportTypeString = [self parseReportType:reportType];
        NSDictionary *dict = @{ @"dismissType" : dismissTypeString, @"reportType" : reportTypeString};
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                     messageAsDictionary:dict];
        [result setKeepCallbackAsBool:true];
        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    };
}

/**
 * Sets a block of code to be executed right after the SDK's UI is dismissed.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setPreSendingHandler:(CDVInvokedUrlCommand*)command
{
    Instabug.willSendReportHandler = ^(IBGReport* report){
        CDVPluginResult* result;
        NSArray *tagsArray = report.tags;
        NSArray *instabugLogs= report.instabugLogs;
        NSArray *consoleLogs= report.consoleLogs;
        NSDictionary *userAttributes= report.userAttributes;
        NSArray *fileAttachments= report.fileLocations;
        
        NSDictionary *dict = @{ @"tagsArray" : tagsArray, @"instabugLogs" : instabugLogs, @"consoleLogs" : consoleLogs,       @"userAttributes" : userAttributes, @"fileAttachments" : fileAttachments};
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                               messageAsDictionary:dict];
        [result setKeepCallbackAsBool:true];
        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
        return report;
    };
}

/**
 * Sets a block of code to be executed just before the survey's UI is presented.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) willShowSurveyHandler:(CDVInvokedUrlCommand*)command
{
    IBGSurveys.willShowSurveyHandler = ^ {
        CDVPluginResult* result;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [result setKeepCallbackAsBool:true];
        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    };
}

/**
 * Sets a block of code to be executed right after the survey's UI is dismissed.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) didDismissSurveyHandler:(CDVInvokedUrlCommand*)command
{
    IBGSurveys.didDismissSurveyHandler = ^ {
        CDVPluginResult* result;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        [result setKeepCallbackAsBool:true];
        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    };
}

/**
 * Shows survey with a specific token.
 * Does nothing if there are no available surveys with that specific token.
 * Answered and cancelled surveys won't show up again.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) showSurveyWithToken:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    NSString* surveyToken = [command argumentAtIndex:0];

    if ([surveyToken length] > 0) {
        [IBGSurveys showSurveyWithToken:surveyToken];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A survey token must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Returns true if the survey with a specific token was answered before.
 * Will return false if the token does not exist or if the survey was not answered before.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) hasRespondedToSurveyWithToken:(CDVInvokedUrlCommand*)command
 {
     CDVPluginResult* result;
     NSString *surveyToken = [command argumentAtIndex:0];

     if (surveyToken.length > 0) {
         result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                    messageAsBool:[IBGSurveys hasRespondedToSurveyWithToken:surveyToken]];
     } else {
         result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                    messageAsString:@"A non-empty survey token must be provided."];
     }

     [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
 }

 /**
  * Returns true if the survey with a specific token was answered before.
  * Will return false if the token does not exist or if the survey was not answered before.
  *
  * @param {CDVInvokedUrlCommand*} command
  *        The command sent from JavaScript
  */
 - (void) getAvailableSurveys:(CDVInvokedUrlCommand*)command
  {
      [IBGSurveys availableSurveysWithCompletionHandler:^(NSArray<IBGSurvey *> *availableSurveys) {
        CDVPluginResult* result;
        NSMutableArray<NSDictionary*>* mappedSurveys = [[NSMutableArray alloc] init];
        for (IBGSurvey* survey in availableSurveys) {
            [mappedSurveys addObject:@{@"title": survey.title }];
        }
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                 messageAsArray:mappedSurveys];
        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    }];
  }

/**
 * Sets the user data that’s attached with each bug report sent.
 * Maximum size of the string is 1000 characters.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setUserData:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    NSString* data = [command argumentAtIndex:0];

    if ([data length] > 0) {
        [Instabug setUserData:data];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"User data must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets whether attachments in bug reporting and in-app messaging are enabled.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setAttachmentTypesEnabled:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    id screenshot = [command argumentAtIndex:0];
    id extraScreenshot = [command argumentAtIndex:1];
    id galleryImage = [command argumentAtIndex:2];
    id screenRecording = [command argumentAtIndex:3];
    IBGAttachmentType attachmentTypes = 0;
    if (screenshot && extraScreenshot && galleryImage && screenRecording) {
        if([screenshot boolValue]) {
            attachmentTypes = IBGAttachmentTypeScreenShot;
        }
        if([extraScreenshot boolValue]) {
            attachmentTypes |= IBGAttachmentTypeExtraScreenShot;
        }
        if([galleryImage boolValue]) {
            attachmentTypes |= IBGAttachmentTypeGalleryImage;
        }
        if([screenRecording boolValue]) {
            attachmentTypes |= IBGAttachmentTypeScreenRecording;
        }
        
        IBGBugReporting.enabledAttachmentTypes = attachmentTypes;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"Attachment types must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets the default position at which the Instabug screen recording button will be shown.
 * Different orientations are already handled.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setVideoRecordingFloatingButtonPosition:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    IBGPosition position = [self parseIBGPosition:[command argumentAtIndex:0]];

    if (position) {
//        IBGBugReporting.videoRecordingFloatingButtonPosition = position;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A position must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Attaches a new copy of this file with each bug report sent
 * with a maximum size of 1 MB. Calling this method several
 * times overrides the file to be attached. The file has to
 * be stored locally at the location provided.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) addFile:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    id file = [command argumentAtIndex:0];
    NSString* filePath;

    if ([file isKindOfClass:[NSString class]]) {
        filePath = file;
    } else if ([file isKindOfClass:[NSDictionary class]]) {
        // File location may be different across platforms
        // and can be specified as such
        filePath = [file objectForKey:@"ios"];
    }

    if ([filePath length] > 0) {
        NSFileManager* fileManager = [NSFileManager defaultManager];

        if ([fileManager fileExistsAtPath:filePath]) {
            // If the file doesn't exist at the path specified,
            // we won't be able to notify the containing app when
            // Instabug API call fails, so we check ourselves.
            NSURL* url = [NSURL URLWithString:filePath];
            [Instabug addFileAttachmentWithURL:url];
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        } else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                       messageAsString:[NSString stringWithFormat:
                                                        @"File %@ does not exist.",
                                                        filePath]];
        }
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A local file URI must be provided."];
    }


    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Adds custom logs that will be sent with each report.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) addLog:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString* log = [command argumentAtIndex:0];

    if ([log length] > 0) {
        [IBGLog log:log];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A log must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets the event that invokes the feedback form.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) changeInvocationEvent:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    IBGInvocationEvent iEvent = [self parseInvocationEvent:[command argumentAtIndex:0]];

    if (iEvent) {
        IBGBugReporting.invocationEvents = iEvent;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid event type must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets the event that invokes the feedback form.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setInvocationEvents:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    NSArray* invEvents = [command argumentAtIndex:0];
    IBGInvocationEvent invocationEvents = 0;

    for (NSString *invEvent in invEvents) {
      IBGInvocationEvent invocationEvent = [self parseInvocationEvent:invEvent];
      invocationEvents |= invocationEvent;
    }

    if (invocationEvents != 0) {
        IBGBugReporting.invocationEvents = invocationEvents;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid event type must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets the invocation options used when invoke Instabug SDK
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setInvocationOptions:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    NSArray* invOptions = [command argumentAtIndex:0];
    IBGBugReportingOption invocationOptions = 0;
    
    for (NSString *invOption in invOptions) {
        IBGBugReportingOption invocationOption = [self parseInvocationOption:invOption];
        invocationOptions |= invocationOption;
    }
    
    if (invocationOptions != 0) {
        IBGBugReporting.bugReportingOptions = invocationOptions;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid invocation option must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Set supported report types bug, feedback or both.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setReportTypes:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* result;
    NSArray* types = [command argumentAtIndex:0];
    IBGBugReportingReportType parsedTypes = 0;
    
    for (NSString* type in types) {
        IBGBugReportingReportType parsedType = [self parseBugReportingReportType:type];
        if (parsedType == 0) {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                messageAsString:@"A valid report type must be provided."];
            [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
            return;
        }
        parsedTypes |= parsedType;
    }
    [IBGBugReporting setPromptOptionsEnabledReportTypes:parsedTypes];
    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Show new report either bug or feedback with given options.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) showBugReportingWithReportTypeAndOptions:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* result;
    NSString* type = [command argumentAtIndex:0];
    NSArray* options = [command argumentAtIndex:1];
    
    IBGBugReportingReportType parsedType = [self parseBugReportingReportType:type];
    if (parsedType == 0) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid report type must be provided."];
        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
        return;
    }
    
    IBGBugReportingOption parsedOptions = 0;
    
    for (NSString* option in options) {
        IBGBugReportingOption parsedOption = [self parseInvocationOption:option];
        if (parsedOption == 0) {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                       messageAsString:@"A valid bug reporting option must be provided."];
            [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
            return;
        }
        parsedOptions |= parsedOption;
    }
    [IBGBugReporting showWithReportType:parsedType options:parsedOptions];
    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Show default Instabug prompt.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) show:(CDVInvokedUrlCommand*)command {
    [Instabug show];
    [self sendSuccessResult:command];
}

/**
 * Enable or disable anything that has to do with bug reporting.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setBugReportingEnabled:(CDVInvokedUrlCommand*)command {
    BOOL isEnabled = [[command argumentAtIndex:0] boolValue];
    IBGBugReporting.enabled = isEnabled;
    [self sendSuccessResult:command];
}

/**
 * Enable or disable anything that has to do with chats.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setChatsEnabled:(CDVInvokedUrlCommand*)command {
    BOOL isEnabled = [[command argumentAtIndex:0] boolValue];
    IBGChats.enabled = isEnabled;
    [self sendSuccessResult:command];
}

/**
 * Enable or disable anything that has to do with replies.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setRepliesEnabled:(CDVInvokedUrlCommand*)command {
    BOOL isEnabled = [[command argumentAtIndex:0] boolValue];
    IBGReplies.enabled = isEnabled;
    [self sendSuccessResult:command];
}

/**
 * Show chats view as a list or new message.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) showChats:(CDVInvokedUrlCommand*)command {
    [IBGChats show];
    [self sendSuccessResult:command];
}

/**
 * See if user has chats.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) hasChats:(CDVInvokedUrlCommand*)command {
    BOOL hasChats = [IBGReplies hasChats];
    if (hasChats) {
        [self sendSuccessResult:command];
    }
}

/**
 * Show replies to user.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) showReplies:(CDVInvokedUrlCommand*)command {
    [IBGReplies show];
    [self sendSuccessResult:command];
}

/**
 * Sets the locale used to display the strings in the
 * correct language.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setLocale:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    IBGLocale iLocale = [self parseLocale:[command argumentAtIndex:0]];

    if (iLocale) {
        [Instabug setLocale:iLocale];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid locale must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets whether user steps tracking is visual, non visual or disabled.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setReproStepsMode:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    IBGUserStepsMode reproStepsMode = [self parseReproStepsMode:[command argumentAtIndex:0]];

    if (reproStepsMode) {
        Instabug.reproStepsMode = reproStepsMode;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid user steps mode must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Shows the welcome message in a specific mode.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) showWelcomeMessage:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    IBGWelcomeMessageMode welcomeMessageMode = [self parseWelcomeMessageMode:[command argumentAtIndex:0]];

    if (welcomeMessageMode) {
        [Instabug showWelcomeMessageWithMode:welcomeMessageMode];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid welcome message mode must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets whether the extended bug report mode should be disabled, enabled with
 * required fields or enabled with optional fields.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setExtendedBugReportMode:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;

    IBGExtendedBugReportMode extendedBugReportMode = [self parseExtendedBugReportMode:[command argumentAtIndex:0]];

    if (extendedBugReportMode != -1) {
        IBGBugReporting.extendedBugReportMode = extendedBugReportMode;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid extended bug report mode must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Enable/Disable view hierarchy from Instabug SDK
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
 - (void) setViewHierarchyEnabled:(CDVInvokedUrlCommand*)command
 {
     CDVPluginResult* result;

     BOOL isEnabled = [command argumentAtIndex:0];

     if (isEnabled) {
         Instabug.shouldCaptureViewHierarchy = [[command argumentAtIndex:0] boolValue];
         result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
     } else {
         result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                    messageAsString:@"A boolean value must be provided."];
     }

     [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
 }

  /**
   * Enables/disables showing in-app notifications when the user receives a new
   * message.
   *
   * @param {CDVInvokedUrlCommand*} command
   *        The command sent from JavaScript
   */
   - (void) setChatNotificationEnabled:(CDVInvokedUrlCommand*)command
   {
       CDVPluginResult* result;

       BOOL isEnabled = [command argumentAtIndex:0];

       if (isEnabled) {
           IBGReplies.inAppNotificationsEnabled = [[command argumentAtIndex:0] boolValue];
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
       } else {
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                      messageAsString:@"A boolean value must be provided."];
       }

       [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
   }

   /**
    * Sets whether IBGLog should also print to Xcode's console log or not.
    *
    * @param {CDVInvokedUrlCommand*} command
    *        The command sent from JavaScript
    */
    - (void) setIBGLogPrintsToConsole:(CDVInvokedUrlCommand*)command
    {
        CDVPluginResult* result;

        BOOL isEnabled = [command argumentAtIndex:0];

        if (isEnabled) {
            IBGLog.printsToConsole = [[command argumentAtIndex:0] boolValue];
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        } else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                       messageAsString:@"A boolean value must be provided."];
        }

        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    }

  /**
   * Sets maximum auto screen recording video duration.
   *
   * @param {CDVInvokedUrlCommand*} command
   *        The command sent from JavaScript
   */
   - (void) setAutoScreenRecordingMaxDuration:(CDVInvokedUrlCommand*)command
   {
       CDVPluginResult* result;

       CGFloat duration = [[command argumentAtIndex:0] floatValue];

       if (duration) {
           Instabug.autoScreenRecordingDuration = duration;
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
       } else {
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                      messageAsString:@"A duration must be provided."];
       }

       [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
   }

   /**
    * Returns the number of unread messages the user currently has.
    *
    * @param {CDVInvokedUrlCommand*} command
    *        The command sent from JavaScript
    */
    - (void) getUnreadRepliesCount:(CDVInvokedUrlCommand*)command
    {
        CDVPluginResult* result;

        NSInteger messageCount = IBGReplies.unreadRepliesCount;
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt: messageCount];

        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    }

/**
 * Convenience method for setting the threshold value
 * of the shake gesture for iPhone/iPod touch and iPad.
 *
 * @param {NSString*} iPhoneThreshold
 *        NSString representation of double iPhone threshold
 * @param {NSString*}
 *        NSString representation of double iPad threshold
 */
- (void) setShakingThresholdForIPhone:(NSString*)iPhoneThreshold forIPad:(NSString*)iPadThreshold
{
    double iPhone = [iPhoneThreshold doubleValue];
    double iPad = [iPadThreshold doubleValue];

    if (iPhone && iPad) {
      IBGBugReporting.shakingThresholdForiPhone = iPhone;
      IBGBugReporting.shakingThresholdForiPad = iPad;
    }
}

/**
 * Convenience method for setting the default edge on
 * which the floating button will be shown and its
 * offset from the top.
 *
 * @param {NSString*} edge
 *        NSString representation of edge
 * @param {NSString*} offset
 *        NSString representation of double offset
 */
- (void) setFloatingButtonEdge:(NSString*)edge withOffset:(NSNumber* )offset
{
    double offsetFromTop = [offset doubleValue];

    if (offset) {
        IBGBugReporting.floatingButtonTopOffset = offsetFromTop;
    }
    if ([edge isEqualToString:@"left"]) {
        IBGBugReporting.floatingButtonEdge = CGRectMinXEdge;
    } else if ([edge isEqualToString:@"right"]) {
        IBGBugReporting.floatingButtonEdge = CGRectMaxXEdge;
    }
}

/**
 * Convenience method for setting whether to track
 * the user’s steps while using the app or not.
 *
 * @param {NSString*} enabled
 *        NSString representation of boolean enabled
 */
- (void) setTrackingUserStepsEnabled:(NSString*)enabled
{
    if ([enabled length] > 0) {
        Instabug.trackUserSteps = [enabled boolValue];
    }
}

/**
 * Convenience method for setting whether to allow
 * the SDK to use push notifications or not.
 *
 * @param {NSString*} enabled
 *        NSString representation of boolean enabled
 */
- (void) setPushNotificationsEnabled:(NSString*)enabled
{
    if ([enabled length] > 0) {
        [IBGReplies setPushNotificationsEnabled:[enabled boolValue]];
    }
}

/**
 * Convenience method for setting whether to enable the
 * session profiler or not.
 *
 * @param {NSString*} enabled
 *        NSString representation of boolean enabled
 */
- (void) setSessionProfilerEnabled:(NSString*)enabled
{
    if ([enabled length] > 0) {
        [Instabug setSessionProfilerEnabled:[enabled boolValue]];
    }
}

/**
 * Convenience method for parsing and setting the welcome message mode
 *
 * @param {NSString*} enabled
 *        NSString representation of welcomeMessageMode
 */
- (void) setWelcomeMessageMode:(NSString*)welcomeMessageMode
{
    if ([welcomeMessageMode length] > 0) {
        IBGWelcomeMessageMode welcomeMessageModeEnum = [self parseWelcomeMessageMode:welcomeMessageMode];
        [Instabug setWelcomeMessageMode:welcomeMessageModeEnum];
    }
}

/**
 * Convenience method for setting the color theme of
 * the SDK invocation.
 *
 * @param {NSString*} theme
 *        NSString representation of color theme
 */
- (void) setColorThemeInOptions:(NSString*)theme
{
    if ([theme isEqualToString:@"dark"]) {
        [Instabug setColorTheme:IBGColorThemeDark];
    } else if ([theme isEqualToString:@"light"]) {
        [Instabug setColorTheme:IBGColorThemeLight];
    }
}

/**
 * Sets the SDK color theme
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) setColorTheme:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString* theme = [command argumentAtIndex:0];

    if ([theme length] > 0) {
        if ([theme isEqualToString:@"dark"]) {
            [Instabug setColorTheme:IBGColorThemeDark];
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        } else if ([theme isEqualToString:@"light"]) {
            [Instabug setColorTheme:IBGColorThemeLight];
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        } else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"Color theme value is not valid."];
        }
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"Color theme must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

/**
 * Sets a threshold for numbers of sessions and another for number of days
 * required before a survey, that has been dismissed once, would show again.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
 - (void) setThresholdForReshowingSurveyAfterDismiss:(CDVInvokedUrlCommand*)command
 {
     CDVPluginResult* result;

     NSInteger sessionsCount = [[command argumentAtIndex:0] integerValue];
     NSInteger daysCount = [[command argumentAtIndex:1] integerValue];

     if (sessionsCount && daysCount) {
         [IBGSurveys setThresholdForReshowingSurveyAfterDismiss:sessionsCount daysCount:daysCount];
         result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
     } else {
         result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                    messageAsString:@"A sessions count and days count must be provided."];
     }

     [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
 }

 /**
  * Sets a threshold for numbers of sessions and another for number of days
  * required before a survey, that has been dismissed once, would show again.
  *
  * @param {CDVInvokedUrlCommand*} command
  *        The command sent from JavaScript
  */
  - (void) setAutoShowingSurveysEnabled:(CDVInvokedUrlCommand*)command
  {
      CDVPluginResult* result;

      BOOL autoShowingSurveysEnabled = [command argumentAtIndex:0];

      if (autoShowingSurveysEnabled) {
          IBGSurveys.autoShowingEnabled = [[command argumentAtIndex:0] boolValue];
          result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
      } else {
          result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                     messageAsString:@"A boolean value must be provided."];
      }

      [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
  }

  /**
   * Sets whether surveys are enabled or not.
   *
   * @param {CDVInvokedUrlCommand*} command
   *        The command sent from JavaScript
   */
   - (void) setSurveysEnabled:(CDVInvokedUrlCommand*)command
   {
       CDVPluginResult* result;

       BOOL isEnabled = [command argumentAtIndex:0];

       if (isEnabled) {
           IBGSurveys.enabled = [[command argumentAtIndex:0] boolValue];
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
       } else {
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                      messageAsString:@"A boolean value must be provided."];
       }

       [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
   }

  /**
   * Shows the UI for feature requests list
   *
   * @param {CDVInvokedUrlCommand*} command
   *        The command sent from JavaScript
   */
   - (void) showFeatureRequests:(CDVInvokedUrlCommand*)command
   {
     [IBGFeatureRequests show];
     [self sendSuccessResult:command];
   }

   /**
    * Resets the value of the user's email and name, previously set.
    *
    * @param {CDVInvokedUrlCommand*} command
    *        The command sent from JavaScript
    */
    - (void) logOut:(CDVInvokedUrlCommand*)command
    {
      [Instabug logOut];
      [self sendSuccessResult:command];
    }

    /**
     * Dismisses any Instabug views that are currently being shown.
     *
     * @param {CDVInvokedUrlCommand*} command
     *        The command sent from JavaScript
     */
     - (void) dismiss:(CDVInvokedUrlCommand*)command
     {
       [IBGBugReporting dismiss];
       [self sendSuccessResult:command];
     }

    /**
     * Set custom user attributes that are going to be sent with each feedback, bug or crash.
     */
     - (void) setUserAttribute:(CDVInvokedUrlCommand*)command
     {
       CDVPluginResult* result;

       NSString* key = [command argumentAtIndex:0];
       NSString* value = [command argumentAtIndex:1];

       if (key && value) {
           [Instabug setUserAttribute:value withKey:key];
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
       } else {
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                      messageAsString:@"key and value parameters must be provided."];
       }

       [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
     }

    /**
     * Removes a given key and its associated value from user attributes.
     * Does nothing if a key does not exist.
    */
     - (void) removeUserAttribute:(CDVInvokedUrlCommand*)command
     {
       CDVPluginResult* result;

       NSString* key = [command argumentAtIndex:0];

       if (key) {
           [Instabug removeUserAttributeForKey:key];
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
       } else {
           result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                      messageAsString:@"key parameter must be provided."];
       }

       [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
     }

    /**
     * Returns the user attribute associated with a given key.
    */
    - (void) getUserAttribute:(CDVInvokedUrlCommand*)command
    {
       CDVPluginResult* result;

       NSString* key = [command argumentAtIndex:0];
       NSString* userAttribute = @[[Instabug userAttributeForKey:key]];

       result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: userAttribute];

       [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    }

    /**
     * Returns the user attribute associated with a given key.
    */
    - (void) getAllUserAttributes:(CDVInvokedUrlCommand*)command
    {
       CDVPluginResult* result;

       NSDictionary* userAttributes = @[[Instabug userAttributes]];

       result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:userAttributes];

       [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    }

   /**
    * Shows one of the surveys that were not shown before, that also have
    * conditions that match the current device/user.
    *
    * @param {CDVInvokedUrlCommand*} command
    *        The command sent from JavaScript
    */
    - (void) showSurveyIfAvailable:(CDVInvokedUrlCommand*)command
    {
      [IBGSurveys showSurveyIfAvailable];
      [self sendSuccessResult:command];
    }

   /**
    * Sets a threshold for numbers of sessions and another for number of days
    * required before a survey, that has been dismissed once, would show again.
    *
    * @param {CDVInvokedUrlCommand*} command
    *        The command sent from JavaScript
    */
    - (void) setShouldShowSurveysWelcomeScreen:(CDVInvokedUrlCommand*)command
    {
        CDVPluginResult* result;

        BOOL shouldShowWelcomeScreen = [command argumentAtIndex:0];

        if (shouldShowWelcomeScreen) {
            IBGSurveys.shouldShowWelcomeScreen = [[command argumentAtIndex:0] boolValue];
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
        } else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                       messageAsString:@"A boolean value must be provided."];
        }

        [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
    }

/**
 * Wrapper method for applying all provided options.
 *
 * @param {NSDictionary*} options
 *        Provided options
 */
- (void) applyOptions:(NSDictionary*)options
{
    [self setShakingThresholdForIPhone:[options objectForKey:@"shakingThresholdIPhone"]
                               forIPad:[options objectForKey:@"shakingThresholdIPad"]];
    [self setFloatingButtonEdge:[options objectForKey:@"floatingButtonEdge"]
                     withOffset:[options objectForKey:@"floatingButtonOffset"]];
    [self setTrackingUserStepsEnabled:[[options objectForKey:@"enableTrackingUserSteps"] stringValue]];
    [self setPushNotificationsEnabled:[[options objectForKey:@"enablePushNotifications"] stringValue]];
    [self setSessionProfilerEnabled:[[options objectForKey:@"enableSessionProfiler"] stringValue]];
    [self setColorThemeInOptions:[options objectForKey:@"colorTheme"]];
    [self setWelcomeMessageMode:[options objectForKey:@"welcomeMessageMode"]];
}

/**
 * Convenience method for converting NSString to
 * IBGInvocationEvent.
 *
 * @param  {NSString*} event
 *         NSString shortcode for IBGInvocationEvent
 */
- (IBGInvocationEvent) parseInvocationEvent:(NSString*)event
{
    if ([event isEqualToString:@"shake"]) {
        return IBGInvocationEventShake;
    } else if ([event isEqualToString:@"button"]) {
        return IBGInvocationEventFloatingButton;
    } else if ([event isEqualToString:@"screenshot"]) {
        return IBGInvocationEventScreenshot;
    } else if ([event isEqualToString:@"swipe"]) {
        return IBGInvocationEventTwoFingersSwipeLeft;
    } else if ([event isEqualToString:@"pan"]) {
        return IBGInvocationEventRightEdgePan;
    } else if ([event isEqualToString:@"none"]) {
        return IBGInvocationEventNone;
    } else return 0;
}

/**
 * Convenience method for converting NSString to
 * IBGAction.
 *
 * @param  {NSString*} actionType
 *         NSString shortcode for IBGAction
 */
- (IBGAction) parseActionType:(NSString*)actionType
{
    if ([actionType isEqualToString:@"requestNewFeature"]) {
        return IBGActionRequestNewFeature;
    } else if ([actionType isEqualToString:@"addCommentToFeature"]) {
        return IBGActionAddCommentToFeature;
    } else return 0;
}

/**
 * Convenience method for converting NSString to
 * IBGBugReportingInvocationOption.
 *
 * @param  {NSString*} option
 *         NSString shortcode for IBGBugReportingInvocationOption
 */
- (IBGBugReportingOption) parseInvocationOption:(NSString*)option
{
    if ([option isEqualToString:@"emailFieldHidden"]) {
        return IBGBugReportingOptionEmailFieldHidden;
    } else if ([option isEqualToString:@"emailFieldOptional"]) {
        return IBGBugReportingOptionEmailFieldOptional;
    } else if ([option isEqualToString:@"commentFieldRequired"]) {
        return IBGBugReportingOptionCommentFieldRequired;
    } else if ([option isEqualToString:@"disablePostSendingDialog"]) {
        return IBGBugReportingOptionDisablePostSendingDialog;
    } else return 0;
}

- (IBGBugReportingReportType) parseBugReportingReportType:(NSString*)type {
    if ([type isEqualToString:@"bug"]) {
        return IBGBugReportingReportTypeBug;
    } else if ([type isEqualToString:@"feedback"]) {
        return IBGBugReportingReportTypeFeedback;
    } else if ([type isEqualToString:@"question"]) {
        return IBGBugReportingReportTypeQuestion;
    } else return 0;
}

/**
 * Convenience method for converting NSString to
 * IBGInvocationMode.
 *
 * @param  {NSString*}
 *         NSString shortcode for IBGInvocationMode
 */
- (IBGInvocationMode) parseInvocationMode:(NSString*)mode
{
    if ([mode isEqualToString:@"bug"]) {
        return IBGInvocationModeNewBug;
    } else if ([mode isEqualToString:@"feedback"]) {
        return IBGInvocationModeNewFeedback;
    } else if ([mode isEqualToString:@"chat"]) {
        return IBGInvocationModeNewChat;
    } else if ([mode isEqualToString:@"chatList"]) {
        return IBGInvocationModeChatsList;
    } else if ([mode isEqualToString:@"na"]) {
        return IBGInvocationModeNA;
    } else return 0;
}

/**
 * Convenience method for converting NSString to
 * IBGPosition.
 *
 * @param  {NSString*} position
 *         NSString shortcode for IBGPosition
 */
- (IBGPosition) parseIBGPosition:(NSString*)position
{
    if ([position isEqualToString:@"topRight"]) {
        return IBGPositionTopRight;
    } else if ([position isEqualToString:@"bottomLeft"]) {
        return IBGPositionBottomLeft;
    } else if ([position isEqualToString:@"topLeft"]) {
        return IBGPositionTopLeft;
    } else if ([position isEqualToString:@"bottomRight"]) {
        return IBGPositionBottomRight;
    } else return 0;
}


/**
 * Convenience method for converting NSString to
 * IBGUserStepsMode.
 *
 * @param  {NSString*} mode
 *         NSString shortcode for IBGUserStepsMode
 */
- (IBGUserStepsMode) parseReproStepsMode:(NSString*)mode
{
    if ([mode isEqualToString:@"enabled"]) {
        return IBGUserStepsModeEnable;
    } else if ([mode isEqualToString:@"disabled"]) {
        return IBGUserStepsModeDisable;
    } else if ([mode isEqualToString:@"enabledWithNoScreenshots"]) {
        return IBGUserStepsModeEnabledWithNoScreenshots;
    } else return 0;
}

/**
 * Convenience method for converting NSString to
 * IBGWelcomeMessageMode.
 *
 * @param  {NSString*} mode
 *         NSString shortcode for IBGWelcomeMessageMode
 */
- (IBGWelcomeMessageMode) parseWelcomeMessageMode:(NSString*)mode
{
    if ([mode isEqualToString:@"welcomeMessageModeLive"]) {
        return IBGWelcomeMessageModeLive;
    } else if ([mode isEqualToString:@"welcomeMessageModeBeta"]) {
        return IBGWelcomeMessageModeBeta;
    } else if ([mode isEqualToString:@"welcomeMessageModeDisabled"]) {
        return IBGWelcomeMessageModeDisabled;
    } else return 0;
}

/**
 * Convenience method for converting NSString to
 * IBGExtendedBugReportMode.
 *
 * @param  {NSString*} mode
 *         NSString shortcode for IBGExtendedBugReportMode
 */
- (IBGExtendedBugReportMode) parseExtendedBugReportMode:(NSString*)mode
{
    if ([mode isEqualToString:@"enabledWithRequiredFields"]) {
        return IBGExtendedBugReportModeEnabledWithRequiredFields;
    } else if ([mode isEqualToString:@"enabledWithOptionalFields"]) {
        return IBGExtendedBugReportModeEnabledWithOptionalFields;
    } else if ([mode isEqualToString:@"disabled"]) {
        return IBGExtendedBugReportModeDisabled;
    } else return -1;
}
            
  /**
   * Convenience method for converting NSString to
   * IBGDismissType.
   *
   * @param  {NSString*} dismissType
   *         NSString shortcode for IBGDismissType
   */
- (NSString*) parseDismissType:(IBGDismissType)dismissType
  {
      if (dismissType == IBGDismissTypeSubmit) {
          return @"submit";
      } else if (dismissType == IBGDismissTypeCancel) {
          return @"cancel";
      } else if (dismissType == IBGDismissTypeAddAttachment) {
          return @"add attachment";
      } else return @"";
  }

/**
 * Convenience method for converting NSString to
 * IBGReportType.
 *
 * @param  {NSString*} reportType
 *         NSString shortcode for IBGReportType
 */
- (NSString*) parseReportType:(IBGReportType)reportType
{
    if (reportType == IBGReportTypeBug) {
        return @"bug";
    } else if (reportType == IBGReportTypeFeedback) {
        return @"feedback";
    } else if (reportType == IBGReportTypeQuestion) {
        return @"question";
    } else return @"";
}

/**
 * Convenience method for converting NSString to
 * IBGLocale.
 *
 * @param  {NSString*} locale
 *         NSString shortcode for IBGLocale
 */
- (IBGLocale) parseLocale:(NSString*)locale
{
    if ([locale isEqualToString:@"arabic"]) {
        return IBGLocaleArabic;
    } else if ([locale isEqualToString:@"chineseTaiwan"]) {
        return IBGLocaleChineseTaiwan;
    } else if ([locale isEqualToString:@"azerbaijani"]) {
        return IBGLocaleAzerbaijani;
    } else if ([locale isEqualToString:@"chineseSimplified"]) {
        return IBGLocaleChineseSimplified;
    } else if ([locale isEqualToString:@"chineseTraditional"]) {
        return IBGLocaleChineseTraditional;
    } else if ([locale isEqualToString:@"czesh"]) {
        return IBGLocaleEnglish;
    } else if ([locale isEqualToString:@"danish"]) {
        return IBGLocaleDanish;
    } else if ([locale isEqualToString:@"dutch"]) {
        return IBGLocaleDutch;
    } else if ([locale isEqualToString:@"english"]) {
        return IBGLocaleEnglish;
    } else if ([locale isEqualToString:@"french"]) {
        return IBGLocaleFrench;
    } else if ([locale isEqualToString:@"german"]) {
        return IBGLocaleGerman;
    } else if ([locale isEqualToString:@"italian"]) {
        return IBGLocaleItalian;
    } else if ([locale isEqualToString:@"japanese"]) {
        return IBGLocaleJapanese;
    } else if ([locale isEqualToString:@"korean"]) {
        return IBGLocaleKorean;
    } else if ([locale isEqualToString:@"polish"]) {
        return IBGLocalePolish;
    } else if ([locale isEqualToString:@"portuguese"]) {
        return IBGLocalePortugese;
    } else if ([locale isEqualToString:@"portugueseBrazil"]) {
        return IBGLocalePortugueseBrazil;
    } else if ([locale isEqualToString:@"russian"]) {
        return IBGLocaleRussian;
    } else if ([locale isEqualToString:@"solvak"]) {
        return IBGLocaleSlovak;
    } else if ([locale isEqualToString:@"spanish"]) {
        return IBGLocaleSpanish;
    } else if ([locale isEqualToString:@"swedish"]) {
        return IBGLocaleSwedish;
    } else if ([locale isEqualToString:@"turkish"]) {
        return IBGLocaleTurkish;
    } else return 0;
}

/**
 * Util method for parsing hex string to UIColor.
 *
 * @param  {NSString *} hexString
 *         NSString representation of hex color
 */
- (UIColor *)colorFromHexString:(NSString *)hexString {
    unsigned rgbValue = 0;
    NSScanner* scanner = [NSScanner scannerWithString:hexString];
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue & 0xFF0000) >> 16)/255.0
                           green:((rgbValue & 0xFF00) >> 8)/255.0
                            blue:(rgbValue & 0xFF)/255.0 alpha:1.0];
}

/**
 * Convenience method for sending successful plugin
 * result in methods that cannot fail.
 *
 * @param {CDVInvokedUrlCommand*} command
 *        The command sent from JavaScript
 */
- (void) sendSuccessResult:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate
     sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK]
     callbackId:[command callbackId]];
}

- (void) setBaseUrlForDeprecationLogs {
    SEL setCurrentPlatformSEL = NSSelectorFromString(@"setCurrentPlatform:");
    if([[Instabug class] respondsToSelector:setCurrentPlatformSEL]) {
        NSInvocation *inv = [NSInvocation invocationWithMethodSignature:[[Instabug class] methodSignatureForSelector:setCurrentPlatformSEL]];
        [inv setSelector:setCurrentPlatformSEL];
        [inv setTarget:[Instabug class]];
        IBGPlatform platform = IBGPlatformCordova;
        [inv setArgument:&(platform) atIndex:2];
        
        [inv invoke];
    }
}

@end
