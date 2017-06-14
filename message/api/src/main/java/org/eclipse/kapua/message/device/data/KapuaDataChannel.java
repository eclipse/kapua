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
 *******************************************************************************/
package org.eclipse.kapua.message.device.data;

import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.xml.MessageXmlRegistry;

import javax.xml.bind.annotation.XmlType;

/**
 * Kapua data message channel object definition.
 * 
 * @since 1.0
 *
 */
@XmlType(factoryClass = MessageXmlRegistry.class, factoryMethod = "newKapuaDataChannel")
public interface KapuaDataChannel extends KapuaChannel {
}
