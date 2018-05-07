/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
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
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.color.Color;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.permission.UserSessionPermission;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUserQuery;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class UserGrid extends EntityGrid<GwtUser> {

    private GwtUserQuery query;
    private UserGridToolbar toolbar;

    private static final GwtUserServiceAsync GWT_USER_SERVICE = GWT.create(GwtUserService.class);

    private final static ConsoleUserMessages USER_MSGS = GWT.create(ConsoleUserMessages.class);
    private final static ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    public UserGrid(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtUserQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
    }

    @Override
    protected void selectionChangedEvent(GwtUser selectedItem) {
        super.selectionChangedEvent(selectedItem);
        getToolbar().getAddEntityButton().setEnabled(currentSession.hasPermission(UserSessionPermission.write()));
        if (selectedItem != null) {
            getToolbar().getEditEntityButton().setEnabled(currentSession.hasPermission(UserSessionPermission.write()));
            getToolbar().getDeleteEntityButton().setEnabled(currentSession.hasPermission(UserSessionPermission.delete()));
        } else {
            getToolbar().getEditEntityButton().setEnabled(false);
            getToolbar().getDeleteEntityButton().setEnabled(false);
        }
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtUser>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtUser>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtUser>> callback) {
                GWT_USER_SERVICE.query((PagingLoadConfig) loadConfig,
                        query,
                        callback);
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("status", USER_MSGS.gridUserColumnHeaderStatus(), 50);
        GridCellRenderer<GwtUser> setStatusIcon = new GridCellRenderer<GwtUser>() {

            @Override
            public String render(GwtUser gwtUser, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtUser> deviceList, Grid<GwtUser> grid) {

                KapuaIcon icon;
                if (gwtUser.getStatusEnum() != null) {
                    switch (gwtUser.getStatusEnum()) {
                    case DISABLED:
                        icon = new KapuaIcon(IconSet.USER);
                        icon.setColor(Color.RED);
                        icon.setTitle(MSGS.disabled());
                        break;
                    case ENABLED:
                        icon = new KapuaIcon(IconSet.USER);
                        icon.setColor(Color.GREEN);
                        icon.setTitle(MSGS.enabled());
                        break;
                    default:
                        icon = new KapuaIcon(IconSet.USER);
                        icon.setColor(Color.GREY);
                        icon.setTitle(MSGS.unknown());
                        break;
                    }
                } else {
                    icon = new KapuaIcon(IconSet.USER);
                    icon.setColor(Color.GREY);
                }

                return icon.getInlineHTML();
            }
        };
        columnConfig.setRenderer(setStatusIcon);
        columnConfig.setAlignment(HorizontalAlignment.CENTER);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("id", USER_MSGS.gridUserColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("username", USER_MSGS.gridUserColumnHeaderUsername(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("displayName", USER_MSGS.gridUserColumnHeaderDisplayName(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("phoneNumber", USER_MSGS.gridUserColumnHeaderPhoneNumber(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("email", USER_MSGS.gridUserColumnHeaderEmail(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("expirationDateFormatted", USER_MSGS.gridUserColumnHeaderExpirationDate(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("modifiedOnFormatted", USER_MSGS.gridUserColumnHeaderModifiedOn(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("modifiedByName", USER_MSGS.gridUserColumnHeaderModifiedBy(), 200);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        query = (GwtUserQuery) filterQuery;
    }

    @Override
    protected EntityCRUDToolbar<GwtUser> getToolbar() {
        if (toolbar == null) {
            toolbar = new UserGridToolbar(currentSession);
        }
        return toolbar;
    }

}
