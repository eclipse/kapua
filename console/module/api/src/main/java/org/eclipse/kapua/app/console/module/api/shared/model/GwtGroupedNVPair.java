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

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

import java.io.Serializable;

public class GwtGroupedNVPair extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 6017065568183482351L;

    @Override
    public <X> X get(String property) {
        ValidationMessages msgs = GWT.create(ValidationMessages.class);
        if ("groupLoc".equals(property)) {
            try {
                return (X) msgs.getString(getGroup());
            } catch (Exception e) {
                return (X) getGroup();
            }
        } else if ("nameLoc".equals(property)) {
            try {
                return (X) msgs.getString(getName());
            } catch (Exception e) {
                String[] nameSplitted = getName().split("_");

                StringBuilder sb = new StringBuilder();
                for (String split : nameSplitted) {
                    sb.append(split.substring(0, 1).toUpperCase())
                            .append(split.substring(1))
                            .append(" ");
                }

                return (X) sb.deleteCharAt(sb.length()).toString();
            }
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
