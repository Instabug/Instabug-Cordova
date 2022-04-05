const fs = require("fs");
const path = require('path');

// Overwrites both the project file (.pbxproj) and the workspace scheme (xcscheme)
// by the files found in src/ios.
// 
// The new files should contains two targets as follows:
// 1. InstabugExampleTests   : unit tests bundle target
// 2. InstabugExampleUITests : ui tests bundle target

module.exports = (ctx) => {
  const srcRoot = path.join(ctx.opts.plugin.dir, "src/ios");
  const iosRoot = path.join(ctx.opts.projectRoot, "platforms/ios");

  const projectFile = "InstabugExample.xcodeproj/project.pbxproj";

  fs.copyFile(
    path.join(srcRoot, projectFile),
    path.join(iosRoot, projectFile),
    (err) => {
      if (err) throw err;
      console.log(`${projectFile} copied successfully!`);
    }
  );

  const schemeFile =
    "InstabugExample.xcworkspace/xcshareddata/xcschemes/InstabugExample.xcscheme";

  fs.copyFile(
    path.join(srcRoot, schemeFile),
    path.join(iosRoot, schemeFile),
    (err) => {
      if (err) throw err;
      console.log(`${schemeFile} copied successfully!`);
    }
  );
};
