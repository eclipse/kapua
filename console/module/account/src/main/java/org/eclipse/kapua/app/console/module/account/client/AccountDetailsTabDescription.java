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
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.account.client.toolbar.AccountEditDialog;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.permission.AccountSessionPermission;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountServiceAsync;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.button.KapuaButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.EditButton;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.EntityDescriptionTabItem;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountDetailsTabDescription extends EntityDescriptionTabItem<GwtAccount> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private GwtAccountServiceAsync gwtAccountService = GWT.create(GwtAccountService.class);

    public AccountDetailsTabDescription(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected RpcProxy<ListLoadResult<GwtGroupedNVPair>> getDataProxy() {
        return new RpcProxy<ListLoadResult<GwtGroupedNVPair>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtGroupedNVPair>> callback) {
                gwtAccountService.getAccountInfo(currentSession.getSelectedAccountId(), getSelectedEntity().getId(), callback);
            }
        };
    }

    @Override
    protected ToolBar getToolbar() {

        ToolBar accountsToolBar = new ToolBar();
        accountsToolBar.setHeight("27px");

        //
        // Edit Account Button
        KapuaButton editButton = new EditButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (getSelectedEntity() != null) {
                    AccountEditDialog accountForm = new AccountEditDialog(currentSession, getSelectedEntity());
                    accountForm.addListener(Events.Hide, new Listener<ComponentEvent>() {

                        @Override
                        public void handleEvent(ComponentEvent be) {
                            setDirty(true);
                            refresh();
                        }
                    });
                    accountForm.show();
                }
            }
        });

        accountsToolBar.add(editButton);
        accountsToolBar.add(new SeparatorToolItem());

        if (!currentSession.hasPermission(AccountSessionPermission.write())) {
            editButton.disable();
        }
        return accountsToolBar;
    }

    @Override
    protected void doRefresh() {
        super.doRefresh();
        gwtAccountService.find(getSelectedEntity().getId(), new AsyncCallback<GwtAccount>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(GwtAccount result) {
                setEntity(result);
            }
        });
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setBorders(false);
    }
}
