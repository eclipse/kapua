/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.cache;

/**
 * Kapua cache definition
 *
 * @param <K>
 *            keys type
 * @param <V>
 *            values type
 *
 * @since 1.0
 */
public interface Cache<K, V> {

    /**
     * Return the metric namespace
     *
     * @return
     */
    public String getNamespace();

    /**
     * Set the metric namespace
     *
     * @param namespace
     */
    public void setNamespace(String namespace);

    /**
     * Return the cache value for the given key
     *
     * @param k
     * @return
     */
    public V get(K k);

    /**
     * Set the cache value for the given key
     *
     * @param k
     * @param v
     */
    public void put(K k, V v);

    /**
     * Remove the cache value for the given key.<BR>
     * No exception will be thrown if the value is not present
     *
     * @param k
     */
    public void remove(K k);

    /**
     * Clear the cache
     */
    public void invalidateAll();

}
