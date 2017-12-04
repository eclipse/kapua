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
package org.eclipse.kapua.app.console.module.api.client.ui.tab;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;

public abstract class KapuaTabItem<M extends GwtEntityModel> extends TabItem {

    protected M selectedEntity;

    private boolean dirty;

    public KapuaTabItem(String title, KapuaIcon tabIcon) {
        super(title, tabIcon);

        setBorders(true);
        setLayout(new FitLayout());

        addListener(Events.Select, new Listener<ComponentEvent>() {

            public void handleEvent(ComponentEvent be) {
                refresh();
            }
        });
    }

    public void setEntity(M t) {
        selectedEntity = t;
        dirty = true;
    }

    public M getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(M selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public void refresh() {
        if (dirty) {
            doRefresh();
            dirty = false;
        }
    }

    protected abstract void doRefresh();
}
