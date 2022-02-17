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
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;

/**
 * {@link TagCreator} implementation.
 *
 * @since 1.0.0
 */
public class TagCreatorImpl extends AbstractKapuaNamedEntityCreator<Tag> implements TagCreator {

    private static final long serialVersionUID = -4676187845961673421L;

    /**
     * Constructor.
     *
     * @param scopeId The scope id to set.
     * @param name    The name to set for this {@link DomainCreator}.
     * @since 1.0.0
     */
    public TagCreatorImpl(KapuaId scopeId, String name) {
        super(scopeId);

        setName(name);
    }

    /**
     * Constructor
     *
     * @param scopeId The scopeId {@link KapuaId}
     */
    public TagCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
