/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    public List<GwtPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<GwtPermission> permissions) {
        this.permissions = permissions;
    }
}
