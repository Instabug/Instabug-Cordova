const fs = require('fs');
const path = require('path');
const Q = require('q');
module.exports = (ctx, isInstalling) => {
  const xml = ctx.requireCordovaModule('cordova-common').xmlHelpers;

  const deferred = Q.defer();
  const platformPath = path.join(ctx.opts.projectRoot, './platforms/android');
  const manifestPaths = [
    path.join(platformPath, './AndroidManifest.xml'),
    path.join(platformPath, './app/src/main/AndroidManifest.xml')
  ];

  const manifestPath = manifestPaths.find(filepath => {
    try {
      fs.accessSync(filepath, fs.constants.F_OK);
      return true;
    } catch (err) {
      return false;
    }
  });

  if (manifestPath != null) {
    const appName = 'com.instabug.cordova.plugin.MyApplication';
    let doc = xml.parseElementtreeSync(manifestPath);
    let appAttr = doc.getroot().find('./application').attrib['android:name'];

    if (isInstalling) {
      if (!appAttr) {
        doc.getroot().find('./application').attrib['android:name'] = appName;
      }
    } else {
      if (appAttr === appName) {
        delete doc.getroot().find('./application').attrib['android:name'];
      }
    }
    
    fs.writeFileSync(manifestPath, doc.write({ indent: 4 }));
    deferred.resolve();
  } else {
    deferred.reject(new Error("Can't find AndroidManifest.xml"));
  }

  return deferred.promise;
};
