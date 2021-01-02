/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
