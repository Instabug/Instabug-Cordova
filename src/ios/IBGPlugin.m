#import "IBGPlugin.h"
#import <Instabug/Instabug.h>

/**
 * This plugin initializes Instabug.
 */
@implementation IBGPlugin

- (void)activate:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    NSDictionary* tokensForPlatforms = [command argumentAtIndex:0];
    
    if (tokensForPlatforms) {
        NSString* token = [tokensForPlatforms objectForKey:@"ios"];
        
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
                [Instabug startWithToken:token invocationEvent:invocationEvent];
                
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

- (void)invoke:(CDVInvokedUrlCommand*)command
{
    IBGInvocationMode iMode = [self parseInvocationMode:[command argumentAtIndex:0]];
    
    if (iMode) {
        [Instabug invokeWithInvocationMode:iMode];
    } else {
        [Instabug invoke];
    }
    
    [self sendSuccessResult:command];
}

- (void)showIntroDialog:(CDVInvokedUrlCommand*)command
{
    [Instabug showIntroMessage];
    [self sendSuccessResult:command];
}

- (void)setPrimaryColor:(CDVInvokedUrlCommand*)command
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
            [Instabug setPrimaryColor:uiColor];
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

- (void)setUserEmail:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    NSString* email = [command argumentAtIndex:0];
    
    if ([email length] > 0) {
        [Instabug setUserEmail:email];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"An email must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)setUserName:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    NSString* name = [command argumentAtIndex:0];
    
    if ([name length] > 0) {
        [Instabug setUserName:name];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A name must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)setUserData:(CDVInvokedUrlCommand*)command
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

- (void)addFile:(CDVInvokedUrlCommand*)command
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
            [Instabug addFileAttachmentWithURL:[NSURL URLWithString:filePath]];
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[NSString stringWithFormat:
                                                        @"file added",
                                                        filePath]];
        } else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                       messageAsString:[NSString stringWithFormat:
                                                        @"File %@ does not exist.",
                                                        filePath]];
        }
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A local file URL must be provided."];
    }
    
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)clearFileAttachments:(CDVInvokedUrlCommand*)command {
    [Instabug clearFileAttachments];
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)addLog:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString* log = [command argumentAtIndex:0];
    
    if ([log length] > 0) {
        IBGLog(@"%@", log);
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A log must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)logVerbose:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString* log = [command argumentAtIndex:0];
    
    if ([log length] > 0) {
        IBGLogVerbose(@"%@", log);
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A verbose log must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)logDebug:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString* log = [command argumentAtIndex:0];
    
    if ([log length] > 0) {
        IBGLogDebug(@"%@", log);
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A debug log must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)logInfo:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString* log = [command argumentAtIndex:0];
    
    if ([log length] > 0) {
        IBGLogInfo(@"%@", log);
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"An info log must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)logWarn:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString* log = [command argumentAtIndex:0];
    
    if ([log length] > 0) {
        IBGLogWarn(@"%@", log);
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A warn log must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)logError:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString* log = [command argumentAtIndex:0];
    
    if ([log length] > 0) {
        IBGLogError(@"%@", log);
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"An error log must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)changeInvocationEvent:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    IBGInvocationEvent iEvent = [self parseInvocationEvent:[command argumentAtIndex:0]];
    
    if (iEvent) {
        [Instabug setInvocationEvent:iEvent];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid event type must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)setLocale:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    IBGLocale iLocale = [self parseLocale:[command argumentAtIndex:0]];
    
    if ((long)iLocale >= 0) {
        [Instabug setLocale:iLocale];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A valid locale must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)setReportCategoriesWithTitles:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    NSArray *titles = [command argumentAtIndex:0];
    
    if (titles.count > 0) {
        [Instabug setReportCategoriesWithTitles:titles iconNames:nil];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A non-empty titles array must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)setUserAttributes:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    
    NSString *value = [command argumentAtIndex:0];
    NSString *key = [command argumentAtIndex:1];
    
    if (value.length > 0 && key.length > 0) {
        [Instabug setUserAttribute:value withKey:key];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A non-empty value and key must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)userAttributeForKey:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString *key = [command argumentAtIndex:0];
    
    if (key.length > 0) {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                   messageAsString:[Instabug userAttributeForKey:key]];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A non-empty key must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}
 
- (void)removeUserAttributeForKey:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result;
    NSString *key = [command argumentAtIndex:0];
    
    if (key.length > 0) {
        [Instabug removeUserAttributeForKey:key];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A non-empty key must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)appendTags:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* result;
    
    NSArray *tags = [command argumentAtIndex:0];
    
    if (tags.count > 0) {
        [Instabug appendTags:tags];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A non-empty tags array must be provided."];
    }
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];

}

