/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and others.
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

import java.util.Base64;

import org.apache.camel.Exchange;
import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.shiro.util.ThreadContext;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.listener.AbstractListener;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua Camel session filter used to bind/unbind Kapua session to the thread context
 *
 */
public class KapuaCamelFilter extends AbstractListener {

    private final static Logger logger = LoggerFactory.getLogger(KapuaCamelFilter.class);

    public KapuaCamelFilter() {
        super("filter");
    }

    /**
     * Bind the Kapua session retrieved from the message header (with key {@link MessageConstants#HEADER_KAPUA_SESSION}) to the current thread context.
     * 
     * @param exchange
     * @param value
     * @throws KapuaException
     */
    public void bindSession(Exchange exchange, Object value) throws KapuaException {
        ThreadContext.unbindSubject();
        if (!exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, boolean.class)) {
            try {
                // FIX #164
                KapuaSecurityUtils
                        .setSession((KapuaSession) SerializationUtils.deserialize(Base64.getDecoder().decode(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_SESSION, String.class))));
            } catch (IllegalArgumentException | SerializationException e) {
                // continue without session
                logger.debug("Cannot restore Kapua session: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Unbind the Kapua session from the current thread context.
     * 
     * @param exchange
     * @param value
     * @throws KapuaException
     */
    public void unbindSession(Exchange exchange, Object value) throws KapuaException {
        KapuaSecurityUtils.clearSession();
    }

}
