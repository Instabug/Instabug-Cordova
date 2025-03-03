
document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
  /** @type {import('instabug-cordova/www/Instabug')} */
  var Instabug = cordova.require("instabug-cordova.Instabug");
  /** @type {import('instabug-cordova/www/BugReporting')} */
  var BugReporting = cordova.require("instabug-cordova.BugReporting");
  var ArgsRegistry = cordova.require("instabug-cordova.ArgsRegistry")
  console.log("Running cordova-" + cordova.platformId + "@" + cordova.version);
  document.getElementById("deviceready").classList.add("ready");
  Instabug.init({
    token: 'cb518c145decef204b0735cb7efa6016',
    invocationEvents: [BugReporting.invocationEvents.button],
    debugLogsLevel: ArgsRegistry.logLeve.verbose,
  });

}
