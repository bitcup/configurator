/*
 * Copyright (c) 2013 bitcup
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
     * <p/>
     * Configuration is refreshed at the default refresh delay value of
     * {@value #DEFAULT_REFRESH_DELAY_IN_SECONDS}.
     *
     * @param filename name of the properties file to load
     */
    public FileConfigMap(String filename) {
        super(filename);
        this.map = new ConfigurationMap(this.configuration);
    }

    /**
     * Loads configuration properties file at the local, host, env and base levels
     * and exposes them as a {@link java.util.Map}.  Sets the refresh delay on the
     * {@link org.apache.commons.configuration.reloading.FileChangedReloadingStrategy}.
     *
     * @param filename         name of the properties file to load
     * @param refreshDelaySecs refresh delay in seconds
     */
    public FileConfigMap(String filename, int refreshDelaySecs) {
        super(filename, refreshDelaySecs);
        this.map = new ConfigurationMap(this.configuration);
    }

    public Map getMap() {
        return map;
    }
}
