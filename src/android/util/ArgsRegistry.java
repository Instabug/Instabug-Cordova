package com.instabug.cordova.plugin.util;

import androidx.annotation.NonNull;

import com.instabug.library.InstabugColorTheme;
import com.instabug.library.InstabugCustomTextPlaceHolder.Key;
import com.instabug.library.invocation.util.InstabugFloatingButtonEdge;
import com.instabug.library.ui.onboarding.WelcomeMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class ArgsRegistry {

    public static class ArgsMap<T> extends HashMap<String, T> {
        @NonNull
        @Override
        public T getOrDefault(Object key, T defaultValue) {
            final T value = get(key);
            return value != null ? value : defaultValue;
        }

        public ArrayList<T> getAll(ArrayList<String> keys) {
            final ArrayList<T> values = new ArrayList<>();
            for (String key : keys) {
                values.add(get(key));
            }
            return values;
        }
    }

    public static ArgsMap<WelcomeMessage.State> welcomeMessageModes = new ArgsMap<WelcomeMessage.State>() {{
        put("welcomeMessageModeLive", WelcomeMessage.State.LIVE);
        put("welcomeMessageModeBeta", WelcomeMessage.State.BETA);
        put("welcomeMessageModeDisabled", WelcomeMessage.State.DISABLED);
    }};

    public static final ArgsMap<InstabugColorTheme> colorThemes = new ArgsMap<InstabugColorTheme>() {{
        put("light", InstabugColorTheme.InstabugColorThemeLight);
        put("dark", InstabugColorTheme.InstabugColorThemeDark);
    }};

    public static final ArgsMap<InstabugFloatingButtonEdge> floatingButtonEdges = new ArgsMap<InstabugFloatingButtonEdge>() {{
        put("left", InstabugFloatingButtonEdge.LEFT);
        put("right", InstabugFloatingButtonEdge.RIGHT);
    }};

    public static final ArgsMap<Key> placeholders = new ArgsMap<Key>() {{
        put("shakeHint", Key.SHAKE_HINT);
        put("swipeHint", Key.SWIPE_HINT);
        put("invalidEmailMessage", Key.INVALID_EMAIL_MESSAGE);
        put("invalidCommentMessage", Key.INVALID_COMMENT_MESSAGE);
        put("emailFieldHint", Key.EMAIL_FIELD_HINT);
        put("commentFieldHintForBugReport", Key.COMMENT_FIELD_HINT_FOR_BUG_REPORT);
        put("commentFieldHintForFeedback", Key.COMMENT_FIELD_HINT_FOR_FEEDBACK);
        put("commentFieldHintForQuestion", Key.COMMENT_FIELD_HINT_FOR_QUESTION);
        put("invocationHeader", Key.INVOCATION_HEADER);
        put("reportQuestion", Key.REPORT_QUESTION);
        put("reportBug", Key.REPORT_BUG);
        put("reportFeedback", Key.REPORT_FEEDBACK);
        put("conversationsHeaderTitle", Key.CONVERSATIONS_LIST_TITLE);
        put("addVoiceMessage", Key.ADD_VOICE_MESSAGE);
        put("addImageFromGallery", Key.ADD_IMAGE_FROM_GALLERY);
        put("addExtraScreenshot", Key.ADD_EXTRA_SCREENSHOT);
        put("addVideoMessage", Key.ADD_VIDEO);
        put("audioRecordingPermissionDeniedMessage", Key.AUDIO_RECORDING_PERMISSION_DENIED);
        put("recordingMessageToHoldText", Key.VOICE_MESSAGE_PRESS_AND_HOLD_TO_RECORD);
        put("recordingMessageToReleaseText", Key.VOICE_MESSAGE_RELEASE_TO_ATTACH);
        put("thankYouText", Key.SUCCESS_DIALOG_HEADER);
        put("video", Key.VIDEO_PLAYER_TITLE);
        put("videoPressRecord", Key.VIDEO_RECORDING_FAB_BUBBLE_HINT);
        put("conversationTextFieldHint", Key.CONVERSATION_TEXT_FIELD_HINT);
        put("thankYouAlertText", Key.REPORT_SUCCESSFULLY_SENT);

        put("welcomeMessageBetaWelcomeStepTitle", Key.BETA_WELCOME_MESSAGE_WELCOME_STEP_TITLE);
        put("welcomeMessageBetaWelcomeStepContent", Key.BETA_WELCOME_MESSAGE_WELCOME_STEP_CONTENT);
        put("welcomeMessageBetaHowToReportStepTitle", Key.BETA_WELCOME_MESSAGE_HOW_TO_REPORT_STEP_TITLE);
        put("welcomeMessageBetaHowToReportStepContent", Key.BETA_WELCOME_MESSAGE_HOW_TO_REPORT_STEP_CONTENT);
        put("welcomeMessageBetaFinishStepTitle", Key.BETA_WELCOME_MESSAGE_FINISH_STEP_TITLE);
        put("welcomeMessageBetaFinishStepContent", Key.BETA_WELCOME_MESSAGE_FINISH_STEP_CONTENT);
        put("welcomeMessageLiveWelcomeStepTitle", Key.LIVE_WELCOME_MESSAGE_TITLE);
        put("welcomeMessageLiveWelcomeStepContent", Key.LIVE_WELCOME_MESSAGE_CONTENT);

        put("surveysStoreRatingThanksTitle", Key.SURVEYS_STORE_RATING_THANKS_SUBTITLE);
        put("surveysStoreRatingThanksSubtitle", Key.SURVEYS_STORE_RATING_THANKS_SUBTITLE);

        put("reportBugDescription", Key.REPORT_BUG_DESCRIPTION);
        put("reportFeedbackDescription", Key.REPORT_FEEDBACK_DESCRIPTION);
        put("reportQuestionDescription", Key.REPORT_QUESTION_DESCRIPTION);
        put("requestFeatureDescription", Key.REQUEST_FEATURE_DESCRIPTION);

        put("discardAlertTitle", Key.REPORT_DISCARD_DIALOG_TITLE);
        put("discardAlertMessage", Key.REPORT_DISCARD_DIALOG_BODY);
        put("discardAlertCancel", Key.REPORT_DISCARD_DIALOG_NEGATIVE_ACTION);
        put("discardAlertAction", Key.REPORT_DISCARD_DIALOG_POSITIVE_ACTION);
        put("addAttachmentButtonTitleStringName", Key.REPORT_ADD_ATTACHMENT_HEADER);

        put("reportReproStepsDisclaimerBody", Key.REPORT_REPRO_STEPS_DISCLAIMER_BODY);
        put("reportReproStepsDisclaimerLink", Key.REPORT_REPRO_STEPS_DISCLAIMER_LINK);
        put("reproStepsProgressDialogBody", Key.REPRO_STEPS_PROGRESS_DIALOG_BODY);
        put("reproStepsListHeader", Key.REPRO_STEPS_LIST_HEADER);
        put("reproStepsListDescription", Key.REPRO_STEPS_LIST_DESCRIPTION);
        put("reproStepsListEmptyStateDescription", Key.REPRO_STEPS_LIST_EMPTY_STATE_DESCRIPTION);
        put("reproStepsListItemTitle", Key.REPRO_STEPS_LIST_ITEM_NUMBERING_TITLE);
    }};
}