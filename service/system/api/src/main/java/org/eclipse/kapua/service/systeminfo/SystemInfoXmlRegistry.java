/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.systeminfo;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class SystemInfoXmlRegistry {

    private final SystemInfoFactory systemInfoFactory = KapuaLocator.getInstance().getFactory(SystemInfoFactory.class);


    /**
     * Creates a new SystemInfo instance.
     *
     * @return new SystemInfo instance.
     */
    public SystemInfo newSystemInfo() {
        return systemInfoFactory.newSystemInfo();
    }
}