- (void)resetTags:(CDVInvokedUrlCommand*)command {
    [Instabug resetTags];
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void) getTags:(CDVInvokedUrlCommand*)command {
    CDVPluginResult *result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                 messageAsArray:[Instabug getTags]];
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)identifyUserWithEmailAndName:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* result;
    NSString *email = [command argumentAtIndex:0];
    NSString *name = [command argumentAtIndex:1];

    if (email.length > 0) {
        [Instabug identifyUserWithEmail:email name:name];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A non-empty email must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)logout:(CDVInvokedUrlCommand*)command {
    [Instabug logOut];
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    
    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)logUserEventWithName:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* result;
    NSString *name = [command argumentAtIndex:0];

    if (name.length > 0) {
        [Instabug logUserEventWithName:name];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A non-empty event name must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)logUserEventWithNameAndParams:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* result;
    NSString *name = [command argumentAtIndex:0];
    NSString *params = [command argumentAtIndex:1];

    if (name.length > 0) {
        [Instabug logUserEventWithName:name params:params];
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                   messageAsString:@"A non-empty event name must be provided."];
    }

    [self.commandDelegate sendPluginResult:result callbackId:[command callbackId]];
}

- (void)setEmailFieldRequired:(NSString*)required
{
    if ([required length] > 0) {
        [Instabug setEmailFieldRequired:[required boolValue]];
    }
}

- (void)setCommentFieldRequired:(NSString*)required
{
    if ([required length] > 0) {
        [Instabug setCommentFieldRequired:[required boolValue]];
    }
}

- (void)setDefaultInvocationMode:(NSString*)mode {
    IBGInvocationMode iMode = [self parseInvocationMode:mode];
    
    if (iMode) {
        [Instabug setDefaultInvocationMode:iMode];
    }
}

- (void)setShakingThresholdForIPhone:(NSString*)iPhoneThreshold forIPad:(NSString*)iPadThreshold
{
    double iPhone = [iPhoneThreshold doubleValue];
    double iPad = [iPadThreshold doubleValue];
    
    if (iPhone && iPad) {
        [Instabug setShakingThresholdForiPhone:iPhone foriPad:iPad];
    }
}

- (void)setFloatingButtonEdge:(NSString*)edge withOffset:(NSString*)offset
{
    double offsetFromTop = [offset doubleValue];
    
    if (offsetFromTop) {
        if ([edge isEqualToString:@"left"]) {
            // SDK is unclear, can't implement
            //[Instabug setFloatingButtonEdge:CGRectMaxXEdge(left) withTopOffset:offsetFromTop];
        } else if ([edge isEqualToString:@"right"]) {
            // SDK is unclear, can't implement
            //[Instabug setFloatingButtonEdge:CGRectMaxXEdge(right) withTopOffset:offsetFromTop];
        }
    }
}

- (void)setTrackingUserStepsEnabled:(NSString*)enabled
{
    if ([enabled length] > 0) {
        [Instabug setUserStepsEnabled:[enabled boolValue]];
    }
}
- (void)setPushNotificationsEnabled:(NSString*)enabled
{
    if ([enabled length] > 0) {
        [Instabug setPushNotificationsEnabled:[enabled boolValue]];
    }
}

- (void)setIntroDialogEnabled:(NSString*)enabled
{
    if ([enabled length] > 0) {
        [Instabug setIntroMessageEnabled:[enabled boolValue]];
    }
}

- (void)setColorTheme:(NSString*)theme
{
    if ([theme isEqualToString:@"dark"]) {
        [Instabug setColorTheme:IBGColorThemeDark];
    } else if ([theme isEqualToString:@"light"]) {
        [Instabug setColorTheme:IBGColorThemeLight];
    }
}

- (void)setPromptOptionsEnabledWithBug:(NSString *)bugReportEnabled feedback:(NSString *)feedbackEnabled chat:(NSString *)chatEnabled;
{
    if (bugReportEnabled.length > 0 && feedbackEnabled.length > 0 && chatEnabled.length > 0) {
        [Instabug setPromptOptionsEnabledWithBug:[bugReportEnabled boolValue] feedback:[feedbackEnabled boolValue] chat:[chatEnabled boolValue]];
    }
}

- (void)setViewHierarchyEnabled:(NSString *)viewHierarchyEnabled {
    if (viewHierarchyEnabled.length > 0) {
        [Instabug setViewHierarchyEnabled:[viewHierarchyEnabled boolValue]];
    }
}

- (void)setWillSkipScreenshotAnnotation:(NSString *)willSkipScreenShot {
    if (willSkipScreenShot.length > 0) {
        [Instabug setWillSkipScreenshotAnnotation:[willSkipScreenShot boolValue]];
    }
}

