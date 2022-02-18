/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service.internal.cache.dummy;

import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Dummy Cache implementation, returns always null.
 *
 * @param <K>
 * @param <V>
 */
public class Cache<K, V> implements javax.cache.Cache<K, V> {

    public Cache() {
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public Map getAll(Set keys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void loadAll(Set keys, boolean replaceExistingValues, CompletionListener completionListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void put(Object key, Object value) {
    }

    @Override
    public Object getAndPut(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean putIfAbsent(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object key) {
        return true;
    }

    @Override
    public boolean remove(Object key, Object oldValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAndRemove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(Object key, Object oldValue, Object newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAndReplace(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAll(Set keys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CacheManager getCacheManager() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isClosed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration cacheEntryListenerConfiguration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration cacheEntryListenerConfiguration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object unwrap(Class clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map invokeAll(Set keys, EntryProcessor entryProcessor, Object... arguments) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object invoke(Object key, EntryProcessor entryProcessor, Object... arguments) throws EntryProcessorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Configuration getConfiguration(Class clazz) {
        throw new UnsupportedOperationException();
    }

}
