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
package org.eclipse.kapua.app.console.shared.model;

import org.eclipse.kapua.app.console.commons.shared.models.KapuaBaseModel;

import java.io.Serializable;

public class GwtBrokerTypeOption extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 8422679791525684523L;

    public GwtBrokerTypeOption() {
        super();
    }

    public GwtBrokerTypeOption(GwtBrokerType type) {
        setType(type.name());
        setName(type.name());
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getType() {
        return (String) get("type");
    }

    public GwtBrokerType getTypeEnum() {
        return (GwtBrokerType) get("typeEnum");
    }

    public void setType(String type) {
        set("type", type);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("typeEnum".equals(property)) {
            String type = getType();
            if (type != null) {
                return (X) GwtBrokerType.valueOf(type);
            }
            return (X) "";
        } else {
            return super.get(property);
        }
    }

}
