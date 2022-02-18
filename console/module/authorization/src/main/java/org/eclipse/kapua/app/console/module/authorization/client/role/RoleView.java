/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
