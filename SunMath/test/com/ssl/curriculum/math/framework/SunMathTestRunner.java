package com.ssl.curriculum.math.framework;

import com.xtremelabs.robolectric.RobolectricConfig;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.runners.model.InitializationError;

import java.io.File;

public class SunMathTestRunner extends RobolectricTestRunner {
    public SunMathTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass, new RobolectricConfig(new File("AndroidManifest.xml"), new File("res")));
    }
}
