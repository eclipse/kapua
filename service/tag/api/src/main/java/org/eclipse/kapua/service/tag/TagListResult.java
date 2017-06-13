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
package org.eclipse.kapua.service.tag;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.query.KapuaListResult;

/**
 * {@link Tag} list result definition.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "tags")
@XmlType(factoryClass = TagXmlRegistry.class, factoryMethod = "newTagListResult")
public interface TagListResult extends KapuaListResult<Tag> {

}
