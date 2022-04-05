package com.instabug.example;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebChromeClient;

import com.instabug.cordova.plugin.IBGPlugin;
import com.instabug.library.Instabug;
import com.instabug.library.InstabugColorTheme;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewEngine;
import org.apache.cordova.ICordovaCookieManager;
import org.apache.cordova.PluginEntry;
import org.apache.cordova.PluginManager;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Looper.class, android.os.Handler.class, Instabug.class, IBGPlugin.class, CallbackContext.class, CordovaWebView.class, JSONArray.class, Color.class})

public class IBGPluginTests {

    private IBGPlugin cordovaModule = new IBGPlugin();
    private CallbackContext callbackContext = new CallbackContext("", new CordovaWebView() {
        @Override
        public void init(CordovaInterface cordova, List<PluginEntry> pluginEntries, CordovaPreferences preferences) {

        }

        @Override
        public boolean isInitialized() {
            return false;
        }

        @Override
        public View getView() {
            return null;
        }

        @Override
        public void loadUrlIntoView(String url, boolean recreatePlugins) {

        }

        @Override
        public void stopLoading() {

        }

        @Override
        public boolean canGoBack() {
            return false;
        }

        @Override
        public void clearCache() {

        }

        @Override
        public void clearCache(boolean b) {

        }

        @Override
        public void clearHistory() {

        }

        @Override
        public boolean backHistory() {
            return false;
        }

        @Override
        public void handlePause(boolean keepRunning) {

        }

        @Override
        public void onNewIntent(Intent intent) {

        }

        @Override
        public void handleResume(boolean keepRunning) {

        }

        @Override
        public void handleStart() {

        }

        @Override
        public void handleStop() {

        }

        @Override
        public void handleDestroy() {

        }

        @Override
        public void sendJavascript(String statememt) {

        }

        @Override
        public void showWebPage(String url, boolean openExternal, boolean clearHistory, Map<String, Object> params) {

        }

        @Override
        public boolean isCustomViewShowing() {
            return false;
        }

        @Override
        public void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {

        }

        @Override
        public void hideCustomView() {

        }

        @Override
        public CordovaResourceApi getResourceApi() {
            return null;
        }

        @Override
        public void setButtonPlumbedToJs(int keyCode, boolean override) {

        }

        @Override
        public boolean isButtonPlumbedToJs(int keyCode) {
            return false;
        }

        @Override
        public void sendPluginResult(PluginResult cr, String callbackId) {

        }

        @Override
        public PluginManager getPluginManager() {
            return null;
        }

        @Override
        public CordovaWebViewEngine getEngine() {
            return null;
        }

        @Override
        public CordovaPreferences getPreferences() {
            return null;
        }

        @Override
        public ICordovaCookieManager getCookieManager() {
            return null;
        }

        @Override
        public String getUrl() {
            return null;
        }

        @Override
        public Context getContext() {
            return null;
        }

        @Override
        public void loadUrl(String url) {

        }

        @Override
        public Object postMessage(String id, Object data) {
            return null;
        }
    });

    @Before
    public void mockMainThreadHandler() throws Exception {
        PowerMockito.mockStatic(Looper.class);
        Looper mockMainThreadLooper = mock(Looper.class);
        when(Looper.getMainLooper()).thenReturn(mockMainThreadLooper);
        Handler mockMainThreadHandler = mock(Handler.class);
        Answer<Boolean> handlerPostAnswer = new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return true;
            }
        };
        doAnswer(handlerPostAnswer).when(mockMainThreadHandler).post(any(Runnable.class));
        doAnswer(handlerPostAnswer).when(mockMainThreadHandler).postDelayed(any(Runnable.class), anyLong());
        PowerMockito.whenNew(Handler.class).withArguments(mockMainThreadLooper).thenReturn(mockMainThreadHandler);
    }

    @Test
    public void givenArg$setPrimaryColor_whenQuery_thenShouldCallNativeApiWithArg() {
        // given
        PowerMockito.mockStatic(Instabug.class);
        PowerMockito.mockStatic(Color.class);
        int color = 3902;
        when(Color.parseColor(anyString())).thenReturn(color);
        // when
        JSONArray args = new JSONArray();
        args.put(color + "");

        cordovaModule.execute("setPrimaryColor", args, callbackContext);
        // then
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        Instabug.setPrimaryColor(color);
    }

    @Test
    public void givenArg$setColorTheme_whenQuery_thenShouldCallNativeApiWithArg() {
        // given
        PowerMockito.mockStatic(Instabug.class);
        String colorTheme = "dark";
        JSONArray args = new JSONArray();
        args.put(colorTheme);

        // when
        cordovaModule.execute("setColorTheme", args, callbackContext);

        // then
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        Instabug.setColorTheme(InstabugColorTheme.InstabugColorThemeDark);
    }

}
