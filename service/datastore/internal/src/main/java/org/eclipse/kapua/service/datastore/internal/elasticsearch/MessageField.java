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

import org.eclipse.kapua.service.datastore.model.query.StorableField;

/**
 * This enumeration defines the fields names used in the {@link MessageField} Elasticsearch schema
 * 
 * @since 1.0
 *
 */
public enum MessageField implements StorableField
{
    /**
     * Account identifier
     */
    ACCOUNT_ID(EsSchema.MESSAGE_ACCOUNT_ID),
    /**
     * Account name
     */
    ACCOUNT(EsSchema.MESSAGE_ACCOUNT),
    /**
     * Device identifier (physical device)
     */
    DEVICE_ID(EsSchema.MESSAGE_DEVICE_ID),
    /**
     * Client identifier
     */
    CLIENT_ID(EsSchema.MESSAGE_CLIENT_ID),
    /**
     * Channel
     */
    CHANNEL(EsSchema.MESSAGE_CHANNEL),
    /**
     * Message indexing timestamp
     */
    TIMESTAMP(EsSchema.MESSAGE_TIMESTAMP),
    /**
     * Received on timestamp
     */
    RECEIVED_ON(EsSchema.MESSAGE_RECEIVED_ON),
    /**
     * Remote address
     */
    FROM_IP_ADDRESS(EsSchema.MESSAGE_IP_ADDRESS),
    /**
     * Captured on timestamp
     */
    CAPTURED_ON(EsSchema.MESSAGE_CAPTURED_ON),
    /**
     * Sent on timestamp
     */
    SENT_ON(EsSchema.MESSAGE_SENT_ON),
    /**
     * Device position
     */
    POSITION(EsSchema.MESSAGE_POSITION),
    /**
     * Device position - location
     */
    POSITION_LOCATION(EsSchema.MESSAGE_POS_LOCATION_FULL),
    /**
     * Device position - altitude
     */
    POSITION_ALT(EsSchema.MESSAGE_POS_ALT_FULL),
    /**
     * Device position - precision
     */
    POSITION_PRECISION(EsSchema.MESSAGE_POS_PRECISION_FULL),
    /**
     * Device position - heading
     */
    POSITION_HEADING(EsSchema.MESSAGE_POS_HEADING_FULL),
    /**
     * Device position - speed
     */
    POSITION_POS_SPEED(EsSchema.MESSAGE_POS_SPEED_FULL),
    /**
     * Device position - timestamp
     */
    POSITION_TIMESTAMP(EsSchema.MESSAGE_POS_TIMESTAMP_FULL),
    /**
     * Device position - satellites
     */
    POSITION_SATELLITES(EsSchema.MESSAGE_POS_SATELLITES_FULL),
    /**
     * Device position - status
     */
    POSITION_STATUS(EsSchema.MESSAGE_POS_STATUS_FULL),
    /**
     * Message metrics
     */
    METRICS(EsSchema.MESSAGE_METRICS),
    /**
     * Message body
     */
    BODY(EsSchema.MESSAGE_BODY);

    private String field;

    private MessageField(String name)
    {
        this.field = name;
    }

    @Override
    public String field()
    {
        return field;
    }
}
