/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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


import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

public class GwtGroupedNVPair extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 6017065568183482351L;

    @Override
    @SuppressWarnings("unchecked")
    public <X> X get(String property) {
        if ("groupLoc".equals(property)) {
            ValidationMessages msgs = GWT.create(ValidationMessages.class);
            return (X) msgs.getString(getGroup());
        } else if ("nameLoc".equals(property)) {
            ValidationMessages msgs = GWT.create(ValidationMessages.class);
            return (X) msgs.getString(getName());
        } else {
            X value = (X) super.get(property);
            if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                value = (X) "N/A";
            }
            return value;
        }
    }

    public GwtGroupedNVPair() {
    }

    public GwtGroupedNVPair(String group, String name, Object value) {
        this();
        setGroup(group);
        setName(name);
        setValue(value);
    }

    public void setGroup(String group) {
        set("group", group);
    }

    public String getGroup() {
        return get("group");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return get("name");
    }

    public void setValue(Object value) {
        set("value", value);
    }

    public Object getValue() {
        return get("value");
    }
}
