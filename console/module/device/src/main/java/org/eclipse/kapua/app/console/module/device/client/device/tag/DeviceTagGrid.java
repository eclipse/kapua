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
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.client.device.tag;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.tag.client.TagGrid;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

import java.util.ArrayList;

public class DeviceTagGrid extends TagGrid {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private static final GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);

    private DeviceTagToolbar deviceTagToolbar;
    private GwtDevice selectedDevice;

    protected DeviceTagGrid(AbstractEntityView<GwtTag> entityView, GwtSession currentSession, GwtDevice selectedDevice) {
        super(entityView, currentSession);
        refreshOnRender = false;
        this.selectedDevice = selectedDevice;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        /* Despite this grid, being a "slave" grid (i.e. a grid that depends on the value
         * selected in another grid) and so not refreshed on render (see comment in
         * EntityGrid class), it should be refreshed anyway on render if no item is
         * selected on the master grid, otherwise the paging toolbar will still be enabled
         * even if no results are actually available in this grid */
        if (selectedDevice == null) {
            refresh();
        }
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtTag>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtTag>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtTag>> callback) {
                if (selectedDevice != null) {
                    GWT_TAG_SERVICE.findByDeviceId((PagingLoadConfig) loadConfig, currentSession.getSelectedAccountId(), selectedDevice.getId(), callback);
                } else {
                    callback.onSuccess(new BasePagingLoadResult<GwtTag>(new ArrayList<GwtTag>()));
                }
            }
        };

    }

    @Override
    protected DeviceTagToolbar getToolbar() {
        if (deviceTagToolbar == null) {
            deviceTagToolbar = new DeviceTagToolbar(currentSession);
        }
        return deviceTagToolbar;
    }

    public void setSelectedDevice(GwtDevice selectedDevice) {
        this.selectedDevice = selectedDevice;
        deviceTagToolbar.setSelectedDevice(selectedDevice);
    }

}
