import { exec } from "./IBGCordova";
import registry from "./ArgsRegistry";
import bugReporting from "./BugReporting";

namespace Instabug {
  export const welcomeMessageMode = registry.welcomeMessageMode;
  export const floatingButtonEdge = registry.floatingButtonEdge;
  export const colorTheme = registry.colorTheme;
  export const strings = registry.strings;
  export const reproStepsMode = registry.reproStepsMode;
  export const locale = registry.locale;

  /**
   * Starts the SDK.
   * This is the main SDK method that does all the magic. This is the only
   * method that SHOULD be called.
   * Should be called in constructor of the AppRegistry component
   *
   * @param token the token that identifies the app, you can find
   * it on your dashboard.
   * @param invocationEvents an array of invocationEvents that invoke
   * the SDK's UI.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const start = (
    token: string,
    invocationEvents: bugReporting.invocationEvents[],
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "start", [token, invocationEvents], success, error);
  };

  /**
   * Shows default Instabug prompt.
   *
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const show = (success?: () => void, error?: (err: any) => void) => {
    exec("IBGPlugin", "show", [], success, error);
  };

  /**
   * Sets the primary color of the SDK's UI.
   * Sets the color of UI elements indicating interactivity or call to action.
   * To use, import processColor and pass to it with argument the color hex
   * as argument.
   *
   * @param color a color hex to set the UI elements of the SDK to.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setPrimaryColor = (
    color: string,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setPrimaryColor", [color], success, error);
  };

  /**
   * Logs a user event that happens through the lifecycle of the application.
   * Logged user events are going to be sent with each report, as well as at the end of a session.
   *
   * @param userEvent the user event name.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const logUserEventWithName = (
    userEvent: string,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "logUserEventWithName", [userEvent], success, error);
  };

  /**
   * Sets whether user steps tracking is visual, non visual or disabled.
   * User Steps tracking is enabled by default if it's available
   * in your current plan.
   *
   * @param reproStepsMode an enum to set user steps tracking
   * to be enabled, non visual or disabled.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setReproStepsMode = (
    reproStepsMode: registry.reproStepsMode | `${registry.reproStepsMode}`,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setReproStepsMode", [reproStepsMode], success, error);
  };

  /**
   * The session profiler is enabled by default and it attaches to the bug and
   * crash reports the following information during the last 60 seconds before the report is sent.
   * @param isEnabled a boolean to enable or disable the feature.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setSessionProfilerEnabled = (
    isEnabled: boolean,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setSessionProfilerEnabled", [isEnabled], success, error);
  };

  /**
   * Sets the welcome message mode to live, beta or disabled.
   * @param mode an enum to set the welcome message mode.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setWelcomeMessageMode = (
    mode: registry.welcomeMessageMode,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setWelcomeMessageMode", [mode], success, error);
  };

  /**
   * Shows the welcome message in a specific mode.
   * @param mode an enum to set the mode to show the welcome message with.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const showWelcomeMessage = (
    mode: registry.welcomeMessageMode,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "showWelcomeMessage", [mode], success, error);
  };

  /**
   * Attaches user data to each report being sent.
   * Each call to this method overrides the user data to be attached.
   * Maximum size of the string is 1,000 characters.
   *
   * @param data a string to be attached to each report, with a
   * maximum size of 1,000 characters.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setUserData = (
    data: string,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setUserData", [data], success, error);
  };

  /**
   * Add file to be attached to the bug report.
   *
   * @param filePath the path of the file to be attached.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const addFile = (
    filePath: string,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "addFile", [filePath], success, error);
  };

  /**
   * Appends a log message to Instabug internal log
   * <p>
   * These logs are then sent along the next uploaded report.
   * All log messages are timestamped <br/>
   * Logs aren't cleared per single application run.
   * If you wish to reset the logs,
   * use {@link #clearLogs()} ()}
   * </p>
   * Note: logs passed to this method are <b>NOT</b> printed to Logcat
   *
   * @param message the log message.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const addLog = (
    message: string,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "addLog", [message], success, error);
  };

  /**
   * Clear all Instabug logs, console logs, network logs and user steps.
   *
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const clearLog = (
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "clearLog", [], success, error);
  };

  /**
   * Sets whether IBGLog should also print to Xcode's console log or not.
   *
   * @param isEnabled a boolean to set whether printing to
   *                  Xcode's console is enabled or not.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setIBGLogPrintsToConsole = (
    isEnabled: boolean,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setIBGLogPrintsToConsole", [isEnabled], success, error);
  };

  /**
   * Disables all Instabug functionality
   * It works on android only
   *
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const disable = (success?: () => void, error?: (err: any) => void) => {
    exec("IBGPlugin", "disable", [], success, error);
  };

  /**
   * Enables all Instabug functionality
   * It works on android only
   *
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const enable = (success?: () => void, error?: (err: any) => void) => {
    exec("IBGPlugin", "enable", [], success, error);
  };

  /**
   * Gets a boolean indicating whether the SDK is enabled or not
   * It works on android only
   *
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const isEnabled = (
    success: (isEnabled: boolean) => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "getIsEnabled", [], success, error);
  };

  /**
   * Sets user attribute to overwrite it's value or create a new one if it doesn't exist.
   *
   * @param key   the attribute key.
   * @param value the attribute value.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setUserAttribute = (
    key: string,
    value: string,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setUserAttribute", [key, value], success, error);
  };

  /**
   * Removes user attribute if exists.
   *
   * @param key the attribute key as string.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const removeUserAttribute = (
    key: string,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "removeUserAttribute", [key], success, error);
  };

  /**
   * Returns all user attributes.
   *
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const getAllUserAttributes = function (
    success: (userAttributes: { key: string; value: string }[]) => void,
    error?: (err: any) => void
  ) {
    exec("IBGPlugin", "getAllUserAttributes", [], success, error);
  };

  /**
   * Returns the user attribute associated with a given key.
   *
   * @param key the attribute key as string.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const getUserAttribute = (
    key: string,
    success: (value: string) => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "getUserAttribute", [key], success, error);
  };

  /**
   * Sets the default value of the user's email and hides the email field from the reporting UI
   * and set the user's name to be included with all reports.
   * It also reset the chats on device to that email and removes user attributes,
   * user data and completed surveys.
   *
   * @param email the email address to be set as the user's email.
   * @param name the name of the user to be set.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const identifyUserWithEmail = (
    email: string,
    name: string,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "identifyUserWithEmail", [email, name], success, error);
  };

  export const setPreSendingHandler = (
    success: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setPreSendingHandler", [], success, error);
  };

  /**
   * Sets the default value of the user's email to nil and show email field and remove user name
   * from all reports
   * It also reset the chats on device and removes user attributes, user data and completed surveys.
   *
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const logOut = (success?: () => void, error?: (err: any) => void) => {
    exec("IBGPlugin", "logOut", [], success, error);
  };

  /**
   * Enable/Disable debug logs from Instabug SDK
   * Default state: disabled
   *
   * @param isDebugEnabled a boolean to control whether debug logs should be printed or not into LogCat.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setDebugEnabled = (
    isDebugEnabled: boolean,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setDebugEnabled", [isDebugEnabled], success, error);
  };

  /**
   * Sets the SDK's locale.
   * Use to change the SDK's UI to different language.
   * Defaults to the device's current locale.
   *
   * @param locale a locale to set the SDK to.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setLocale = (
    locale: registry.locale | `${registry.locale}`,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setLocale", [locale], success, error);
  };

  /**
   * Sets SDK color theme.
   *
   * @param theme the color theme to set the SDK UI to.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setColorTheme = (
    theme: registry.colorTheme,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setColorTheme", [theme], success, error);
  };

  /**
   * Overrides any of the strings shown in the SDK with custom ones.
   * Allows you to customize any of the strings shown to users in the SDK.
   * @param key the key of the string to override.
   * @param value the string value to override the default one.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setString = (
    key: registry.strings,
    value: string,
    success?: () => void,
    error?: (err: any) => void
  ) => {
    exec("IBGPlugin", "setString", [key, value], success, error);
  };
}
export = Instabug;
