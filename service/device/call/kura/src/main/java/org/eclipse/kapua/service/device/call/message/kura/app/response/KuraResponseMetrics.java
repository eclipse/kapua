/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
 *
 * @since 1.0.0
 */
public enum KuraResponseMetrics implements DeviceResponseMetrics {

    /**
     * Response exit code.
     *
     * @since 1.0.0
     */
    EXIT_CODE("response.code"),
    /**
     * Response exception message.
     *
     * @since 1.0.0
     */
    EXCEPTION_MESSAGE("response.exception.message"),
    /**
     * Response exception stack trace.
     *
     * @since 1.0.0
     */
    EXCEPTION_STACK("response.exception.stack"),
    ;

    /**
     * The metric name.
     *
     * @since 1.0.0
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name The metric name.
     * @since 1.0.0
     */
    KuraResponseMetrics(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return getName();
    }
}
