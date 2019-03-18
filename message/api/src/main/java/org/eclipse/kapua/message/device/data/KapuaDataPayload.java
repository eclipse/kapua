/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.device.data.xml.DataMessageXmlRegistry;

import javax.xml.bind.annotation.XmlType;

/**
 * {@link KapuaDataPayload} definition.
 *
 * @since 1.0.0
 */
@XmlType(factoryClass = DataMessageXmlRegistry.class, factoryMethod = "newKapuaDataPayload")
public interface KapuaDataPayload extends KapuaPayload {

}
