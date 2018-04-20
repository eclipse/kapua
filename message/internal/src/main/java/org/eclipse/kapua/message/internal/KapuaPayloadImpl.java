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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.commons.util.Payloads;
import org.eclipse.kapua.message.KapuaPayload;

/**
 * Kapua message payload object reference implementation.
 * 
 * @since 1.0
 *
 */
public class KapuaPayloadImpl implements KapuaPayload {

    private Map<String, Object> metrics;
    private byte[] body;

    /**
     * Constructor
     */
    public KapuaPayloadImpl() {
    }

    @Override
    public Map<String, Object> getMetrics() {
        if (metrics == null) {
            metrics = new HashMap<>();
        }

        return metrics;
    }

    @Override
    public void setMetrics(Map<String, Object> metrics) {
        this.metrics = metrics;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toDisplayString() {
        return Payloads.toDisplayString(metrics);
    }
}
