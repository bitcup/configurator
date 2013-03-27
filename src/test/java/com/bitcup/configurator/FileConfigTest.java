package com.bitcup.configurator;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.*;

/**
 * User: omar
 */
public class FileConfigTest {

    private File f;

    @BeforeMethod
    public void setUp() throws Exception {
        Context.getInstance().env = null;
        Context.getInstance().hostName = null;
        Context.getInstance().configPath = null;
        f = createTestConfig();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        deleteTestConfig(f);
    }

    @Test(enabled = true)
    @SuppressWarnings("unchecked")
    public void testBase() throws Exception {
        // no context
        assertNull(Context.getInstance().getHostName());
        assertNull(Context.getInstance().getConfigPath());
        assertNull(Context.getInstance().getEnv());

        // load config
        FileConfig config = new FileConfig("project.properties");
        assertNotNull(config);

        // component 1
        assertEquals(123, (int) config.getInt("comp1.propInt"));
        assertEquals(true, (boolean) config.getBoolean("comp1.sub.propBool"));
        // component 2
        List<String> l = new ArrayList<String>();
        l.add("item1");
        l.add("item2");
        assertEquals(l, config.getList("comp2.propList"));

        // default values for non-existing prop
        assertEquals(true, (boolean) config.getBoolean("propX", true));
        assertEquals(222, (int) config.getInt("propX", 222));
        assertEquals(l, config.getList("propX", (List) l));
    }

    @Test(enabled = true)
    @SuppressWarnings("unchecked")
    public void testUnmatchedContext() throws Exception {
        // unmatched context ==> props same as base
        Context.getInstance().env = "xxx";
        assertNull(Context.getInstance().getConfigPath());
        assertNull(Context.getInstance().getHostName());

        // load config
        FileConfig config = new FileConfig("project.properties");
        assertNotNull(config);

        // component 1
        assertEquals(123, (int) config.getInt("comp1.propInt"));
        assertEquals(true, (boolean) config.getBoolean("comp1.sub.propBool"));
        // component 2
        List<String> l = new ArrayList<String>();
        l.add("item1");
        l.add("item2");
        assertEquals(l, config.getList("comp2.propList"));
    }

    @Test(enabled = true)
    @SuppressWarnings("unchecked")
    public void testHostnameContext() throws Exception {
        // localhost context
        Context.getInstance().hostName = "localhost";
        assertNull(Context.getInstance().getConfigPath());
        assertNull(Context.getInstance().getEnv());

        // load config
        FileConfig config = new FileConfig("project.properties");
        assertNotNull(config);

        // component 1
        assertEquals(123, (int) config.getInt("comp1.propInt"));
        assertEquals(true, (boolean) config.getBoolean("comp1.sub.propBool"));
        // component 2
        List<String> l = new ArrayList<String>();
        l.add("item3");
        assertEquals(l, config.getList("comp2.propList"));
    }

    @Test(enabled = true)
    @SuppressWarnings("unchecked")
    public void testEnvContext() throws Exception {
        // dev context
        Context.getInstance().env = "dev";
        assertNull(Context.getInstance().getConfigPath());
        assertNull(Context.getInstance().getHostName());

        // load config
        FileConfig config = new FileConfig("project.properties");
        assertNotNull(config);

        // component 1
        assertEquals(456, (int) config.getInt("comp1.propInt"));
        assertEquals(true, (boolean) config.getBoolean("comp1.sub.propBool"));
        // component 2
        List<String> l = new ArrayList<String>();
        l.add("item1");
        l.add("item2");
        assertEquals(l, config.getList("comp2.propList"));
    }

    @Test(enabled = true)
    @SuppressWarnings("unchecked")
    public void testHostnameAndEnvContext() throws Exception {
        // localhost and dev context
        Context.getInstance().hostName = "localhost";
        Context.getInstance().env = "dev";
        assertNull(Context.getInstance().getConfigPath());

        // load config
        FileConfig config = new FileConfig("project.properties");
        assertNotNull(config);

        // component 1
        assertEquals(456, (int) config.getInt("comp1.propInt"));
        assertEquals(true, (boolean) config.getBoolean("comp1.sub.propBool"));
        // component 2
        List<String> l = new ArrayList<String>();
        l.add("item3");
        assertEquals(l, config.getList("comp2.propList"));
    }

    @Test(enabled = true)
    @SuppressWarnings("unchecked")
    public void testConfigPathAndHostnameAndEnvContext() throws Exception {
        // localhost, dev and configPath context
        Context.getInstance().hostName = "localhost";
        Context.getInstance().env = "dev";
        Context.getInstance().configPath = f.getParent();

        // load config
        FileConfig config = new FileConfig("project.properties");
        assertNotNull(config);

        // component 1
        assertEquals(0, (int) config.getInt("comp1.propInt"));
        assertEquals(true, (boolean) config.getBoolean("comp1.sub.propBool"));
        // component 2
        List<String> l = new ArrayList<String>();
        l.add("item3");
        assertEquals(l, config.getList("comp2.propList"));
    }

    private File createTestConfig() throws Exception {
        File file = new File("/Users/obaba/temp/configurator/project.properties");
        FileUtils.write(file, "comp1.propInt=0");
        return file;
    }

    private void deleteTestConfig(File file) throws Exception {
        FileUtils.forceDelete(file);
    }
}
