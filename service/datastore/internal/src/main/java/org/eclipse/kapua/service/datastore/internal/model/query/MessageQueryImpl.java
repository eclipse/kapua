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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.AbstractStorableQuery;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;

/**
 * Message query implementation
 * 
 * @since 1.0.0
 *
 */
public class MessageQueryImpl extends AbstractStorableQuery<DatastoreMessage> implements MessageQuery
{
    /**
     * Constructor.
     * 
     * @param scopeId
     * 
     * @since 1.0.0
     */
    public MessageQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
    
    /**
     * Create and keep a copy of the given query
     * 
     * @param query
     * 
     * @since 1.0.0
     */
    public void copy(MessageQuery query)
    {
        super.copy(query);
    }
}
