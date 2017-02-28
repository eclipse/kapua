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
package org.eclipse.kapua.service.device.registry.connection;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * Device connection xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class DeviceConnectionXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceConnectionFactory factory = locator.getFactory(DeviceConnectionFactory.class);

    /**
     * Creates a new connection summary
     *
     * @return
     */
    public DeviceConnectionSummary newConnectionSummary() {
        return factory.newConnectionSummary();
    }
}
