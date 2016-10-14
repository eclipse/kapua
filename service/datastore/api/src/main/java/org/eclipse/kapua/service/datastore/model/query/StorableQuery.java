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
package org.eclipse.kapua.service.datastore.model.query;

import org.eclipse.kapua.service.datastore.model.Storable;

public interface StorableQuery<S extends Storable>
{
    public StorablePredicate getPredicate();
    
    public void setPredicate(StorablePredicate predicate);
    
    public int getOffset();

    public void setOffset(int offset);

    public int getLimit();

    public void setLimit(int limit);

    public boolean isAskTotalCount();

    public void setAskTotalCount(boolean askTotalCount);
    
    public MessageFetchStyle getFetchStyle();

    public void setFetchStyle(MessageFetchStyle fetchStyle);

    public SortDirection getSort();

    public void setSort(SortDirection sortStyle);
    
    public void copy(StorableQuery<S> query);
}
