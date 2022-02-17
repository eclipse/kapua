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
import java.util.ArrayList;
import java.util.List;

public class GwtConfigComponent extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -6388356998309026758L;

    private static final String COMPONENT_ID = "componentId";
    private static final String COMPONENT_NAME = "componentName";

    private List<GwtConfigParameter> parameters;

    public GwtConfigComponent() {
        parameters = new ArrayList<GwtConfigParameter>();
    }

    public String getComponentId() {
        return get(COMPONENT_ID);
    }

    public String getUnescapedComponentId() {
        return getUnescaped(COMPONENT_ID);
    }

    public void setId(String componentId) {
        set(COMPONENT_ID, componentId);
    }

    public String getComponentName() {
        return get(COMPONENT_NAME);
    }

    public String getUnescapedComponentName() {
        return getUnescaped(COMPONENT_NAME);
    }

    public void setName(String componentName) {
        set(COMPONENT_NAME, componentName);
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
