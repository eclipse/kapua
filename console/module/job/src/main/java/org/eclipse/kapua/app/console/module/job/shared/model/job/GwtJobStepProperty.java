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
package org.eclipse.kapua.app.console.module.job.shared.model.job;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtJobStepProperty extends KapuaBaseModel {

    public String getPropertyName() {
        return get("propertyName");
    }

    public void setPropertyName(String propertyName) {
        set("propertyName", propertyName);
    }

    public String getPropertyType() {
        return get("propertyType");
    }

    public void setPropertyType(String propertyType) {
        set("propertyType", propertyType);
    }

    public String getPropertyValue() {
        return get("propertyValue");
    }

    public void setPropertyValue(String propertyValue) {
        set("propertyValue", propertyValue, false);
    }

}
