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
package org.eclipse.kapua.service.device.management;

public enum ResponseProperties
{
    RESP_PROPERTY_EXCEPTION_MESSAGE("kapua.response.exception.message"),
    RESP_PROPERTY_EXCEPTION_STACK("kapua.response.exception.stack"),
    ;

    private String value;

    ResponseProperties(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
