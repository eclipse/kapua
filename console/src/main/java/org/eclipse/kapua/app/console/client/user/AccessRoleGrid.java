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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.user;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.shared.service.GwtRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtRoleServiceAsync;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class AccessRoleGrid extends EntityGrid<GwtRole> {
    
    private static final GwtRoleServiceAsync gwtRoleService = GWT.create(GwtRoleService.class);
    
    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    private String userId = null;
    
    public AccessRoleGrid(EntityView<GwtRole> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtRole>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtRole>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtRole>> callback) {
                if (userId != null) {
                    gwtRoleService.getByUserId((PagingLoadConfig) loadConfig,
                            currentSession.getSelectedAccount().getId(),
                            userId,
                            callback);
                }
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        
        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridRoleColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("name", MSGS.gridRoleColumnHeaderName(), 400);
        columnConfigs.add(columnConfig);
        
        columnConfig = new ColumnConfig("createdBy", MSGS.gridRoleColumnHeaderCreatedBy(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOn", MSGS.gridRoleColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return null;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) { }

    @Override
    protected EntityCRUDToolbar<GwtRole> getToolbar() {
        // TODO Auto-generated method stub
        return super.getToolbar();
    }
    
}
