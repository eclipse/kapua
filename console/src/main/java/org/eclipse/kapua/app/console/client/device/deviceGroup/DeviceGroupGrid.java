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
package org.eclipse.kapua.app.console.client.device.deviceGroup;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleDeviceGroupMessages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceGroup;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceGroupService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceGroupServiceAsync;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceGroupGrid extends EntityGrid<GwtDeviceGroup> {

    private static final GwtDeviceGroupServiceAsync service = GWT
            .create(GwtDeviceGroupService.class);
    private GwtDeviceGroupQuery query;
    private String devId = null;
    private DeviceGroupTabItemToolbar toolbar;
    private static final ConsoleDeviceGroupMessages MSGS = GWT
            .create(ConsoleDeviceGroupMessages.class);

    protected DeviceGroupGrid(EntityView<GwtDeviceGroup> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtDeviceGroupQuery();
        query.setScopeId(currentSession.getSelectedAccount().getId());

    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtDeviceGroup>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtDeviceGroup>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<PagingLoadResult<GwtDeviceGroup>> callback) {
                service.findByDeviceId((PagingLoadConfig) loadConfig,
                        currentSession.getSelectedAccount().getId(), devId, callback);

            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridDeviceGroupColumnHeaderId(),100);
        columnConfig.setHidden(true);
        columnConfigs.add(columnConfig);
        
        columnConfig = new ColumnConfig("groupName", MSGS.gridDeviceGroupColumnHeaderGroupName(),200);
        columnConfigs.add(columnConfig);


        columnConfig = new ColumnConfig("createdBy", MSGS.gridDeviceGroupColumnHeaderCreatedBy(),200);
        columnConfigs.add(columnConfig);
        
        columnConfig = new ColumnConfig("createdOn", MSGS.gridDeviceGroupColumnHeaderCreatedOn(),200);
        columnConfigs.add(columnConfig);
        
        return columnConfigs;
    }

    @Override
    protected GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    protected void setFilterQuery(GwtQuery filterQuery) {

    }

    @Override
    protected DeviceGroupTabItemToolbar getToolbar() {
        if (toolbar == null) {
            toolbar = new DeviceGroupTabItemToolbar(currentSession);
        }
        return toolbar;
    }

    public String getDeviceId() {
        return devId;
    }

    public void setDeviceId(String devId) {
        this.devId = devId;
    }
    
    @Override
    protected void selectionChangedEvent(GwtDeviceGroup selectedItem) {
        super.selectionChangedEvent(selectedItem);
        getToolbar().refreshButtons();
    }

}
