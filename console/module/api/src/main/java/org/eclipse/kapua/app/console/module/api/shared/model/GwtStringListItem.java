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
package org.eclipse.kapua.app.console.module.api.shared.model;

import java.io.Serializable;

public class GwtStringListItem extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -9095667239285215364L;

    private static final String VALUE = "value";

    public GwtStringListItem() {
    }

    public GwtStringListItem(String value) {
        set(VALUE, value);
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getId() {
        return (String) get("id");
    }

    public void setValue(String value) {
        set(VALUE, value);
    }

    public String getValue() {
        return (String) get(VALUE);
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
