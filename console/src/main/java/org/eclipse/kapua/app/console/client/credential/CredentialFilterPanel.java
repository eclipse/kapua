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
package org.eclipse.kapua.app.console.client.credential;

import org.eclipse.kapua.app.console.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredential;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredentialQuery;
import org.eclipse.kapua.app.console.shared.model.authentication.GwtCredentialType;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;

public class CredentialFilterPanel extends EntityFilterPanel<GwtCredential> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private final static ConsoleCredentialMessages CREDENTIAL_MSGS = GWT.create(ConsoleCredentialMessages.class);
    private final int WIDTH = 200;

    private final EntityGrid<GwtCredential> entityGrid;
    private final GwtSession currentSession;

    private final TextField<String> nameField;
    private final SimpleComboBox<GwtCredentialType> credentialType;

    public CredentialFilterPanel(EntityView<GwtCredential> entityView, GwtSession currentSession) {
        super(entityView, currentSession);

        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setHeading(CREDENTIAL_MSGS.filterHeader());

        VerticalPanel fieldsPanel = getFieldsPanel();

        final Label credentialNameLabel = new Label(CREDENTIAL_MSGS.filterFieldCredentialNameLabel());
        credentialNameLabel.setWidth(WIDTH);
        credentialNameLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(credentialNameLabel);

        nameField = new TextField<String>();
        nameField.setName("name");
        nameField.setWidth(WIDTH);
        nameField.setStyleAttribute("margin-top", "0px");
        nameField.setStyleAttribute("margin-left", "5px");
        nameField.setStyleAttribute("margin-right", "5px");
        nameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(nameField);

        final Label credentialTypeLabel = new Label(CREDENTIAL_MSGS.filterFieldCredentialTypeLabel());
        credentialTypeLabel.setWidth(WIDTH);
        credentialTypeLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(credentialTypeLabel);

        credentialType = new SimpleComboBox<GwtCredentialType>();
        credentialType.setEditable(false);
        credentialType.setTypeAhead(false);
        credentialType.setTriggerAction(ComboBox.TriggerAction.ALL);
        credentialType.add(GwtCredentialType.ALL);
        credentialType.add(GwtCredentialType.PASSWORD);
        credentialType.add(GwtCredentialType.API_KEY);
        credentialType.setWidth(WIDTH);
        credentialType.setStyleAttribute("margin-top", "0px");
        credentialType.setStyleAttribute("margin-left", "5px");
        credentialType.setStyleAttribute("margin-right", "5px");
        credentialType.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(credentialType);

    }

    @Override
    public void resetFields() {
        nameField.setValue(null);
        GwtCredentialQuery query = new GwtCredentialQuery();
        query.setScopeId(currentSession.getSelectedAccount().getId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtCredentialQuery query = new GwtCredentialQuery();
        query.setUsername(nameField.getValue());
        query.setType(credentialType.getValue().getValue());
        query.setScopeId(currentSession.getSelectedAccount().getId());
        entityGrid.refresh(query);
    }

}
