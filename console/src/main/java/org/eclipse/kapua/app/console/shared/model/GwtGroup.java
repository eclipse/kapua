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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

public class GwtGroup extends GwtUpdatableEntityModel {

    public String getGroupName() {
        return get("groupName");
    }

    public void setGroupName(String name) {
        set("groupName", name);
        set("value", name);
    }

    @Override
    public String toString() {
        return getGroupName();
    }
}
