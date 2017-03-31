/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.devicegroup;


import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * DeviceGroup xml factory class
 */
@XmlRegistry
public class DeviceGroupXmlRegistry {
    
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceGroupFactory factory = locator.getFactory(DeviceGroupFactory.class);
    
    public DeviceGroup newDevGroup(){
        return factory.newDevGroup();
    }
    
    public DeviceGroupCreator newDevGroupCreator(){
        return factory.newCreator(null, null);
    }
    
    public DevGroupListResult newDevGroupListResult(){
        return factory.newDevGroupListResult();
    }

}
