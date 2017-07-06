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
package org.eclipse.kapua.app.console.client.tag;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.commons.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.commons.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.commons.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.commons.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtTag;

public class TagView extends AbstractEntityView<GwtTag> {

    private TagGrid tagGrid;

    public TagView(GwtSession gwtSession) {
        super(gwtSession);
    }

    @Override
    public List<KapuaTabItem<GwtTag>> getTabs(AbstractEntityView<GwtTag> entityView,
            GwtSession currentSession) {
        List<KapuaTabItem<GwtTag>> tabs = new ArrayList<KapuaTabItem<GwtTag>>();
        tabs.add(new TagTabDescription());
        return tabs;
    }

    @Override
    public EntityGrid<GwtTag> getEntityGrid(AbstractEntityView<GwtTag> entityView,
            GwtSession currentSession) {
        if (tagGrid == null) {
            tagGrid = new TagGrid(entityView, currentSession);
        }
        return tagGrid;
    }

    @Override
    public EntityFilterPanel<GwtTag> getEntityFilterPanel(AbstractEntityView<GwtTag> entityView,
            GwtSession currentSession2) {

        return new TagFilterPanel(this, currentSession2);
    }

}