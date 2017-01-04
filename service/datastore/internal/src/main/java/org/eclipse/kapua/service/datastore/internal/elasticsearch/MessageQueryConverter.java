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

import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;

/**
 * Message query converter.<br>
 * This object adds the specific message included and excluded fields definition to the abstract query converter.
 * 
 * @since 1.0
 *
 */
public class MessageQueryConverter extends AbstractStorableQueryConverter<DatastoreMessage, MessageQuery>
{
    @Override
    protected String[] getIncludes(StorableFetchStyle fetchStyle)
    {

        // Fetch mode
        String[] includeSource = null;

        switch (fetchStyle) {
            case FIELDS:
                includeSource = new String[] { "" };
                break;
            case SOURCE_SELECT:
                includeSource = new String[] { EsSchema.MESSAGE_CAPTURED_ON, EsSchema.MESSAGE_POSITION + ".*", EsSchema.MESSAGE_METRICS + ".*" };
                break;
            case SOURCE_FULL:
                includeSource = new String[] { "*" };
        }

        return includeSource;
    }

    @Override
    protected String[] getExcludes(StorableFetchStyle fetchStyle)
    {

        // Fetch mode
        String[] excludeSource = null;

        switch (fetchStyle) {
            case FIELDS:
                excludeSource = new String[] { "*" };
                break;
            case SOURCE_SELECT:
                excludeSource = new String[] { EsSchema.MESSAGE_BODY };
                break;
            case SOURCE_FULL:
                excludeSource = new String[] { "" };
        }

        return excludeSource;
    }

    @Override
    protected String[] getFields()
    {
        return new String[] { MessageField.ACCOUNT_ID.field(),
                              MessageField.ACCOUNT.field(),
                              MessageField.DEVICE_ID.field(),
                              MessageField.CLIENT_ID.field(),
                              MessageField.CHANNEL.field(),
                              MessageField.TIMESTAMP.field() };
    }
}
