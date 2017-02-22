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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.credential;

import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;

import java.util.ArrayList;
import java.util.List;

public class CredentialView extends EntityView<GwtCredential> {

    private CredentialGrid credentialGrid;

    public CredentialView(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    public List<KapuaTabItem<GwtCredential>> getTabs(EntityView<GwtCredential> entityView, GwtSession currentSession) {
        List<KapuaTabItem<GwtCredential>> tabs = new ArrayList<KapuaTabItem<GwtCredential>>();
        tabs.add(new CredentialTabDescription());
        return tabs;
    }

    @Override
    public EntityGrid<GwtCredential> getEntityGrid(EntityView<GwtCredential> entityView, GwtSession currentSession) {
        if (credentialGrid == null) {
            credentialGrid = new CredentialGrid(entityView, currentSession);
        }
        return credentialGrid;
    }

    @Override
    public EntityFilterPanel<GwtCredential> getEntityFilterPanel(EntityView<GwtCredential> entityView, GwtSession currentSession) {
        return new CredentialFilterPanel(this, currentSession);
    }
}
