package com.bitcup.configurator;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Loads a refreshable {@link org.apache.commons.configuration.CompositeConfiguration}
 * from several properties files specified via a common name.  The properties files
 * represent different layers of configuration meant to provide flexibility, for example,
 * enabling overrides of a property values for different environments and or hosts.
 * <p/>
 * The layers consist of the following order:
 * <p/>
 * 1) local:    loads filename relative to a user-specified configuration path
 * 2) host:     loads <host>.filename from the classpath
 * 3) env:      loads <env>.filename from the classpath
 * 4) base:     loads filename from the classpath
 * <p/>
 * Based on the order above, properties in 'dev.app.properties' configuration file would
 * override the same properties in 'app.properties', assuming both files exist on the
 * classpath.
 * <p/>
 * Host, env and local config path are provided by {@link com.bitcup.configurator.Context}.
 * For example, if {@link com.bitcup.configurator.Context#hasConfigPath()} is '/usr/local',
 * then properties in '/usr/local/app.properties' would override those in 'app.properties'
 * on the classpath.
 * <p/>
 * User: omar
 */
public class FileConfig {

    private static final Logger logger = LoggerFactory.getLogger(FileConfig.class);

    public static final int REFRESH_DELAY_IN_SECONDS = 15;
    private static final String SEPARATOR = ".";

    protected CompositeConfiguration configuration = new CompositeConfiguration();

    /**
     * Loads configuration properties file at the local, host, env and base levels.
     *
     * @param filename name of the properties file to load
     */
    public FileConfig(String filename) {
        if (Context.getInstance().hasConfigPath()) {
            final String fn = Context.getInstance().getConfigPath() + File.separator + filename;
            URL fileUrl = ConfigurationUtils.locate(fn);
            if (fileUrl != null) {
                try {
                    PropertiesConfiguration pc = new PropertiesConfiguration(fileUrl);
                    pc.setReloadingStrategy(getReloadingStrategy());
                    configuration.addConfiguration(pc);
                    logger.info("Loaded non-classpath config file " + fn);
                } catch (ConfigurationException e) {
                    logger.warn("Config file " + fn + " not found");
                }
            } else {
                logger.warn("Config file " + fn + " not found");
            }
        }
        // hostname-prefixed filename on classpath
        if (Context.getInstance().hasHostName()) {
            final String contextFilename = Context.getInstance().getHostName() + SEPARATOR + filename;
            addToCompositeConfig(contextFilename, false);
        }
        // env-prefixed filename on classpath
        if (Context.getInstance().hasEnv()) {
            final String contextFilename = Context.getInstance().getEnv() + SEPARATOR + filename;
            addToCompositeConfig(contextFilename, false);
        }
        // filename on classpath
        addToCompositeConfig(filename,
                !Context.getInstance().hasConfigPath() &&
                        !Context.getInstance().hasHostName() &&
                        !Context.getInstance().hasEnv());
    }

    private void addToCompositeConfig(String filename, boolean logWithThrowable) {
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Attempting to load config file " + filename + " on the classpath...");
            }
            PropertiesConfiguration pc = new PropertiesConfiguration(filename);
            pc.setReloadingStrategy(getReloadingStrategy());
            configuration.addConfiguration(pc);
            logger.info("Loaded config file " + filename + " on the classpath");
        } catch (ConfigurationException e) {
            if (logWithThrowable) {
                logger.warn("Config file " + filename + " not found on classpath", e);
            } else {
                logger.warn("Config file " + filename + " not found on classpath");
            }
        }
    }

    private FileChangedReloadingStrategy getReloadingStrategy() {
        final FileChangedReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
        reloadingStrategy.setRefreshDelay(TimeUnit.SECONDS.toMillis(REFRESH_DELAY_IN_SECONDS));
        return reloadingStrategy;
    }

    public String getString(String key) {
        return configuration.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return configuration.getString(key, defaultValue);
    }

    public Boolean getBoolean(String key) {
        return configuration.getBoolean(key);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return configuration.getBoolean(key, defaultValue);
    }

    public Integer getInt(String key) {
        return configuration.getInt(key);
    }

    public Integer getInt(String key, Integer defaultValue) {
        return configuration.getInt(key, defaultValue);
    }

    public Long getLong(String key) {
        return configuration.getLong(key);
    }

    public Long getLong(String key, Long defaultValue) {
        return configuration.getLong(key, defaultValue);
    }

    public List<Object> getList(String key) {
        return configuration.getList(key);
    }

    public List<Object> getList(String key, List<Object> defaultValue) {
        return configuration.getList(key, defaultValue);
    }
}
