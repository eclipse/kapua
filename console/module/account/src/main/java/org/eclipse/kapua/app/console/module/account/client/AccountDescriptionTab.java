/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.account.client;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountServiceAsync;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.EntityDescriptionTabItem;
import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountDescriptionTab extends EntityDescriptionTabItem<GwtAccount> {

    private static final GwtAccountServiceAsync GWT_ACCOUNT_SERVICE = GWT.create(GwtAccountService.class);
    private static final String CHILD_ACCOUNT = "child account";

    public AccountDescriptionTab(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected RpcProxy<ListLoadResult<GwtGroupedNVPair>> getDataProxy() {
        return new RpcProxy<ListLoadResult<GwtGroupedNVPair>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtGroupedNVPair>> callback) {
                GWT_ACCOUNT_SERVICE.getAccountInfo(currentSession.getSelectedAccountId(), selectedEntity.getId(), callback);
            }
        };
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
    }

    @Override
    protected String getGroupViewText() {
        return MSGS.tabDescriptionNoItemSelected(CHILD_ACCOUNT);
    }

    @Override
    protected Object renderValueCell(GwtGroupedNVPair model, String property, ColumnData config, int rowIndex,
            int colIndex, ListStore<GwtGroupedNVPair> store, Grid<GwtGroupedNVPair> grid) {
        Object value = model.getValue();
        if (model.getName().equals("expirationDate") && model.getValue().equals("N/A")) {
            return MSGS.never();
        } else if (value != null && value instanceof Date) {
            Date dateValue = (Date) value;
            return DateUtils.formatDateTime(dateValue);
        } else {
            return value;
        }
    }
}
