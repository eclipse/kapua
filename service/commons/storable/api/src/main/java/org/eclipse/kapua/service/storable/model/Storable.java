/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * {@link Storable} definition.
 * <p>
 * It is the base {@code interface} for all {@link Object}s that are {@link Storable}
 *
 * @since 1.0.0
 */
@XmlType(propOrder = {"scopeId"})
public interface Storable extends KapuaSerializable {

    /**
     * Gets the scope {@link KapuaId}.
     *
     * @return The scope {@link KapuaId}.
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getScopeId();

    /**
     * Sets the scope {@link KapuaId}.
     *
     * @param scopeId The scope {@link KapuaId}.
     * @since 1.3.0
     */
    void setScopeId(KapuaId scopeId);

}
