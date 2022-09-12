/*
 * Instabug Bug Reporting module.
 */

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
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setBugReportingEnabled", [
      isEnabled,
    ]);
  };

  /**
   * Sets report type either bug, feedback or both.
   * @param reportTypes an array of reportType.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setReportTypes = (
    reportTypes: reportType[],
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setReportTypes", [reportTypes]);
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
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(
      success,
      error,
      "IBGPlugin",
      "showBugReportingWithReportTypeAndOptions",
      [reportType, options]
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
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setInvocationOptions", [
      options,
    ]);
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
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setPreInvocationHandler", []);
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
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setPostInvocationHandler", []);
  };

  /**
   * Sets the events that will invoke the SDK.
   * @param events an array of invocationEvents.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setInvocationEvents = (
    events: invocationEvents[],
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setInvocationEvents", [events]);
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
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setAttachmentTypesEnabled", [
      screenshot,
      extraScreenshot,
      galleryImage,
      screenRecording,
    ]);
  };

  /**
   *
   * @param extendedBugReportMode an enum of extendedBugReportMode.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setExtendedBugReportMode = (
    extendedBugReportMode: extendedBugReportMode,
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setExtendedBugReportMode", [
      extendedBugReportMode,
    ]);
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
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setFloatingButtonEdge", [
      edge,
      offset,
    ]);
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
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setShakingThresholdForiPhone", [
      threshold,
    ]);
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
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setShakingThresholdForiPad", [
      threshold,
    ]);
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
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setShakingThreshold", [
      threshold,
    ]);
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
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(
      success,
      error,
      "IBGPlugin",
      "setVideoRecordingFloatingButtonPosition",
      [position]
    );
  };
}
export = BugReporting;