- (void)setPostSendingDialogEnabled:(NSString *)isPostSendingDialogEnabled {
    if (isPostSendingDialogEnabled.length > 0) {
        [Instabug setPostSendingDialogEnabled:[isPostSendingDialogEnabled boolValue]];
    }
}

- (void)setChatNotificationEnabled:(NSString *)chatNotificationEnabled {
    if (chatNotificationEnabled.length > 0) {
        [Instabug setChatNotificationEnabled:[chatNotificationEnabled boolValue]];
    }
}

- (void)setAttachmentTypesEnabledScreenShot:(NSString *)screenShot 
                            extraScreenShot:(NSString *)extraScreenShot
                               galleryImage:(NSString *)galleryImage
                                  voiceNote:(NSString *)voiceNote
                            screenRecording:(NSString *)screenRecording {
    if (screenShot.length > 0 && extraScreenShot.length > 0 && galleryImage.length > 0 && voiceNote.length > 0 && screenRecording.length > 0) {
        [Instabug setAttachmentTypesEnabledScreenShot:[screenShot boolValue]
                            extraScreenShot:[extraScreenShot boolValue]
                               galleryImage:[galleryImage boolValue]
                                  voiceNote:[voiceNote boolValue]
                            screenRecording:[screenRecording boolValue]];
    }
}

- (void) applyOptions:(NSDictionary*)options
{
    [self setEmailFieldRequired:[[options objectForKey:@"emailRequired"] stringValue]];
    [self setCommentFieldRequired:[[options objectForKey:@"commentRequired"] stringValue]];
    [self setDefaultInvocationMode:[options objectForKey:@"defaultInvocationMode"]];
    [self setShakingThresholdForIPhone:[options objectForKey:@"shakingThresholdIPhone"]
                               forIPad:[options objectForKey:@"shakingThresholdIPad"]];
    [self setFloatingButtonEdge:[options objectForKey:@"floatingButtonEdge"]
                     withOffset:[options objectForKey:@"floatingButtonOffset"]];
    [self setTrackingUserStepsEnabled:[[options objectForKey:@"enableTrackingUserSteps"] stringValue]];
    [self setPushNotificationsEnabled:[[options objectForKey:@"enablePushNotifications"] stringValue]];
    [self setIntroDialogEnabled:[[options objectForKey:@"enableIntroDialog"] stringValue]];
    [self setColorTheme:[options objectForKey:@"colorTheme"]];
    [self setPromptOptionsEnabledWithBug:[[options objectForKey:@"bugReportEnabled"] stringValue] 
                                feedback:[[options objectForKey:@"feedbackEnabled"] stringValue]
                                    chat:[[options objectForKey:@"chatEnabled"] stringValue]];
    [self setViewHierarchyEnabled:[[options objectForKey:@"viewHierarchyEnabled"] stringValue]];
    [self setWillSkipScreenshotAnnotation:[[options objectForKey:@"willSkipScreenShot"] stringValue]];
    [self setPostSendingDialogEnabled:[[options objectForKey:@"isPostSendingDialogEnabled"] stringValue]];
    [self setAttachmentTypesEnabledScreenShot:[[options objectForKey:@"screenShot"] stringValue]
                              extraScreenShot:[[options objectForKey:@"extraScreenShot"] stringValue]
                                 galleryImage:[[options objectForKey:@"galleryImage"] stringValue]
                                    voiceNote:[[options objectForKey:@"voiceNote"] stringValue]
                              screenRecording:[[options objectForKey:@"screenRecording"] stringValue]];
    [self setChatNotificationEnabled:[[options objectForKey:@"chatNotificationEnabled"] stringValue]];
}

- (IBGInvocationEvent)parseInvocationEvent:(NSString*)event
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

- (IBGInvocationMode)parseInvocationMode:(NSString*)mode
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

- (IBGLocale)parseLocale:(NSString*)locale
{
    if ([locale isEqualToString:@"arabic"]) {
        return IBGLocaleArabic;
    } else if ([locale isEqualToString:@"chineseTaiwan"]) {
        return IBGLocaleChineseTaiwan;
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
    } else if ([locale isEqualToString:@"norwegian"]) {
        return IBGLocaleNorwegian;
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
    } else return -1;
}

- (UIColor *)colorFromHexString:(NSString *)hexString {
    unsigned rgbValue = 0;
    NSScanner* scanner = [NSScanner scannerWithString:hexString];
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue & 0xFF0000) >> 16)/255.0
                           green:((rgbValue & 0xFF00) >> 8)/255.0
                            blue:(rgbValue & 0xFF)/255.0 alpha:1.0];
}

- (void)sendSuccessResult:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate
     sendPluginResult:[CDVPluginResult resultWithStatus:CDVCommandStatus_OK]
     callbackId:[command callbackId]];
}

@end
