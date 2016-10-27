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
package org.eclipse.kapua.service.device.call.kura.app;

/**
 * Response metrics properties definition.
 * 
 * @since 1.0
 *
 */
public enum ResponseMetrics
{
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
    RESP_METRIC_EXCEPTION_STACK("response.exception.stack"),
    ;

    private String value;

    ResponseMetrics(String value)
    {
        this.value = value;
    }

    /**
     * Get a value property associated to this specific enumeration key.
     * 
     * @return
     */
    public String getValue()
    {
        return value;
    }
}
