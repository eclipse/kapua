/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.converter;

import com.codahale.metrics.Counter;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.CamelKapuaMessage;
import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua message converter used to convert notification messages of Kapua Package Management service.
 *
 * @since 1.0
 */
public class KapuaPackageManagementNotifyConverter extends AbstractKapuaConverter {

    public static final Logger logger = LoggerFactory.getLogger(KapuaPackageManagementNotifyConverter.class);

    private Counter metricConverterNotifyMessage;

    public KapuaPackageManagementNotifyConverter() {
        super();
        metricConverterNotifyMessage = METRICS_SERVICE.getCounter(METRIC_COMPONENT_NAME, "kapua", "kapua_message", "messages", "notify", "packages", "count");
    }

    /**
     * Convert incoming message to a Kapua notify (life cycle) message
     *
     * @param exchange
     * @param value
     * @return
     * @throws KapuaException
     */
    @Converter
    public CamelKapuaMessage<?> convertToNotify(Exchange exchange, Object value) throws KapuaException {
        metricConverterNotifyMessage.inc();
        return convertTo(exchange, value, MessageType.NOTIFY);
    }
}
