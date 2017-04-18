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
package org.eclipse.kapua.service.authorization.group.shiro;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.Group;

/**
 * {@link Group} implementation.
 * 
 * @since 1.0
 */
@Entity(name = "Group")
@Table(name = "athz_group")
public class GroupImpl extends AbstractKapuaUpdatableEntity implements Group {

    private static final long serialVersionUID = -3760818776351242930L;

    @Basic
    @Column(name = "name")
    private String name;

    protected GroupImpl() {
        super();
    }

    /**
     * Constructor.<br>
     * Creates a soft clone.
     * 
     * @param group
     * @throws KapuaException
     */
    public GroupImpl(Group group) throws KapuaException {
        super((AbstractKapuaUpdatableEntity) group);

        setName(group.getName());
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public GroupImpl(KapuaId scopeId) {
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
