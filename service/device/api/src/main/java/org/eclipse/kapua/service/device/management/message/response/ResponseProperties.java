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
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.message.response;

/**
 * Kapua response message properties.
 *
 * @since 1.0
 */
public enum ResponseProperties {

    /**
     * Exception message (if present)
     */
    RESP_PROPERTY_EXCEPTION_MESSAGE("kapua.response.exception.message"),
    /**
     * Exception stack (if present)
     */
    RESP_PROPERTY_EXCEPTION_STACK("kapua.response.exception.stack"),
    ;

    private String value;

    /**
     * Constructor
     *
     * @param value
     */
    ResponseProperties(String value) {
        this.value = value;
    }

    /**
     * Get the property value
     *
     * @return
     */
    public String getValue() {
        return value;
    }
}
