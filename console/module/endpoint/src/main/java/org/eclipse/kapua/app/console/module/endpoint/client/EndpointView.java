/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.client;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;

public class EndpointView extends AbstractEntityView<GwtEndpoint> {

    private EndpointGrid endpointGrid;

    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);

    public EndpointView(GwtSession gwtSession) {
        super(gwtSession);
    }

    public static String getName() {
        return MSGS.endpoints();
    }

    @Override
    public EntityGrid<GwtEndpoint> getEntityGrid(AbstractEntityView<GwtEndpoint> entityView,
            GwtSession currentSession) {
        if (endpointGrid == null) {
            endpointGrid = new EndpointGrid(entityView, currentSession);
        }
        return endpointGrid;
    }

    @Override
    public EntityFilterPanel<GwtEndpoint> getEntityFilterPanel(AbstractEntityView<GwtEndpoint> entityView,
            GwtSession currentSession) {

        return new EndpointFilterPanel(this, currentSession);
    }

}
