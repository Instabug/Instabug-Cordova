document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
  console.log("Running cordova-" + cordova.platformId + "@" + cordova.version);
  document.getElementById("deviceready").classList.add("ready");

  // Initialize Instabug SDK
  cordova.plugins.instabug.start(
    "cb518c145decef204b0735cb7efa6016",
    [cordova.plugins.bugReporting.invocationEvents.button],
    () => console.log("Instabug initialized."),
    (error) => console.log("Instabug could not be initialized - " + error)
  );
}
