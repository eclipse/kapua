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
package org.eclipse.kapua.app.console.module.api.shared.model;

import java.io.Serializable;

public class GwtStringListItem extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -9095667239285215364L;

    public GwtStringListItem() {
    }

    public GwtStringListItem(String value) {
        set("value", value);
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getId() {
        return (String) get("id");
    }

    public void setValue(String value) {
        set("value", value);
    }

    public String getValue() {
        return (String) get("value");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GwtStringListItem other = (GwtStringListItem) obj;

        return getValue().equals(other.getValue());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getValue() == null ? 0 : getValue().hashCode());
        return result;
    }
}
