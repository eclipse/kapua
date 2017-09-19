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
package org.eclipse.kapua.app.console.module.tag.client;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.tag.client.messages.ConsoleTagMessages;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagCreator;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

public class TagAddDialog extends EntityAddEditDialog {

    private final static GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);
    private final static ConsoleTagMessages MSGS = GWT.create(ConsoleTagMessages.class);

    protected TextField<String> tagNameField;

    public TagAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 200);
    }

    @Override
    public void createBody() {
        FormPanel tagFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        tagNameField = new TextField<String>();
        tagNameField.setAllowBlank(false);
        tagNameField.setFieldLabel("* " + MSGS.dialogAddFieldName());
        tagNameField.setToolTip(MSGS.dialogAddFieldNameTooltip());
        tagFormPanel.add(tagNameField);
        bodyPanel.add(tagFormPanel);
    }

    @Override
    public void submit() {
        GwtTagCreator gwtTagCreator = new GwtTagCreator();
        gwtTagCreator.setScopeId(currentSession.getSelectedAccountId());
        gwtTagCreator.setName(tagNameField.getValue());
        GWT_TAG_SERVICE.create(gwtTagCreator, new AsyncCallback<GwtTag>() {

            @Override
            public void onSuccess(GwtTag arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable arg0) {
                FailureHandler.handleFormException(formPanel, arg0);
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogAddInfo();
    }

}
