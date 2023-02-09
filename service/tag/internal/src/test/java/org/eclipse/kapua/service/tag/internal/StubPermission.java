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

import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;

import java.util.Objects;

public class StubPermission implements Permission {
    private final String domain;
    private final Actions action;
    private final KapuaId targetScopeId;
    private final KapuaId groupId;
    private final Boolean forwardable;

    public StubPermission(String domain, Actions actions, KapuaId targetScopeId, KapuaId groupId, Boolean forwardable) {
        this.domain = domain;
        this.action = actions;
        this.targetScopeId = targetScopeId;
        this.groupId = groupId;
        this.forwardable = forwardable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StubPermission stubPermission = (StubPermission) o;
        return Objects.equals(domain, stubPermission.domain) && action == stubPermission.action && Objects.equals(targetScopeId, stubPermission.targetScopeId) && Objects.equals(groupId, stubPermission.groupId) && Objects.equals(forwardable, stubPermission.forwardable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, action, targetScopeId, groupId, forwardable);
    }

    @Override
    public void setDomain(String domain) {

    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public void setAction(Actions action) {

    }

    @Override
    public Actions getAction() {
        return action;
    }

    @Override
    public void setTargetScopeId(KapuaId targetScopeId) {

    }

    @Override
    public KapuaId getTargetScopeId() {
        return targetScopeId;
    }

    @Override
    public void setGroupId(KapuaId groupId) {

    }

    @Override
    public KapuaId getGroupId() {
        return groupId;
    }

    @Override
    public void setForwardable(boolean forwardable) {

    }

    @Override
    public boolean getForwardable() {
        return forwardable;
    }
}
