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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.query.StorableField;

/**
 * This enumeration defines the fields names used in the {@link ClientInfo} Elasticsearch schema
 * 
 * @since 1.0
 *
 */
public enum ClientInfoField implements StorableField
{
    /**
     * Account name
     */
    ACCOUNT(EsSchema.CLIENT_ACCOUNT),
    /**
     * Client identifier
     */
    CLIENT_ID(EsSchema.CLIENT_ID),
    /**
     * Timestamp
     */
    TIMESTAMP(EsSchema.CLIENT_TIMESTAMP),
    /**
     * Message identifier
     */
    MESSAGE_ID(EsSchema.CLIENT_MESSAGE_ID);

    private String field;

    private ClientInfoField(String name)
    {
        this.field = name;
    }

    @Override
    public String field()
    {
        return field;
    }
}
