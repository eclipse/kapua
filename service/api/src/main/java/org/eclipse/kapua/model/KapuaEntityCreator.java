/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

/**
 * {@link KapuaEntityCreator} definition.<br>
 * All the {@link KapuaEntityCreator}s will be an extension of this entity.
 *
 * @param <E>
 *            The {@link KapuaEntity} of which this {@link KapuaEntityCreator} is the creator model.
 * 
 * @since 1.0.0
 * 
 */
@XmlType(propOrder = { "scopeId" })
public interface KapuaEntityCreator<E extends KapuaEntity> {

    /**
     * Gets the scope {@link KapuaId} of this {@link KapuaEntityCreator}.
     * 
     * @return The scope {@link KapuaId} of this {@link KapuaEntityCreator}.
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getScopeId();

    /**
     * Sets the scope {@link KapuaId} in which create the {@link KapuaEntity}.
     * 
     * @param scopeId
     *            The scope {@link KapuaId} in which create the {@link KapuaEntity}.
     * @since 1.0.0
     */
    public void setScopeId(KapuaId scopeId);
}
