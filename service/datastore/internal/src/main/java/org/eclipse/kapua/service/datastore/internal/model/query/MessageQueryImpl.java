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
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;

/**
 * Message query implementation
 * 
 * @since 1.0.0
 *
 */
public class MessageQueryImpl extends AbstractStorableQuery<DatastoreMessage> implements MessageQuery {

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

    @Override
    public String[] getIncludes(StorableFetchStyle fetchStyle) {
        // Fetch mode
        String[] includeSource = null;
        switch (fetchStyle) {
        case FIELDS:
            includeSource = getFields();
            break;
        case SOURCE_SELECT:
            includeSource = new String[] { MessageSchema.MESSAGE_CAPTURED_ON, MessageSchema.MESSAGE_POSITION + ".*", MessageSchema.MESSAGE_METRICS + ".*" };
            break;
        case SOURCE_FULL:
            includeSource = new String[] { "*" };
        }
        return includeSource;
    }

    @Override
    public String[] getExcludes(StorableFetchStyle fetchStyle) {
        // Fetch mode
        String[] excludeSource = null;
        switch (fetchStyle) {
        case FIELDS:
            excludeSource = new String[] { "" };
            break;
        case SOURCE_SELECT:
            excludeSource = new String[] { MessageSchema.MESSAGE_BODY };
            break;
        case SOURCE_FULL:
            excludeSource = new String[] { "" };
        }
        return excludeSource;
    }

    @Override
    public String[] getFields() {
        return new String[] {
                MessageField.MESSAGE_ID.field(),
                MessageField.SCOPE_ID.field(),
                MessageField.DEVICE_ID.field(),
                MessageField.CLIENT_ID.field(),
                MessageField.CHANNEL.field(),
                MessageField.TIMESTAMP.field() };
    }

}
