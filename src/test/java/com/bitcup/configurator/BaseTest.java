package com.bitcup.configurator;

import org.apache.commons.io.FileUtils;

import java.io.File;

import static org.testng.AssertJUnit.fail;

/**
 * User: omar
 */
public abstract class BaseTest {

    protected File createTestConfigFile(String path) {
        String home = System.getenv("HOME");
        if (home == null) {
            fail("Environment variable $HOME not setup");
        }
        return new File(home + path);
    }

    protected File writeToTestConfig(File file, boolean append, String... data) throws Exception {
        for (String line : data) {
            FileUtils.write(file, line, append);
        }
        return file;
    }

    protected void deleteTestConfig(File file) throws Exception {
        if (file != null) {
            FileUtils.forceDelete(file);
        }
    }

}
