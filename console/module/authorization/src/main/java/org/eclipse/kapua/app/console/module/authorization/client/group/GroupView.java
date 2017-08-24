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
package org.eclipse.kapua.app.console.module.authorization.client.group;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleGroupMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;

public class GroupView extends AbstractEntityView<GwtGroup> {

    private GroupGrid groupGrid;

    private static final ConsoleGroupMessages MSGS = GWT.create(ConsoleGroupMessages.class);

    public GroupView(GwtSession gwtSession) {
        super(gwtSession);
    }

    public static String getName() {
        return MSGS.groups();
    }

    @Override
    public EntityGrid<GwtGroup> getEntityGrid(AbstractEntityView<GwtGroup> entityView,
            GwtSession currentSession) {
        if (groupGrid == null) {
            groupGrid = new GroupGrid(entityView, currentSession);
        }
        return groupGrid;
    }

    @Override
    public EntityFilterPanel<GwtGroup> getEntityFilterPanel(AbstractEntityView<GwtGroup> entityView,
            GwtSession currentSession2) {

        return new GroupFilterPanel(this, currentSession2);
    }
}
