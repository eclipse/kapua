/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message;

import org.eclipse.kapua.message.xml.MessageXmlRegistry;

import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * {@link Payload} definition.
 *
 * @since 1.0.0
 */
@XmlType(factoryClass = MessageXmlRegistry.class, factoryMethod = "newPayload")
public interface Payload extends Serializable {
}
