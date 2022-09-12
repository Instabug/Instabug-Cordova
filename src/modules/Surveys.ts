/*
 * Instabug Surveys module.
 */
namespace Surveys {
  /**
   * Sets whether auto surveys showing are enabled or not.
   * @param autoShowingSurveysEnabled  a boolean for whether to auto show a survey.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setAutoShowingEnabled = (
    autoShowingSurveysEnabled: boolean,
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setAutoShowingSurveysEnabled", [
      autoShowingSurveysEnabled,
    ]);
  };

  /**
   * Sets whether surveys are enabled or not.
   * If you disable surveys on the SDK but still have active surveys on your Instabug dashboard,
   * those surveys are still going to be sent to the device, but are not going to be
   * shown automatically.
   * To manually display any available surveys, call `Instabug.showSurveyIfAvailable()`.
   * Defaults to `true`.
   * @param isEnabled a boolean to enable or disable the feature.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setEnabled = (
    isEnabled: boolean,
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setSurveysEnabled", [isEnabled]);
  };

  /**
   * Shows one of the surveys that were not shown before, that also have conditions
   * that match the current device/user.
   * Does nothing if there are no available surveys or if a survey has already been shown
   * in the current session.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const showSurveyIfAvailable = (
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "showSurveyIfAvailable", []);
  };

  /**
   * Sets a block of code to be executed just before the survey's UI is presented.
   * This block is executed on the UI thread. Could be used for performing any UI changes before
   * the survey's UI is shown.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setOnShowHandler = (
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "willShowSurveyHandler", []);
  };

  /**
   * Sets a block of code to be executed right after the survey's UI is dismissed.
   * This block is executed on the UI thread. Could be used for performing any UI
   * changes after the survey's UI is dismissed.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setOnDismissHandler = (
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "didDismissSurveyHandler", []);
  };

  /**
   * Shows survey with a specific token.
   * Does nothing if there are no available surveys with that specific token.
   * Answered and cancelled surveys won't show up again.
   * @param surveyToken a string with a survey token.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const showSurveyWithToken = (
    surveyToken: string,
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "showSurveyWithToken", [
      surveyToken,
    ]);
  };

  /**
   * Returns true if the survey with a specific token was answered before.
   * Will return false if the token does not exist or if the survey was not answered before.
   * @param surveyToken a string with a survey token.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const hasRespondedToSurveyWithToken = (
    surveyToken: string,
    success: (hasResponded: boolean) => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "hasRespondedToSurveyWithToken", [
      surveyToken,
    ]);
  };

  /**
   * Returns an array containing the available surveys.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const getAvailableSurveys = (
    success: (availableSurveys: string[]) => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "getAvailableSurveys", []);
  };

  /**
   * Setting an option for all the surveys to show a welcome screen before
   * the user starts taking the survey.
   * @param shouldShowWelcomeScreen  a boolean to control whether the welcome screen should show.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setShouldShowSurveysWelcomeScreen = (
    shouldShowWelcomeScreen: boolean,
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(
      success,
      error,
      "IBGPlugin",
      "setShouldShowSurveysWelcomeScreen",
      [shouldShowWelcomeScreen]
    );
  };
}
export = Surveys;
