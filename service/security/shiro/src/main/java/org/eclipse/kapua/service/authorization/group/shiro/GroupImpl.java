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
package org.eclipse.kapua.service.authorization.group.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.Group;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * {@link Group} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "Group")
@Table(name = "athz_group")
public class GroupImpl extends AbstractKapuaNamedEntity implements Group {

    private static final long serialVersionUID = -3760818776351242930L;

    /**
     * Constructor.
     * <p>
     * Required by JPA.
     *
     * @since 1.1.0
     */
    public GroupImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId the scope {@link KapuaId}
     * @since 1.0.0
     */
    public GroupImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param group the {@link Group} to clone.
     * @throws KapuaException
     * @since 1.0.0
     */
    public GroupImpl(Group group) throws KapuaException {
        super(group);
    }
}
