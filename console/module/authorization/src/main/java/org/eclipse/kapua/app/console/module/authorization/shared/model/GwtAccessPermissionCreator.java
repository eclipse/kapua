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

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityCreator;

public class GwtAccessPermissionCreator extends GwtEntityCreator {

    private static final long serialVersionUID = -1333808048669893906L;

    private String accessInfoId;

    private GwtPermission permission;

    public GwtAccessPermissionCreator() {
        super();
    }

    public String getAccessInfoId() {
        return accessInfoId;
    }

    public void setAccessInfoId(String name) {
        this.accessInfoId = name;
    }

    public GwtPermission getPermission() {
        return permission;
    }

    public void setPermission(GwtPermission permission) {
        this.permission = permission;
    }

}
