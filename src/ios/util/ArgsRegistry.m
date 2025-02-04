#import "ArgsRegistry.h"

@implementation ArgsRegistry

+ (ArgsDictionary *) recordButtonPositions {
    return @{
        @"topLeft": @(IBGPositionTopLeft),
        @"topRight": @(IBGPositionTopRight),
        @"bottomLeft": @(IBGPositionBottomLeft),
        @"bottomRight": @(IBGPositionBottomRight),
    };
}

+ (ArgsDictionary *) welcomeMessageModes {
    return @{
        @"welcomeMessageModeLive": @(IBGWelcomeMessageModeLive),
        @"welcomeMessageModeBeta": @(IBGWelcomeMessageModeBeta),
        @"welcomeMessageModeDisabled": @(IBGWelcomeMessageModeDisabled),
    };
}

+ (ArgsDictionary *) colorThemes {
    return @{
        @"light": @(IBGColorThemeLight),
        @"dark": @(IBGColorThemeDark),
    };
}

+ (ArgsDictionary *) floatingButtonEdges {
    return @{
        @"left": @(CGRectMinXEdge),
        @"right": @(CGRectMaxXEdge),
    };
}

+ (NSDictionary<NSString *, NSString *> *) placeholders {
    return @{
        @"shakeHint": kIBGShakeStartAlertTextStringName,
        @"swipeHint": kIBGTwoFingerSwipeStartAlertTextStringName,
        @"edgeSwipeStartHint": kIBGEdgeSwipeStartAlertTextStringName,
        @"startAlertText": kIBGStartAlertTextStringName,
        @"invalidEmailMessage": kIBGInvalidEmailMessageStringName,
        @"invalidEmailTitle": kIBGInvalidEmailTitleStringName,
        @"invalidCommentMessage": kIBGInvalidNumberMessageStringName,
        @"invalidCommentTitle": kIBGInvalidNumberTitleStringName,
        @"invocationHeader": kIBGInvocationTitleStringName,
        @"reportQuestion": kIBGAskAQuestionStringName,
        @"reportBug": kIBGReportBugStringName,
        @"reportFeedback": kIBGReportFeedbackStringName,
        @"emailFieldHint": kIBGEmailFieldPlaceholderStringName,
        @"commentFieldHintForBugReport": kIBGCommentFieldPlaceholderForBugReportStringName,
        @"commentFieldHintForFeedback": kIBGCommentFieldPlaceholderForFeedbackStringName,
        @"commentFieldHintForQuestion": kIBGCommentFieldPlaceholderForQuestionStringName,
        @"addVideoMessage": kIBGAddScreenRecordingMessageStringName,
        @"addVoiceMessage": kIBGAddVoiceMessageStringName,
        @"addImageFromGallery": kIBGAddImageFromGalleryStringName,
        @"addExtraScreenshot": kIBGAddExtraScreenshotStringName,
        @"audioRecordingPermissionDeniedTitle": kIBGAudioRecordingPermissionDeniedTitleStringName,
        @"audioRecordingPermissionDeniedMessage": kIBGAudioRecordingPermissionDeniedMessageStringName,
        @"microphonePermissionAlertSettingsButtonTitle": kIBGMicrophonePermissionAlertSettingsButtonTitleStringName,
        @"conversationsHeaderTitle": kIBGChatsTitleStringName,
        @"chatsHeaderTitle": kIBGChatsTitleStringName,
        @"team": kIBGTeamStringName,
        @"recordingMessageToHoldText": kIBGRecordingMessageToHoldTextStringName,
        @"recordingMessageToReleaseText": kIBGRecordingMessageToReleaseTextStringName,
        @"messagesNotification": kIBGMessagesNotificationTitleSingleMessageStringName,
        @"messagesNotificationAndOthers": kIBGMessagesNotificationTitleMultipleMessagesStringName,
        @"screenshotHeaderTitle": kIBGScreenshotTitleStringName,
        @"okButtonTitle": kIBGOkButtonTitleStringName,
        @"cancelButtonTitle": kIBGCancelButtonTitleStringName,
        @"thankYouText": kIBGThankYouAlertTitleStringName,
        @"audio": kIBGAudioStringName,
        @"image": kIBGImageStringName,
        @"screenRecording": kIBGScreenRecordingStringName,
        @"surveyEnterYourAnswer": kIBGSurveyEnterYourAnswerTextPlaceholder,
        @"videoPressRecord": kIBGVideoPressRecordTitle,
        @"collectingDataText": kIBGCollectingDataText,
        @"thankYouAlertText": kIBGThankYouAlertMessageStringName,

        @"welcomeMessageBetaWelcomeStepTitle": kIBGBetaWelcomeMessageWelcomeStepTitle,
        @"welcomeMessageBetaWelcomeStepContent": kIBGBetaWelcomeMessageWelcomeStepContent,
        @"welcomeMessageBetaHowToReportStepTitle": kIBGBetaWelcomeMessageHowToReportStepTitle,
        @"welcomeMessageBetaHowToReportStepContent": kIBGBetaWelcomeMessageHowToReportStepContent,
        @"welcomeMessageBetaFinishStepTitle": kIBGBetaWelcomeMessageFinishStepTitle,
        @"welcomeMessageBetaFinishStepContent": kIBGBetaWelcomeMessageFinishStepContent,
        @"welcomeMessageLiveWelcomeStepTitle": kIBGLiveWelcomeMessageTitle,
        @"welcomeMessageLiveWelcomeStepContent": kIBGLiveWelcomeMessageContent,

        @"surveysStoreRatingThanksTitle": kIBGStoreRatingThankYouTitleText,
        @"surveysStoreRatingThanksSubtitle": kIBGStoreRatingThankYouDescriptionText,

        @"reportBugDescription": kIBGReportBugDescriptionStringName,
        @"reportFeedbackDescription": kIBGReportFeedbackDescriptionStringName,
        @"reportQuestionDescription": kIBGReportQuestionDescriptionStringName,
        @"requestFeatureDescription": kIBGRequestFeatureDescriptionStringName,

        @"discardAlertTitle": kIBGDiscardAlertTitle,
        @"discardAlertMessage": kIBGDiscardAlertMessage,
        @"discardAlertCancel": kIBGDiscardAlertCancel,
        @"discardAlertAction": kIBGDiscardAlertAction,
        @"addAttachmentButtonTitleStringName": kIBGAddAttachmentButtonTitleStringName,

        @"reportReproStepsDisclaimerBody": kIBGReproStepsDisclaimerBody,
        @"reportReproStepsDisclaimerLink": kIBGReproStepsDisclaimerLink,
        @"reproStepsProgressDialogBody": kIBGProgressViewTitle,
        @"reproStepsListHeader": kIBGReproStepsListTitle,
        @"reproStepsListDescription": kIBGReproStepsListHeader,
        @"reproStepsListEmptyStateDescription": kIBGReproStepsListEmptyStateLabel,
        @"reproStepsListItemTitle": kIBGReproStepsListItemName,

        @"insufficientContentMessage": kIBGInsufficientContentMessageStringName,
        @"insufficientContentTitle": kIBGInsufficientContentTitleStringName,
    };
}

