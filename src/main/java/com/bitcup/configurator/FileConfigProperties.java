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

    public FileConfigProperties(String filename) {
        fileConfig = new FileConfig(filename);
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
