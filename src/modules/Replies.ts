/*
 * Instabug Replies module.
 */
namespace Replies {
  /**
   * Enables or disables all replies functionalities.
   * @param isEnabled a boolean to enable or disable the feature.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setEnabled = (
    isEnabled: boolean,
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setRepliesEnabled", [isEnabled]);
  };

  /**
   * Shows replies.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const show = (success: () => void, error: (err: any) => void) => {
    cordova.exec(success, error, "IBGPlugin", "showReplies");
  };

  /**
   * Calls success callback if chats exist.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const hasChats = (success: () => void, error: (err: any) => void) => {
    cordova.exec(success, error, "IBGPlugin", "hasChats");
  };

  /**
   * Returns the number of unread replies for the user.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const getUnreadRepliesCount = (
    success: (repliesCount: number) => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "getUnreadRepliesCount");
  };

  /**
   * Enables in app notifications for any new reply received.
   * @param isEnabled a boolean to enable or disable in-app notifications.
   * @param success callback on function success.
   * @param error callback on function error.
   */
  export const setInAppNotificationEnabled = (
    isEnabled: boolean,
    success: () => void,
    error: (err: any) => void
  ) => {
    cordova.exec(success, error, "IBGPlugin", "setChatNotificationEnabled", [
      isEnabled,
    ]);
  };
}
export = Replies;
