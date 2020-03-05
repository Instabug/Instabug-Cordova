const editManifest = require('./edit_manifest');
require('./unlink_gradle');

module.exports = (ctx) => {
    return editManifest(ctx, false);
}