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

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected TagImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId}
     * @since 1.0.0
     */
    public TagImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param tag The {@link Tag} from which to create the new {@link Tag}.
     * @throws KapuaException
     * @since 1.0.0
     */
    public TagImpl(Tag tag) throws KapuaException {
        super(tag);
    }
}
