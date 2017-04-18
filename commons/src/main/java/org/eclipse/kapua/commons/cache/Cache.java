/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.cache;

/**
 * Kapua cache definition
 * 
 * @param <K> keys type
 * @param <V> values type
 * 
 * @since 1.0
 */
public interface Cache<K, V>
{
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
}
