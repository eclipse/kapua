/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message;

import org.eclipse.kapua.message.xml.MessageXmlRegistry;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * {@link Message} {@link Payload} definition.
 *
 * @since 1.0
 */
@XmlType(factoryClass = MessageXmlRegistry.class, factoryMethod = "newPayload")
public interface Payload extends Serializable {
}
