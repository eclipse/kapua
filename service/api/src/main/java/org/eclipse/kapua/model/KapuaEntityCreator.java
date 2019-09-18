/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * {@link KapuaEntityCreator} definition
 * <p>
 * All the {@link KapuaEntityCreator} will be an extension of this.
 *
 * @param <E> entity type
 * @since 1.0.0
 */
public interface KapuaEntityCreator<E extends KapuaEntity> {

    /**
     * Gets the scope {@link KapuaId}
     *
     * @return the scope {@link KapuaId}
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getScopeId();

    /**
     * Sets the scope {@link KapuaId}
     *
     * @param scopeId the scope {@link KapuaId}
     * @since 1.0.0
     */
    void setScopeId(KapuaId scopeId);
}
