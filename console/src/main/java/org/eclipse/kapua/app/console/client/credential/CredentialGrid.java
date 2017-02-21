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

import org.eclipse.kapua.app.console.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.misc.color.Color;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredentialQuery;
import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.shared.service.GwtCredentialService;
import org.eclipse.kapua.app.console.shared.service.GwtCredentialServiceAsync;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CredentialGrid extends EntityGrid<GwtCredential> {

    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);
    private static final GwtCredentialServiceAsync gwtCredentialService = GWT.create(GwtCredentialService.class);
    private GwtCredentialQuery query;

    private CredentialToolbar toolbar;

    protected CredentialGrid(EntityView<GwtCredential> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtCredentialQuery();
        query.setScopeId(currentSession.getSelectedAccount().getId());
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtCredential>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtCredential>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtCredential>> callback) {
                gwtCredentialService.query((PagingLoadConfig) loadConfig,
                        query,
                        callback);
            }
        };
    }

    @Override
    protected void selectionChangedEvent(GwtCredential selectedItem) {
        super.selectionChangedEvent(selectedItem);
        getToolbar().getEditEntityButton().setEnabled(selectedItem != null && currentSession.hasCredentialUpdatePermission());
        getToolbar().getDeleteEntityButton().setEnabled(selectedItem != null && currentSession.hasCredentialDeletePermission());
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("status", MSGS.gridCredentialColumnHeaderStatus(), 50);
        GridCellRenderer<GwtCredential> setStatusIcon = new GridCellRenderer<GwtCredential>() {

            public String render(GwtCredential gwtUser, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtCredential> deviceList, Grid<GwtCredential> grid) {

                KapuaIcon icon = new KapuaIcon(IconSet.KEY);
                icon.setColor(Color.GREY);
                return icon.getInlineHTML();
            }
        };
        columnConfig.setRenderer(setStatusIcon);
        columnConfig.setAlignment(Style.HorizontalAlignment.CENTER);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("id", MSGS.gridCredentialColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("username", MSGS.gridCredentialColumnHeaderUsername(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("credentialType", MSGS.gridCredentialColumnHeaderCredentialType(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("", MSGS.gridCredentialColumnHeaderExpirationDate(), 200);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {
        query = (GwtCredentialQuery) filterQuery;
    }

    @Override
    protected EntityCRUDToolbar<GwtCredential> getToolbar() {
        if (toolbar == null) {
            toolbar = new CredentialToolbar(currentSession);
        }
        return toolbar;
    }
}
