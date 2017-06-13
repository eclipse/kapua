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
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;

/**
 * {@link TagCreator} implementation.
 * 
 * @since 1.0.0
 */
public class TagCreatorImpl extends AbstractKapuaEntityCreator<Tag> implements TagCreator {

    private static final long serialVersionUID = -4676187845961673421L;

    private String name;

    /**
     * Constructor
     * 
     * @param scopeId
     *            The scope id to set.
     * @param name
     *            The name to set for this {@link DomainCreator}.
     * @since 1.0.0
     */
    public TagCreatorImpl(KapuaId scopeId, String name) {
        super(scopeId);
        setName(name);
    }

    public TagCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
