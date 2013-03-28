package com.bitcup.configurator;

import org.apache.commons.configuration.ConfigurationMap;

import java.util.Map;

/**
 * Exposes properties in {@link com.bitcup.configurator.FileConfig} as a
 * {@link java.util.Map}.  This is useful in a servlet context (web apps) where the map
 * from an instance of this object can be placed as an attribute in the servlet context
 * and thus made available inside of JSP via JSTL/EL notation ${map[key]}.  This can be
 * achieved using a {@link javax.servlet.ServletContextListener} and web.xml.
 * <p/>
 * User: omar
 */
public class FileConfigMap extends FileConfig {

    private Map map;

    /**
     * Loads configuration properties file at the local, host, env and base levels
     * and exposes them as a {@link java.util.Map}.
     *
     * @param filename name of the properties file to load
     */
    public FileConfigMap(String filename) {
        super(filename);
        map = new ConfigurationMap(this.configuration);
    }

    public Map getMap() {
        return map;
    }
}
