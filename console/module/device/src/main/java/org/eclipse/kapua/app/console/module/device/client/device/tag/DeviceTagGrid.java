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

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.module.tag.client.TagGrid;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagQuery;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

import java.util.List;

public class DeviceTagGrid extends TagGrid {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);
    private static final GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);

    private DeviceTagToolbar deviceTagToolbar;
    private GwtDevice selectedDevice;

    protected DeviceTagGrid(AbstractEntityView<GwtTag> entityView, GwtSession currentSession, GwtDevice selectedDevice) {
        super(entityView, currentSession);

        this.selectedDevice = selectedDevice;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtTag>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtTag>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtTag>> callback) {
                GWT_TAG_SERVICE.query((PagingLoadConfig) loadConfig, query, callback);

            }
        };
    }

    @Override
    protected DeviceTagToolbar getToolbar() {
        deviceTagToolbar = new DeviceTagToolbar(currentSession);

        return deviceTagToolbar;
    }

    @Override
    public void refresh() {
        if (selectedDevice != null) {
            mask(MSGS.loading());

            GWT_DEVICE_SERVICE.findDevice(selectedDevice.getScopeId(), selectedDevice.getId(), new AsyncCallback<GwtDevice>() {

                @Override
                public void onFailure(Throwable caught) {
                    FailureHandler.handle(caught);
                    unmask();
                }

                @Override
                public void onSuccess(GwtDevice gwtDevice) {
                    List<String> tagIds = gwtDevice.getTagIds();

                    if (!tagIds.isEmpty()) {
                        GwtTagQuery query = new GwtTagQuery();
                        query.setScopeId(gwtDevice.getScopeId());
                        query.setIds(tagIds);

                        refresh(query);
                    }
                    {
                        clearGridElement();
                    }

                    unmask();
                }
            });
        }
    }

    public void setSelectedDevice(GwtDevice selectedDevice) {

        this.selectedDevice = selectedDevice;

        deviceTagToolbar.setSelectedDevice(selectedDevice);
    }

}
