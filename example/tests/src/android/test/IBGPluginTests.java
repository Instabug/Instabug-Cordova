package com.instabug.example;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.instabug.cordova.plugin.IBGPlugin;
import com.instabug.library.Instabug;

import org.apache.cordova.CallbackContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class IBGPluginTests {
    private MockedStatic<Instabug> mockInstabug;
    private final IBGPlugin mockPlugin = spy(new IBGPlugin());
    private final CallbackContext mockContext = mock(CallbackContext.class);

    @Before
    public void setup() {
        mockInstabug = mockStatic(Instabug.class);
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