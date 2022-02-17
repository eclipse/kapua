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
package org.eclipse.kapua.app.console.module.authorization.shared.model;

import java.util.List;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityCreator;

public class GwtRoleCreator extends GwtEntityCreator {

    private static final long serialVersionUID = -1333808048669893906L;

    private String name;
    private String description;

    private List<GwtPermission> permissions;

    public GwtRoleCreator() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GwtPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<GwtPermission> permissions) {
        this.permissions = permissions;
    }
}
