package com.bitcup.configurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * User: omar
 */
public class FileConfigReloadTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(FileConfig.class);

    private File localConfigFile;

    @BeforeMethod
    public void setUp() throws Exception {
        Context.getInstance().env = null;
        Context.getInstance().hostName = null;
        Context.getInstance().configPath = null;
        final File testConfigFile = createTestConfigFile("/temp/configurator/reload.properties");
        localConfigFile = writeToTestConfig(testConfigFile, false, "comp1.propInt=0");
    }

    @AfterMethod
    public void tearDown() throws Exception {
        deleteTestConfig(localConfigFile);
    }

    @Test(enabled = true)
    public void testReload() throws Exception {
        // configPath context
        Context.getInstance().configPath = localConfigFile.getParent();

        // load config
        FileConfig config = new FileConfig("reload.properties");
        assertNotNull(config);

        // initial value
        assertEquals(0, (int) config.getInt("comp1.propInt"));

        // this is needed to make the test work!
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));

        // change value and wait until config file in reloaded
        localConfigFile = writeToTestConfig(localConfigFile, false, "comp1.propInt=100");
        logger.info("waiting " + FileConfig.REFRESH_DELAY_IN_SECONDS + " secs while config is refreshed...");
        Thread.sleep(TimeUnit.SECONDS.toMillis(FileConfig.REFRESH_DELAY_IN_SECONDS + 1));

        assertEquals(100, (int) config.getInt("comp1.propInt"));
    }

}
