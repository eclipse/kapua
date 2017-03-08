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
 * Query predicate definition for matching field value
 * 
 * @since 1.0
 *
 */
public interface TermPredicate extends StorablePredicate
{

    /**
     * Return the field
     * 
     * @return
     */
    public StorableField getField();

    /**
     * Return the value
     * 
     * @return
     */
    public Object getValue();

    /**
     * Return the value (typed)
     * 
     * @param clazz
     * @return
     */
    public <V> V getValue(Class<V> clazz);
}
