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

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseModel;

/**
 * GwtComboItem
 *
 * This is a generic key, value object to use with the combo box control.
 *
 */
public class GwtComboItem extends BaseModel implements Serializable {

    private static final long serialVersionUID = 2179331098490277748L;

    public void setComboId(String id) {
        set("ComboId", id);
    }

    public String getComboId() {
        return (String) get("ComboId");
    }

    public void setComboValue(String value) {
        set("ComboValue", value);
    }

    public String getComboValue() {
        return (String) get("ComboValue");
    }

    public void setDisabled(boolean disabled) {
        set("ComboDisabled", disabled);
    }

    public boolean isDisable() {
        return (Boolean) get("ComboDisabled");
    }

    public <X> X get(String property) {
        return super.get(property);
    }

}
