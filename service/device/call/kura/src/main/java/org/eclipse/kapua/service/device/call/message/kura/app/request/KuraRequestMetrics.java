/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.app.request;

import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMetrics;

/**
 * {@link DeviceRequestMetrics }{@link org.eclipse.kapua.service.device.call.kura.Kura} implementation
 */
public enum KuraRequestMetrics implements DeviceRequestMetrics {

    /**
     * Request identifier
     */
    REQUEST_ID("request.id"),
    /**
     * Requester client identifier
     */
    REQUESTER_CLIENT_ID("requester.client.id"),
    ;

    private String value;

    KuraRequestMetrics(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
