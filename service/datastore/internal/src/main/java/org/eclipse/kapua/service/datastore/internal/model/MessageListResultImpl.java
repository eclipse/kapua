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
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.internal.model.query.AbstractStorableListResult;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;

/**
 * Message query result list implementation
 * 
 * @since 1.0
 *
 */
public class MessageListResultImpl extends AbstractStorableListResult<DatastoreMessage> implements MessageListResult
{
    private static final long serialVersionUID = -3862584760563199758L;

    /**
     * Construct a message result list
     */
    public MessageListResultImpl()
    {
        super();
    }

    /**
     * Construct a message result list linking the next result list
     * 
     * @param nextKey
     */
    public MessageListResultImpl(Object nextKey)
    {
        super(nextKey);
    }

    /**
     * Construct a message result list linking the next result list and setting the total count
     * 
     * @param nextKey
     * @param totalCount
     */
    public MessageListResultImpl(Object nextKey, Long totalCount)
    {
        super(nextKey, totalCount);
    }
}
