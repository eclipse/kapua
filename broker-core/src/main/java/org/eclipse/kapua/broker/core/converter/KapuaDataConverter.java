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
package org.eclipse.kapua.broker.core.converter;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;

/**
 * Kapua message converter used to convert data messages.
 * 
 * @since 1.0
 */
public class KapuaDataConverter extends AbstractKapuaConverter
{

    public static final Logger logger = LoggerFactory.getLogger(KapuaDataConverter.class);

    private Counter metricConverterDataMessage;

    public KapuaDataConverter()
    {
        super();
        metricConverterDataMessage = metricsService.getCounter(METRIC_COMPONENT_NAME, "kapua", "kapua_message", "messages", "data", "count");
    }

    /**
     * Convert incoming message to a Kapua data message
     * 
     * @param exchange
     * @param value
     * @return Message container that contains data message
     * @throws KapuaException if incoming message does not contain a javax.jms.BytesMessage or an error during conversion occurred
     */
    @Converter
    public CamelKapuaMessage<?> convertToData(Exchange exchange, Object value) throws KapuaException
    {
        metricConverterDataMessage.inc();
        return convertTo(exchange, value, MessageType.DATA);
    }

}
