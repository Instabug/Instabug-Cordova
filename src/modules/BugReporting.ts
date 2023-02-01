/*
 * Instabug Bug Reporting module.
 */

import { exec } from "./IBGCordova";
import registry from "./ArgsRegistry";

namespace BugReporting {
  export enum reportType {
    bug = "bug",
    feedback = "feedback",
    question = "question",
  }

  export enum option {
    emailFieldHidden = "emailFieldHidden",
    emailFieldOptional = "emailFieldOptional",
    commentFieldRequired = "commentFieldRequired",
    disablePostSendingDialog = "disablePostSendingDialog",
  }

  export enum invocationEvents {
    shake = "shake",
    button = "button",
    screenshot = "screenshot",
    swipe = "swipe",
    none = "none",
  }

  export enum extendedBugReportMode {
    enabledWithRequiredFields = "enabledWithRequiredFields",
    enabledWithOptionalFields = "enabledWithOptionalFields",
    disabled = "disabled",
  }

  export const position = registry.position;

  /**
   * Enables or disables all bug reporting functionalities.
   * @param isEnabled a boolean to enable or disable the feature.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setEnabled = (
    isEnabled: boolean,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setBugReportingEnabled", [isEnabled], success, error);
  };

  /**
   * Sets report type either bug, feedback or both.
   * @param reportTypes an array of reportType.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setReportTypes = (
    reportTypes: reportType[],
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setReportTypes", [reportTypes], success, error);
  };

  /**
   * Shows report view with specified options.
   * @param reportType an enum of reportType.
   * @param options an array of invocation option.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const showWithOptions = (
    reportType: reportType,
    options: option[],
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec(
      "IBGPlugin",
      "showBugReportingWithReportTypeAndOptions",
      [reportType, options],
      success,
      error
    );
  };

  /**
   * Sets the invocation options.
   * Default is set by `Instabug.start`.
   * @param options an array of invocation option.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setOptions = (
    options: option[],
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setInvocationOptions", [options], success, error);
  };

  /**
   * Sets a block of code to be executed just before the SDK's UI is presented.
   * This block is executed on the UI thread. Could be used for performing any
   * UI changes before the SDK's UI is shown.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setOnInvokeHandler = (
    success: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setPreInvocationHandler", [], success, error);
  };

  /**
   * Sets a block of code to be executed right after the SDK's UI is dismissed.
   * This block is executed on the UI thread. Could be used for performing any
   * UI changes after the SDK's UI is dismissed.
   * @param success callback on function success; param includes reportType and dismissType.
   * @param error callback on function error.
   */
  export const setOnDismissHandler = (
    success: (data: { reportType: string; dismissType: string }) => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setPostInvocationHandler", [], success, error);
  };

  /**
   * Sets the events that will invoke the SDK.
   * @param events an array of invocationEvents.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setInvocationEvents = (
    events: invocationEvents[],
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setInvocationEvents", [events], success, error);
  };

  /**
   * Sets enabled types of attachments for bug reporting.
   * @param screenshot a boolean to enable/disable screenshot attachment.
   * @param extraScreenshot a boolean to enable/disable extra screenshot attachment.
   * @param galleryImage a boolean to enable/disable gallery image attachment.
   * @param screenRecording a boolean to enable/disable screen recording attachment.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setEnabledAttachmentTypes = (
    screenshot: boolean,
    extraScreenshot: boolean,
    galleryImage: boolean,
    screenRecording: boolean,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec(
      "IBGPlugin",
      "setAttachmentTypesEnabled",
      [screenshot, extraScreenshot, galleryImage, screenRecording],
      success,
      error
    );
  };

  /**
   *
   * @param extendedBugReportMode an enum of extendedBugReportMode.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setExtendedBugReportMode = (
    extendedBugReportMode: extendedBugReportMode,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec(
      "IBGPlugin",
      "setExtendedBugReportMode",
      [extendedBugReportMode],
      success,
      error
    );
  };

  /**
   * Sets the default edge and offset from the top at which the floating button
   * will be shown. Different orientations are already handled.
   * @param edge the position of the edge; the default is right.
   * @param offset the offset value from the top edge.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setFloatingButtonEdge = (
    edge: registry.floatingButtonEdge,
    offset: number,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setFloatingButtonEdge", [edge, offset], success, error);
  };

  /**
   * Sets the threshold value of the shake gesture for iPhone/iPod Touch
   * Default for iPhone is 2.5.
   * @param threshold the shaking threshold for iPhone.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setShakingThresholdForiPhone = (
    threshold: number,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec(
      "IBGPlugin",
      "setShakingThresholdForiPhone",
      [threshold],
      success,
      error
    );
  };

  /**
   * Sets the threshold value of the shake gesture for iPad.
   * Default for iPad is 0.6.
   * @param threshold the shaking threshold for iPad.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setShakingThresholdForiPad = (
    threshold: number,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec(
      "IBGPlugin",
      "setShakingThresholdForiPad",
      [threshold],
      success,
      error
    );
  };

  /**
   * Sets the threshold value of the shake gesture for android devices.
   * Default for android is an integer value equals 350.
   * you could increase the shaking difficulty level by
   * increasing the `350` value and vice versa
   * @param threshold the shaking threshold for android devices.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setShakingThresholdForAndroid = (
    threshold: number,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setShakingThreshold", [threshold], success, error);
  };

  /**
   * Sets the default position at which the Instabug screen recording button will be shown.
   * Different orientations are already handled.
   * (Default for `position` is `bottomRight`)
   *
   * @param position an enum of position to control the video recording button position on the screen.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setVideoRecordingFloatingButtonPosition = (
    position: registry.position,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec(
      "IBGPlugin",
      "setVideoRecordingFloatingButtonPosition",
      [position],
      success,
      error
    );
  };

  /**
   * Adds a disclaimer text within the bug reporting form, which can include hyperlinked text.
   * @param text a String of the disclaimer text.
   * @param success callback on function success.
   * @param error callback on function error.
  */
  export const setDisclaimerText = (
    text: string, 
    success?: () => void,
    error?: (err: any) => void 
  ) => {
    exec("IBGPlugin", "setDisclaimerText", [text], success, error);
  };

  /**
   * Sets a minimum number of characters as a requirement for the comments field in the different report types.
   * @param limit an integer number of characters.
   * @param reportTypes an optional an array of reportType. If it's not passed, the limit will apply to all report types.
   * @param success callback on function success.
   * @param error callback on function error.
  */
  export const setCommentMinimumCharacterCount = (
    limit: number, 
    reportTypes?: reportType[],
    success?: () => void,
    error?: (err: any) => void 
  ) => {
    exec("IBGPlugin", "setCommentMinimumCharacterCount", [limit, reportTypes], success, error);
  };
}
export = BugReporting;
