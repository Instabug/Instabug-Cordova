cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/com.wodify.cordova.plugin.instabug/www/instabug.js",
        "id": "com.wodify.cordova.plugin.instabug.Instabug",
        "clobbers": [
            "cordova.plugins.instabug"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.2.2",
    "me.tonny.cordova.plugins.multidex": "0.1.0",
    "com.wodify.cordova.plugin.instabug": "0.0.1"
};
// BOTTOM OF METADATA
});