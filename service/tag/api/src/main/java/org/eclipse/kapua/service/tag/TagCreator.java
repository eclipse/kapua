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
package org.eclipse.kapua.service.tag;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link TagCreator} definition
 * <p>
 * It is used to create a new {@link Tag}.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "tagCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = TagXmlRegistry.class, factoryMethod = "newTagCreator")
public interface TagCreator extends KapuaNamedEntityCreator<Tag> {
}
