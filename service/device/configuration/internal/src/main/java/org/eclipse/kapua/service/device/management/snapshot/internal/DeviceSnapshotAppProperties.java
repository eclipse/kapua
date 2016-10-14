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
package org.eclipse.kapua.service.device.management.snapshot.internal;

import org.eclipse.kapua.service.device.management.KapuaAppProperties;

public enum DeviceSnapshotAppProperties implements KapuaAppProperties
{
	APP_NAME("SNAPSHOT"),
    APP_VERSION("1.0.0"),
    ;

    private String value;

    DeviceSnapshotAppProperties(String value)
    {
        this.value = value;
    }

    @Override
    public String getValue()
    {
        return value;
    }
	
}
