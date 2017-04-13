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
 *******************************************************************************/
package org.eclipse.kapua.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

/**
 * Kapua entity base creator service definition.<br>
 * All the Kapua entities base creator service will be an extension of this entity.
 *
 * @param <E>
 *            entity type
 * 
 * @since 1.0.0
 * 
 */
@XmlType(propOrder = { "scopeId" })
public interface KapuaEntityCreator<E extends KapuaEntity> {

    /**
     * Get the Kapua scope identifier
     * 
     * @return
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getScopeId();

    /**
     * Set the Kapua scope identifier
     * 
     * @param scopeId
     */
    public void setScopeId(KapuaId scopeId);
}
