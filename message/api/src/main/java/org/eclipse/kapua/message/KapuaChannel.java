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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message;

import org.eclipse.kapua.message.xml.MessageXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Kapua message channel object definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "channel")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { //
        "semanticParts" //
}, factoryClass = MessageXmlRegistry.class, factoryMethod = "newKapuaChannel") //
public interface KapuaChannel extends Channel {

    /**
     * Get the channel destination semantic part
     *
     * @return
     */
    public List<String> getSemanticParts();

    /**
     * Set the channel destination semantic part
     *
     * @param semanticParts
     */
    public void setSemanticParts(List<String> semanticParts);

    public default String toPathString() {
        return String.join("/", getSemanticParts());
    }
}
