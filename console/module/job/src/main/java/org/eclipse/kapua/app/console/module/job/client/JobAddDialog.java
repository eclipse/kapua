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
package org.eclipse.kapua.app.console.module.job.client;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobCreator;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

public class JobAddDialog extends EntityAddEditDialog {

    protected TextField<String> name;
    protected TextField<String> description;

    private static final GwtJobServiceAsync GWT_JOB_SERVICE = GWT.create(GwtJobService.class);

    protected static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    public JobAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 200);
    }

    @Override
    public void createBody() {
        FormPanel jobFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        name = new TextField<String>();
        name.setAllowBlank(false);
        name.setName("name");
        name.setFieldLabel("* " + JOB_MSGS.dialogAddFieldName());
        jobFormPanel.add(name);

        description = new TextField<String>();
        description.setAllowBlank(true);
        description.setName("description");
        description.setFieldLabel(JOB_MSGS.dialogAddFieldDescription());
        jobFormPanel.add(description);

        bodyPanel.add(jobFormPanel);
    }

    @Override
    public void submit() {
        GwtJobCreator gwtJobCreator = new GwtJobCreator();

        gwtJobCreator.setScopeId(currentSession.getSelectedAccountId());

        gwtJobCreator.setName(name.getValue());
        gwtJobCreator.setDescription(description.getValue());

        GWT_JOB_SERVICE.create(xsrfToken, gwtJobCreator, new AsyncCallback<GwtJob>() {

            @Override
            public void onSuccess(GwtJob arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                exitStatus = false;
                exitMessage = JOB_MSGS.dialogAddError(cause.getLocalizedMessage());

                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogAddInfo();
    }
}
