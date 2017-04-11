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
 * Asset metrics properties definition.
 * 
 * @since 1.0
 *
 */
public enum AssetMetrics
{

    /**
     * Application identifier
     */
    APP_ID("ASSET"),
    /**
     * Application version
     */
    APP_VERSION("V1"),
    ;

    private String value;

    AssetMetrics(String value)
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
