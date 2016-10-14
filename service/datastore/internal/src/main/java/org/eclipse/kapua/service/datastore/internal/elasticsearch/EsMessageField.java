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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.eclipse.kapua.service.datastore.model.query.StorableField;

public enum EsMessageField implements StorableField
{
    ID(EsSchema.ASSET_MESSAGE_ID),
    ACCOUNT(EsSchema.MESSAGE_ACCOUNT),
    ASSET(EsSchema.MESSAGE_AS_NAME),
    SEMANTIC_TOPIC(EsSchema.MESSAGE_SEM_TOPIC),
    TIMESTAMP(EsSchema.MESSAGE_TIMESTAMP),
    RECEIVED_ON(EsSchema.MESSAGE_RECEIVED_ON),
    FROM_IP_ADDRESS(EsSchema.MESSAGE_IP_ADDRESS),
    COLLECTED_ON(EsSchema.MESSAGE_COLLECTED_ON),
    POSITION(EsSchema.MESSAGE_POS),
    POSITION_LOCATION(EsSchema.MESSAGE_POS_LOCATION_FULL),
    POSITION_ALT(EsSchema.MESSAGE_POS_ALT_FULL),
    POSITION_PRECISION(EsSchema.MESSAGE_POS_PRECISION_FULL),
    POSITION_HEADING(EsSchema.MESSAGE_POS_HEADING_FULL),
    POSITION_POS_SPEED(EsSchema.MESSAGE_POS_SPEED_FULL),
    POSITION_TIMESTAMP(EsSchema.MESSAGE_POS_TIMESTAMP_FULL),
    POSITION_SATELLITES(EsSchema.MESSAGE_POS_SATELLITES_FULL),
    POSITION_STATUS(EsSchema.MESSAGE_POS_STATUS_FULL),
    //METRICS(EsSchema.MESSAGE_MTR),
    BODY(EsSchema.MESSAGE_BODY);
    
    private String field;
    
    private EsMessageField(String name)
    {
        this.field= name;
    }
    
    @Override
    public String field()
    {
        return field;
    }
}
