/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.group;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Interface used to mark group-able entities.
 */
public interface Groupable {

    /**
     * Sets the {@link Group} id of this entity.
     *
     * @param groupId The {@link Group} id to assign.
     * @since 1.0.0
     */
    void setGroupId(KapuaId groupId);

    /**
     * Gets the {@link Group} id assigned to this entity.
     *
     * @return The {@link Group} id assigned to this entity.
     * @since 1.0.0
     */
    @XmlElement(name = "groupId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getGroupId();

}
