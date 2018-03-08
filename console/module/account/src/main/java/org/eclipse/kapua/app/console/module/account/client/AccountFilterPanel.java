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
package org.eclipse.kapua.app.console.module.account.client;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.account.client.messages.ConsoleAccountMessages;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountQuery;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountFilterPanel extends EntityFilterPanel<GwtAccount> {

    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;
    private static final ConsoleAccountMessages MSGS = GWT.create(ConsoleAccountMessages.class);

    private EntityGrid<GwtAccount> entityGrid;
    private final GwtSession currentSession;

    private final KapuaTextField<String> accountNameField;
    private final KapuaTextField<String> accountOrgNameField;
    private final KapuaTextField<String> accountOrgEmailField;

    public AccountFilterPanel(AbstractEntityView<GwtAccount> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setHeading(MSGS.filterHeader());

        VerticalPanel fieldsPanel = getFieldsPanel();

        Label accountNameLabel = new Label(MSGS.accountFilterNameLabel());
        accountNameLabel.setWidth(WIDTH);
        accountNameLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(accountNameLabel);

        accountNameField = new KapuaTextField<String>();
        accountNameField.setName("name");
        accountNameField.setWidth(WIDTH);
        accountNameField.setMaxLength(MAX_LEN);
        accountNameField.setStyleAttribute("margin-top", "0px");
        accountNameField.setStyleAttribute("margin-left", "5px");
        accountNameField.setStyleAttribute("margin-right", "5px");
        accountNameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(accountNameField);

        Label accountOrgNameLabel = new Label(MSGS.accountFilterOrgNameLabel());
        accountOrgNameLabel.setWidth(WIDTH);
        accountOrgNameLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(accountOrgNameLabel);

        accountOrgNameField = new KapuaTextField<String>();
        accountOrgNameField.setName("orgName");
        accountOrgNameField.setWidth(WIDTH);
        accountOrgNameField.setMaxLength(MAX_LEN);
        accountOrgNameField.setStyleAttribute("margin-top", "0px");
        accountOrgNameField.setStyleAttribute("margin-left", "5px");
        accountOrgNameField.setStyleAttribute("margin-right", "5px");
        accountOrgNameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(accountOrgNameField);

        Label accountOrgEmailLabel = new Label(MSGS.accountFilterOrgEmailLabel());
        accountOrgEmailLabel.setWidth(WIDTH);
        accountOrgEmailLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(accountOrgEmailLabel);

        accountOrgEmailField = new KapuaTextField<String>();
        accountOrgEmailField.setName("name");
        accountOrgEmailField.setWidth(WIDTH);
        accountOrgEmailField.setMaxLength(MAX_LEN);
        accountOrgEmailField.setStyleAttribute("margin-top", "0px");
        accountOrgEmailField.setStyleAttribute("margin-left", "5px");
        accountOrgEmailField.setStyleAttribute("margin-right", "5px");
        accountOrgEmailField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(accountOrgEmailField);

    }

    @Override
    public void resetFields() {
        accountNameField.setValue(null);
        accountOrgEmailField.setValue(null);
        accountOrgNameField.setValue(null);
        GwtAccountQuery query = new GwtAccountQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtAccountQuery query = new GwtAccountQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        query.setName(accountNameField.getValue());
        query.setOrganizationName(accountOrgNameField.getValue());
        query.setOrganizationEmail(accountOrgEmailField.getValue());
        entityGrid.refresh(query);
    }

}
