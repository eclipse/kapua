/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.request.message.request;

import org.eclipse.kapua.service.device.management.request.GenericRequestXmlRegistry;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"resources"}, factoryClass = GenericRequestXmlRegistry.class, factoryMethod = "newRequestChannel")
public interface GenericRequestChannel extends KapuaRequestChannel {

    /**
     * Get the resources
     *
     * @return resources
     */
    String[] getResources();

    /**
     * Set the resources
     *
     * @param resources
     */
    void setResources(String[] resources);
}
