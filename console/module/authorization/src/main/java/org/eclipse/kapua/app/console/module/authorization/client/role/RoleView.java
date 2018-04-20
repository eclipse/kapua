/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.client.role;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;

public class RoleView extends AbstractEntityView<GwtRole> {

    private RoleGrid roleGrid;

    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    public RoleView(GwtSession gwtSession) {
        super(gwtSession);
    }

    public static String getName() {
        return MSGS.mainMenuRoles();
    }

    @Override
    public EntityGrid<GwtRole> getEntityGrid(AbstractEntityView<GwtRole> entityView, GwtSession currentSession) {
        if (roleGrid == null) {
            roleGrid = new RoleGrid(entityView, currentSession);
        }
        return roleGrid;
    }

    @Override
    public EntityFilterPanel<GwtRole> getEntityFilterPanel(AbstractEntityView<GwtRole> entityView, GwtSession currentSession) {
        return new RoleFilterPanel(this, currentSession);
    }
}
