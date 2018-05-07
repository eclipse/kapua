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
package org.eclipse.kapua.service.endpoint;

import org.eclipse.kapua.model.KapuaEntityCreator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Set;

/**
 * {@link EndpointInfo} creator definition.<br>
 * It is used to create a new {@link EndpointInfo}.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "endpointInfoCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = EndpointInfoXmlRegistry.class, factoryMethod = "newCreator")
public interface EndpointInfoCreator extends KapuaEntityCreator<EndpointInfo> {

    String getSchema();

    void setSchema(String schema);

    String getDns();

    void setDns(String dns);

    int getPort();

    void setPort(int port);

    boolean getSecure();

    void setSecure(boolean secure);

    Set<EndpointUsage> getUsages();

    void setUsages(Set<EndpointUsage> usages);
}
