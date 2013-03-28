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

import org.apache.commons.lang.NotImplementedException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Wraps a {@link com.bitcup.configurator.FileConfig} instance and exposes its
 * properties as a {@link java.util.Properties} instance. This is useful in the
 * context of Spring's {@link org.springframework.beans.factory.config.PropertyPlaceholderConfigurer}
 * whereby Spring configuration can be parametrized via placeholders that are
 * resolved from a FileConfig instance.
 * <p/>
 * User: omar
 */
public class FileConfigProperties extends Properties {

    private FileConfig fileConfig;

    /**
     * Loads configuration properties file at the local, host, env and base levels
     * and exposes them as a {@link java.util.Map}.
     * <p/>
     * Configuration is refreshed at the default refresh delay value of
     * {@value com.bitcup.configurator.FileConfig#DEFAULT_REFRESH_DELAY_IN_SECONDS}.
     *
     * @param filename name of the properties file to load
     */
    public FileConfigProperties(String filename) {
        fileConfig = new FileConfig(filename);
    }

    /**
     * Loads configuration properties file at the local, host, env and base levels
     * and exposes them as a {@link java.util.Map}.  Sets the refresh delay on the
     * {@link org.apache.commons.configuration.reloading.FileChangedReloadingStrategy}.
     *
     * @param filename         name of the properties file to load
     * @param refreshDelaySecs refresh delay in seconds
     */
    public FileConfigProperties(String filename, int refreshDelaySecs) {
        fileConfig = new FileConfig(filename, refreshDelaySecs);
    }

    @Override
    public String getProperty(String key) {
        return fileConfig.getString(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return fileConfig.getString(key, defaultValue);
    }

    // -------------------------------------------------------------------------------
    // NOTE: all methods below are needed in order for the class to act correctly
    // as a Properties file, which is needed to use with PropertyPlaceholderConfigurer
    // -------------------------------------------------------------------------------

    @Override
    public boolean isEmpty() {
        return this.fileConfig.configuration.isEmpty();
    }

    @Override
    public String toString() {
        return this.fileConfig.configuration.toString();
    }

    @Override
    public void list(final PrintStream out) {
        throw new NotImplementedException();
    }

    @Override
    public void list(final PrintWriter out) {
        throw new NotImplementedException();
    }

    @Override
    public void load(final InputStream in) {
        throw new NotImplementedException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration propertyNames() {
        return getHashTable().keys();
    }

    /**
     * Note that {@link Properties} has a different expectation of the return
     * value than the corresponding method in commons config.
     */
    @Override
    public Object setProperty(final String k, final String v) {
        this.fileConfig.configuration.setProperty(k, v);
        return null;
    }

    @Override
    public void store(final OutputStream out, final String header) {
        throw new NotImplementedException();
    }

    @Override
    public Object get(final Object o) {
        return this.fileConfig.configuration.getString((String) o);
    }

    @Override
    public void clear() {
        throw new NotImplementedException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(final Object key) {
        return getHashTable().contains(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(final Object key) {
        return getHashTable().containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsValue(final Object key) {
        return getHashTable().contains(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration elements() {
        return getHashTable().elements();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set entrySet() {
        return getHashTable().entrySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Enumeration keys() {
        return getHashTable().keys();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set keySet() {
        return getHashTable().keySet();
    }

    @Override
    public Object put(final Object key, final Object value) {
        throw new NotImplementedException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void putAll(final Map t) {
        throw new NotImplementedException();
    }

    @Override
    public Object remove(final Object o) {
        throw new NotImplementedException();
    }

    @Override
    public int size() {
        Iterator<String> keysIterator = getKeysIterator();
        int size = 0;
        for (; keysIterator.hasNext(); ++size) {
            keysIterator.next();
        }
        return size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection values() {
        throw new NotImplementedException();
    }

    private Iterator<String> getKeysIterator() {
        return this.fileConfig.configuration.getKeys();
    }

    private Hashtable<String, String> getHashTable() {
        Hashtable<String, String> ht = new Hashtable<String, String>();
        Iterator<String> keysIterator = getKeysIterator();
        for (; keysIterator.hasNext(); ) {
            String key = keysIterator.next();
            ht.put(key, this.fileConfig.getString(key));
        }
        return ht;
    }
}
