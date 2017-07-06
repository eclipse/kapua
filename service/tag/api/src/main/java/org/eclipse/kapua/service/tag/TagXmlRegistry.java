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

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class TagXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final TagFactory factory = locator.getFactory(TagFactory.class);

    /**
     * Creates a new tag instance
     * 
     * @return
     */
    public Tag newTag() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new tag creator instance
     * 
     * @return
     */
    public TagCreator newTagCreator() {
        return factory.newCreator(null, null);
    }

    /**
     * Creates a new tag creator instance
     * 
     * @return
     */
    public TagListResult newTagListResult() {
        return factory.newListResult();
    }

    public TagQuery newQuery() {
        return factory.newQuery(null);
    }
}
