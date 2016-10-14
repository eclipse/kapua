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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model.query;

import java.util.ArrayList;

import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.StorableListResult;

@SuppressWarnings("serial")
public class AbstractStorableListResult<E extends Storable> extends ArrayList<E> implements StorableListResult<E>
{
    private Object  nextKey;
    private Integer totalCount;

    public AbstractStorableListResult()
    {
        nextKey = null;
        totalCount = null;
    }

    public AbstractStorableListResult(Object nextKey)
    {
        this.nextKey = nextKey;
        this.totalCount = null;
    }

    public AbstractStorableListResult(Object nextKeyOffset, Integer totalCount)
    {
        this(nextKeyOffset);
        this.totalCount = totalCount;
    }

    public Object getNextKey()
    {
        return nextKey;
    }

    public Integer getTotalCount()
    {
        return totalCount;
    }
}
