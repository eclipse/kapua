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
package org.eclipse.kapua.app.console.module.authorization.client.role.dialog;

import java.util.List;

import org.eclipse.kapua.app.console.commons.client.ui.widget.EntityGridFieldToolbar;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;

public class RolePermissionEditGridField extends RolePermissionNewGridField {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    protected RolePermissionEditGridField(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = super.getColumns();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.dialogEditFieldGridRolePermissionId(), 80);
        columnConfigs.add(0, columnConfig);

        columnConfig = new ColumnConfig("createdBy", MSGS.dialogEditFieldGridRolePermissionCreatedBy(), 100);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.dialogEditFieldGridRolePermissionCreatedOn(), 100);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    protected EntityGridFieldToolbar<GwtRolePermission> getToolbar() {
        return new RolePermissionGridFieldToolbar(currentSession);
    }
}
