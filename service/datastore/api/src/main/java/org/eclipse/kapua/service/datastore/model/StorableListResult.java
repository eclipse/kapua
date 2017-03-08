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
package org.eclipse.kapua.service.datastore.model;

import java.util.List;

/**
 * Storable object list definition
 * 
 * @since 1.0
 *
 * @param <E>
 */
public interface StorableListResult<E extends Storable> extends List<E>
{

    /**
     * Get the next key.<br>
     * If a limit is set into the query parameters (limit) and the messages count matching the query is higher than the limit, so the next key is the key of the first next object not included in the
     * result set.
     * 
     * @return
     */
    public Object getNextKey();

    /**
     * Get the total count.<br>
     * The total count may be higher that the result set since the extracted result set can be limited by the query (limit) parameter
     * 
     * @return
     */
    public Long getTotalCount();
}
