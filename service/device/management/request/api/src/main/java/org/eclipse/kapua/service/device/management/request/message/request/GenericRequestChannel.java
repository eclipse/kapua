/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.request.message.request;

import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.request.GenericRequestXmlRegistry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Generic {@link KapuaRequestChannel} definition.
 *
 * @since 1.0.0
 */
@XmlType(factoryClass = GenericRequestXmlRegistry.class, factoryMethod = "newRequestChannel")
public interface GenericRequestChannel extends KapuaRequestChannel {

    /**
     * Gets the resources.
     * <p>
     * To be used if {@link #getResource()} is not enough.
     *
     * @return The resources.
     * @since 1.0.0
     */
    @XmlElement(name = "resources")
    List<String> getResources();

    /**
     * Sets the resources.
     * <p>
     * To be used if {@link #setResource(String)} is not enough.
     *
     * @param resources The resources.
     * @since 1.0.0
     */
    void setResources(List<String> resources);
}
