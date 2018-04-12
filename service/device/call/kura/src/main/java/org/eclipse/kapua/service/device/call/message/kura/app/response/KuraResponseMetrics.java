/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.app.response;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMetrics;

/**
 * {@link DeviceResponseMetrics} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public enum KuraResponseMetrics implements DeviceResponseMetrics {
    /**
     * Response exit code
     */
    RESP_METRIC_EXIT_CODE("response.code"),
    /**
     * Response exception message
     */
    RESP_METRIC_EXCEPTION_MESSAGE("response.exception.message"),
    /**
     * Response exception stack trace
     */
    RESP_METRIC_EXCEPTION_STACK("response.exception.stack"),;

    private String value;

    KuraResponseMetrics(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
