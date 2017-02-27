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
package org.eclipse.kapua.service.device.management.bundle.internal;

import org.eclipse.kapua.service.device.management.KapuaAppProperties;

/**
 * Device bundle properties definition.
 *
 * @since 1.0
 *
 */
public enum DeviceBundleAppProperties implements KapuaAppProperties {
    /**
     * Application name
     */
    APP_NAME("BUNDLE"),
    /**
     * Version
     */
    APP_VERSION("1.0.0"),
    ;

    private String value;

    DeviceBundleAppProperties(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