+ (ArgsDictionary *) reproStepsModes {
    return @{
        @"enabled": @(IBGUserStepsModeEnable),
        @"disabled": @(IBGUserStepsModeDisable),
        @"enabledWithNoScreenshots": @(IBGUserStepsModeEnabledWithNoScreenshots),
    };
}

+ (ArgsDictionary *) locales {
    return @{
        @"arabic": @(IBGLocaleArabic),
        @"azerbaijani": @(IBGLocaleAzerbaijani),
        @"chineseSimplified": @(IBGLocaleChineseSimplified),
        @"chineseTraditional": @(IBGLocaleChineseTraditional),
        @"czech": @(IBGLocaleCzech),
        @"danish": @(IBGLocaleDanish),
        @"dutch": @(IBGLocaleDutch),
        @"english": @(IBGLocaleEnglish),
        @"finnish": @(IBGLocaleFinnish),
        @"french": @(IBGLocaleFrench),
        @"german": @(IBGLocaleGerman),
        @"hungarian": @(IBGLocaleHungarian),
        @"italian": @(IBGLocaleItalian),
        @"japanese": @(IBGLocaleJapanese),
        @"korean": @(IBGLocaleKorean),
        @"norwegian": @(IBGLocaleNorwegian),
        @"polish": @(IBGLocalePolish),
        @"portugueseBrazil": @(IBGLocalePortugueseBrazil),
        @"portuguesePortugal": @(IBGLocalePortuguese),
        @"romanian": @(IBGLocaleRomanian),
        @"russian": @(IBGLocaleRussian),
        @"slovak": @(IBGLocaleSlovak),
        @"spanish": @(IBGLocaleSpanish),
        @"swedish": @(IBGLocaleSwedish),
        @"turkish": @(IBGLocaleTurkish),
    };
}

@end
