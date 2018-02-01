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
package org.eclipse.kapua.app.console.module.authorization.shared.model;

import java.util.Map;

import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtDomain;

import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtCheckedItems implements IsSerializable {

    private GwtDomain name;
    private boolean isChecked;
    private boolean isAllChecked;
    private Map<GwtAction, CheckBox> map;

    public GwtDomain getName() {
        return name;
    }

    public void setName(GwtDomain name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Map<GwtAction, CheckBox> getMap() {
        return map;
    }

    public void setMap(Map<GwtAction, CheckBox> map) {
        this.map = map;
    }

    public boolean isAllChecked() {
        return isAllChecked;
    }

    public void setAllChecked(boolean isAllChecked) {
        this.isAllChecked = isAllChecked;
    }
}
