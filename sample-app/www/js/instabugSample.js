/**
 * Created by hossam on 3/16/17.
 */

function startInstabug() {
    // navigator.notification.alert("message", invoke, "title", "button");
    invoke();
}

function invoke() {
    cordova.plugins.instabug.invoke(
        'options'
        ,
        function () {
            console.log('Instabug initialized.');
        },
        function (error) {
            console.log('Instabug could not be initialized - ' + error);
        }
    )
}