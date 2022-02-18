/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.account.client.cors;

import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;

import com.google.gwt.core.client.GWT;

public class CorsView extends AbstractEntityView<GwtEndpoint> implements EntityView<GwtEndpoint> {

    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);

    public CorsView(GwtSession currentSession) {
        super();
        this.currentSession = currentSession;
    }

    @Override
    public EntityGrid<GwtEndpoint> getEntityGrid(AbstractEntityView<GwtEndpoint> entityView, GwtSession currentSession) {
        return new CorsGrid(currentSession, this);
    }

    @Override
    public EntityFilterPanel<GwtEndpoint> getEntityFilterPanel(AbstractEntityView<GwtEndpoint> entityView, GwtSession currentSession2) {
        return null;
    }

}
