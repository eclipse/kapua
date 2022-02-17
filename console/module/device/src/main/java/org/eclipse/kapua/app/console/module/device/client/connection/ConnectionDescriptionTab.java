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

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.EntityDescriptionTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.connection.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceConnectionService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceConnectionServiceAsync;

public class ConnectionDescriptionTab extends EntityDescriptionTabItem<GwtDeviceConnection> {

    private static final GwtDeviceConnectionServiceAsync GWT_DEVICE_CONNECTION_SERVICE = GWT.create(GwtDeviceConnectionService.class);
    private static final String CONNECTION = "connection";

    public ConnectionDescriptionTab(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected RpcProxy<ListLoadResult<GwtGroupedNVPair>> getDataProxy() {
        return new RpcProxy<ListLoadResult<GwtGroupedNVPair>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtGroupedNVPair>> callback) {
                GWT_DEVICE_CONNECTION_SERVICE.getConnectionInfo(selectedEntity.getScopeId(), selectedEntity.getId(), callback);
            }
        };
    }

    @Override
    protected String getGroupViewText() {
        return MSGS.tabDescriptionNoItemSelected(CONNECTION);
    }

}
