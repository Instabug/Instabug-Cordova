package com.instabug.example;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.instabug.cordova.plugin.IBGPlugin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import org.apache.cordova.CallbackContext;

public class IBGPluginTests {
    private MockedStatic<Instabug> mockInstabug;
    private IBGPlugin mockPlugin;
    private CallbackContext mockContext;

    @Before
    public void setup() {
        mockInstabug = mockStatic(Instabug.class);
        mockPlugin = spy(new IBGPlugin());
        mockContext = mock(CallbackContext.class);
    }

    @After
    public void teardown() {
        mockInstabug.close();
    }

    @Test
    public void testShow() {
        mockPlugin.show(mockContext);
        verify(mockContext).success();
    }
}