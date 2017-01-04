/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model.query;

/**
 * Query predicate for matching range values implementation
 * 
 * @since 1.0
 *
 */
public interface RangePredicate extends StorablePredicate
{
    /**
     * Get the field to be compared
     * 
     * @return
     */
    public StorableField getField();

    /**
     * Return the desired minimum value
     * 
     * @return
     */
    public Object getMinValue();

    /**
     * Get the minimum value as {@link Comparable} (type)
     * 
     * @param clazz
     * @return
     */
    public <V extends Comparable<V>> V getMinValue(Class<V> clazz);

    /**
     * Get the maximum value
     * 
     * @return
     */
    public Object getMaxValue();

    /**
     * Get the maximum value as {@link Comparable} (typed)
     * 
     * @param clazz
     * @return
     */
    public <V extends Comparable<V>> V getMaxValue(Class<V> clazz);
}
