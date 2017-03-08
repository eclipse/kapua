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

import org.eclipse.kapua.service.datastore.internal.AbstractStorableQuery;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;

/**
 * Channel information query implementation
 * 
 * @since 1.0
 *
 */
public class ChannelInfoQueryImpl extends AbstractStorableQuery<ChannelInfo> implements ChannelInfoQuery
{

    /**
     * Create and keep a copy of the given query
     * 
     * @param query
     */
    public void copy(ChannelInfoQuery query)
    {
        super.copy(query);
        // Add copy for local members
    }
}
