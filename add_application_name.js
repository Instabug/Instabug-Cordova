module.exports = function(context) {

  var APPLICATION_CLASS = "com.instabug.cordova.plugin.MyApplication";

  var fs = context.requireCordovaModule('fs'),
      path = context.requireCordovaModule('path');

  var platformRoot = path.join(context.opts.projectRoot, 'platforms/android');
  var manifestFile = path.join(platformRoot, 'AndroidManifest.xml');

  if (fs.existsSync(manifestFile)) {
    fs.readFile(manifestFile, 'utf8', function (err, data) {
      if (err) {
        throw new Error('Unable to find AndroidManifest.xml: ' + err);
      }

      if (data.indexOf(APPLICATION_CLASS) == -1) {
          var positionStart = data.indexOf("<application");
          var positionEnd = data.substr(positionStart,data.length).indexOf(">");
          var newApplicationTag = adjustApplicationTag(data.substr(positionStart,positionEnd+1));
          var result = data.substr(0,positionStart)+newApplicationTag+data.substr(positionEnd+positionStart+1,data.length);
        fs.writeFile(manifestFile, result, 'utf8', function (err) {
          if (err) throw new Error('Unable to write into AndroidManifest.xml: ' + err);
        })
      }
    });
  }
};

var adjustApplicationTag = function(applicationTag) {
  var APPLICATION_CLASS = "com.instabug.cordova.plugin.MyApplication";
  var result;
  if(applicationTag.match(/android:name="([A-z]+.)+"/g)) {
    result = applicationTag.replace(/android:name="([A-z]+.)+"/g, 'android:name="' + APPLICATION_CLASS + '"');
  } else {
    result = applicationTag.replace(/<application/g, '<application android:name="' + APPLICATION_CLASS + '"');
  }

  return result;

}