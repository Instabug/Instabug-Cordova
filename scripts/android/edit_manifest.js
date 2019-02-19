module.exports = function(ctx) {
    console.log('INNN');
    const fs = ctx.requireCordovaModule('fs');
    const path = ctx.requireCordovaModule('path');
    const Q = ctx.requireCordovaModule('q');
    const xml = ctx.requireCordovaModule('cordova-common').xmlHelpers;

    const deferred = Q.defer();
    const platformPath = path.join(ctx.opts.projectRoot, './platforms/android');
    const manifestPaths = [
        path.join(platformPath, './AndroidManifest.xml'),
        path.join(platformPath, './app/src/main/AndroidManifest.xml'),
    ];

    const manifestPath = manifestPaths.find((filepath) => {
        try {
            fs.accessSync(filepath, fs.constants.F_OK)
            return true
        } catch (err) {
            return false
        }
    });

    var doc;

    if (manifestPath != null) {
        doc = xml.parseElementtreeSync(manifestPath)
        doc.getroot().find('./application').attrib['android:name'] =
            'com.instabug.cordova.plugin.MyApplication'
        fs.writeFileSync(manifestPath, doc.write({ indent: 4 }))
        deferred.resolve()
    } else {
        deferred.reject(new Error("Can't find AndroidManifest.xml"))
    }

    return deferred.promise
}