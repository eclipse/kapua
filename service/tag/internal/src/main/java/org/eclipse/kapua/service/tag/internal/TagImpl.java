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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.tag.Tag;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * {@link Tag} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "Tag")
@Table(name = "tag_tag")
public class TagImpl extends AbstractKapuaNamedEntity implements Tag {

    private static final long serialVersionUID = -3760818776351242930L;

    protected TagImpl() {
        super();
    }

    /**
     * Constructor.
     * <p>
     * Creates a soft clone.
     *
     * @param tag The {@link Tag} from which to create the new {@link Tag}.
     * @throws KapuaException
     */
    public TagImpl(Tag tag) throws KapuaException {
        super(tag);
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}
     */
    public TagImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
