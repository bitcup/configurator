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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Singleton used to store metadata about the java process,
 * passed as JVM parameters, consisting of the following:
 * <p/>
 * env
 * hostName
 * configPath
 * <p/>
 * These properties can be used by the java process in order
 * to implement environment/host specific logic.  They are also
 * used in {@link com.bitcup.configurator.FileConfig} classes in
 * order to detect configuration at different layers.
 * <p/>
 * env and configPath are determined exclusively from JVM parameters.
 * hostName is determined first from the JVM parameter -DhostName, but
 * if missing, then from {@link java.net.InetAddress#getLocalHost()}.
 * <p/>
 * User: omar
 */
public class Context {

    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    public static final String CONFIG_PATH = "configPath";
    public static final String ENV = "env";
    public static final String HOST_NAME = "hostName";

    private static Context instance = new Context();

    // protected for ease of testing
    protected String env = null;
    protected String hostName = null;
    protected String configPath = null;

    public static Context getInstance() {
        return instance;
    }

    private Context() {
        this.configPath = readJVMParam(CONFIG_PATH);
        this.env = readJVMParam(ENV);
        this.hostName = readJVMParam(HOST_NAME);
        if (this.hostName == null) {
            this.hostName = getLocalHostName();
            if (!StringUtils.isEmpty(this.hostName)) {
                logger.info("Using inferred hostname " + this.hostName);
            }
        }
    }

    private String readJVMParam(String param) {
        String value = System.getProperty(param);
        if (!StringUtils.isEmpty(value)) {
            logger.info("Using JVM command line parameter '" + param + "' with value: " + value);
            return value;
        } else {
            logger.warn("JVM command line parameter '" + param + "' is unspecified");
            return null;
        }
    }

    public String getHostName() {
        return hostName;
    }

    public boolean hasHostName() {
        return !StringUtils.isEmpty(hostName);
    }

    public String getConfigPath() {
        return configPath;
    }

    public boolean hasConfigPath() {
        return !StringUtils.isEmpty(configPath);
    }

    public String getEnv() {
        return env;
    }

    public boolean hasEnv() {
        return !StringUtils.isEmpty(env);
    }

    private String getLocalHostName() {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("Unable to infer hostname from InetAddress.getLocalHost().getHostName()", e.getMessage());
        }
        return hostname;
    }
}
