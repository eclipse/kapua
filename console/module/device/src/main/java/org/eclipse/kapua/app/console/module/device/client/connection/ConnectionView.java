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
package org.eclipse.kapua.app.console.module.device.client.connection;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleConnectionMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.connection.GwtDeviceConnection;

public class ConnectionView extends AbstractEntityView<GwtDeviceConnection> {

    private ConnectionGrid connectionGrid;
    private ConnectionFilterPanel filterPanel;

    private static final ConsoleConnectionMessages MSGS = GWT.create(ConsoleConnectionMessages.class);

    public ConnectionView(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    public EntityGrid<GwtDeviceConnection> getEntityGrid(AbstractEntityView<GwtDeviceConnection> entityView, GwtSession currentSession) {
        if (connectionGrid == null) {
            connectionGrid = new ConnectionGrid(this, currentSession);
        }
        return connectionGrid;
    }

    @Override
    public EntityFilterPanel<GwtDeviceConnection> getEntityFilterPanel(AbstractEntityView<GwtDeviceConnection> entityView, GwtSession currentSession) {
        if (filterPanel == null) {
            filterPanel = new ConnectionFilterPanel(entityView, currentSession);
        }
        return filterPanel;
    }

    public static String getName() {
        return MSGS.connections();
    }
}
