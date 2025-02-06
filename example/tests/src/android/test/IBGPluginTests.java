package com.instabug.example;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.instabug.cordova.plugin.IBGPlugin;
import com.instabug.library.Instabug;

import org.apache.cordova.CallbackContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class IBGPluginTests {
    private MockedStatic<Instabug> mockInstabug;
    private IBGPlugin mockPlugin;
    private CallbackContext mockContext;

    @Before
    public void setup() {
        try {
            mockInstabug = mockStatic(Instabug.class);
            mockPlugin = spy(new IBGPlugin());
            mockContext = mock(CallbackContext.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void teardown() {
        try {
            if (mockInstabug != null) {
                mockInstabug.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShow() {
        try {
            mockPlugin.show(mockContext);
            verify(mockContext).success();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
