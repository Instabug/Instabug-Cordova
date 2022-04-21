document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
  const Instabug = cordova.require("instabug-cordova.Instabug");
  const BugReporting = cordova.require("instabug-cordova.BugReporting");

  console.log("Running cordova-" + cordova.platformId + "@" + cordova.version);
  document.getElementById("deviceready").classList.add("ready");

  // Initialize Instabug SDK
  Instabug.start(
    "cb518c145decef204b0735cb7efa6016",
    [BugReporting.invocationEvents.button],
    () => console.log("Instabug initialized."),
    (error) => console.log("Instabug could not be initialized - " + error)
  );
}
