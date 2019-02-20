const editManifest = require('./edit_manifest');
module.exports = (ctx) => {
    return editManifest(ctx, false);
}