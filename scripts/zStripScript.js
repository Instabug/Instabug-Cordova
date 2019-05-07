'use strict';

const xcode = require('xcode'),
    fs = require('fs'),
    path = require('path');

module.exports = function(context) {
    if(process.length >=5 && process.argv[1].indexOf('cordova') == -1) {
        if(process.argv[4] != 'ios') {
            return; // plugin only meant to work for ios platform.
        }
    }

    function fromDir(startPath,filter, rec, multiple){
      if (!fs.existsSync(startPath)){
          console.log("no dir ", startPath);
          return;
      }

      const files=fs.readdirSync(startPath);
      var resultFiles = []
      for(var i=0;i<files.length;i++){
          var filename=path.join(startPath,files[i]);
          var stat = fs.lstatSync(filename);
          if (stat.isDirectory() && rec){
              fromDir(filename,filter); //recurse
          }

          if (filename.indexOf(filter)>=0) {
              if (multiple) {
                  resultFiles.push(filename);
              } else {
                  return filename;
              }
          }
      }
      if(multiple) {
          return resultFiles;
      }
    }

    const xcodeProjPath = fromDir('platforms/ios','.xcodeproj', false);
    const projectPath = xcodeProjPath + '/project.pbxproj';
    const myProj = xcode.project(projectPath);

    console.log(xcodeProjPath);
    console.log(projectPath);

    myProj.parseSync();

    // unquote (remove trailing ")
    // Removing the char " at beginning and the end.
    var projectName = myProj.getFirstTarget().firstTarget.name;

    if (projectName.substr(projectName.length - 1) == '"') {
        projectName = projectName.substr(1);
        projectName = projectName.substr(0, projectName.length - 1);
    }

    var options = {shellPath: '/bin/sh', shellScript: 'bash "${BUILT_PRODUCTS_DIR}/${FRAMEWORKS_FOLDER_PATH}/Instabug.framework/strip-frameworks.sh"', runOnlyForDeploymentPostprocessing: 0};
    var buildPhase = myProj.addBuildPhase([], 'PBXShellScriptBuildPhase', 'StripFrameworkScript', myProj.getFirstTarget().uuid, options).buildPhase;

    fs.chmod(projectPath, '0755', function(exc) {
        fs.writeFileSync(projectPath, myProj.writeSync());
    });
    
    fs.writeFileSync(projectPath, myProj.writeSync());
    console.log('Added Arch Trim run script build phase');
};
