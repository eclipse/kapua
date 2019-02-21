/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
