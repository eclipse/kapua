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
import java.util.ArrayList;
import java.util.List;

public class GwtConfigComponent extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -6388356998309026758L;

    private List<GwtConfigParameter> parameters;

    public GwtConfigComponent() {
        parameters = new ArrayList<GwtConfigParameter>();
    }

    public String getComponentId() {
        return get("componentId");
    }

    public String getUnescapedComponentId() {
        return getUnescaped("componentId");
    }

    public void setId(String componentId) {
        set("componentId", componentId);
    }

    public String getComponentName() {
        return get("componentName");
    }

    public String getUnescapedComponentName() {
        return getUnescaped("componentName");
    }

    public void setName(String componentName) {
        set("componentName", componentName);
    }

    public String getComponentDescription() {
        return get("componentDescription");
    }

    public void setDescription(String componentDescription) {
        set("componentDescription", componentDescription);
    }

    public String getComponentIcon() {
        return get("componentIcon");
    }

    public void setComponentIcon(String componentIcon) {
        set("componentIcon", componentIcon);
    }

    public List<GwtConfigParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<GwtConfigParameter> parameters) {
        this.parameters = parameters;
    }

    public GwtConfigParameter getParameter(String id) {
        for (GwtConfigParameter param : parameters) {
            if (param.getId().equals(id)) {
                return param;
            }
        }
        return null;
    }
}
