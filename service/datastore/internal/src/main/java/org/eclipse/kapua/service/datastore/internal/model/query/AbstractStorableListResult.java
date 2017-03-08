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
package org.eclipse.kapua.service.datastore.internal.model.query;

import java.util.ArrayList;

import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.StorableListResult;

/**
 * Storable object list implementation
 * 
 * @since 1.0
 *
 * @param <E>
 */
@SuppressWarnings("serial")
public class AbstractStorableListResult<E extends Storable> extends ArrayList<E> implements StorableListResult<E>
{
    private Object nextKey;
    private Long   totalCount;

    /**
     * Default constructor
     */
    public AbstractStorableListResult()
    {
        nextKey = null;
        totalCount = null;
    }

    /**
     * Construct a result list linking the next result list
     * 
     * @param nextKey
     */
    public AbstractStorableListResult(Object nextKey)
    {
        this.nextKey = nextKey;
        this.totalCount = null;
    }

    /**
     * Construct a result list linking the next result list and setting the total count
     * 
     * @param nextKeyOffset
     * @param totalCount
     */
    public AbstractStorableListResult(Object nextKeyOffset, Long totalCount)
    {
        this(nextKeyOffset);
        this.totalCount = totalCount;
    }

    @Override
    public Object getNextKey()
    {
        return nextKey;
    }

    @Override
    public Long getTotalCount()
    {
        return totalCount;
    }
}
