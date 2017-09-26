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
package org.eclipse.kapua.app.console.module.api.client.ui.panel;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;

public class KapuaTabPanel<M extends GwtEntityModel> extends TabPanel {

    public KapuaTabPanel() {
        setPlain(true);
        setBorders(false);
        setBodyBorder(false);
    }

    public void setEntity(M entity) {
        for (TabItem t : getItems()) {
            ((KapuaTabItem<M>) t).setEntity(entity);
        }

        if (getSelectedItem() != null) {
            if (!getSelectedItem().isEnabled()) {
                selectDefaultTab();
            }

            ((KapuaTabItem<M>) getSelectedItem()).refresh();
        }
    }

    public void selectDefaultTab() {
        if (!getItems().isEmpty()) {
            setSelection(getItem(0));
        }
    }
}
