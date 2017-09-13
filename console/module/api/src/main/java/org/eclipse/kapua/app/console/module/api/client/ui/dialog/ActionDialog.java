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
package org.eclipse.kapua.app.console.module.api.client.ui.dialog;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenServiceAsync;

public abstract class ActionDialog extends KapuaDialog {

    protected static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    protected final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    protected GwtXSRFToken xsrfToken;

    protected static final int FORM_LABEL_WIDTH = 120;
    protected FormPanel formPanel;

    protected Button submitButton;
    protected Button cancelButton;
    protected Status status;

    protected Boolean exitStatus;
    protected String exitMessage;

    public ActionDialog() {
        super();
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        FormLayout formLayout = new FormLayout();
        formLayout.setLabelWidth(FORM_LABEL_WIDTH);

        formPanel = new FormPanel();
        formPanel.setPadding(0);
        formPanel.setFrame(false);
        formPanel.setHeaderVisible(false);
        formPanel.setBodyBorder(false);
        formPanel.setBorders(false);
        formPanel.setLayout(formLayout);
        formPanel.setEncoding(Encoding.MULTIPART);
        formPanel.setMethod(Method.POST);

        addListeners();

        add(formPanel);

        //
        // Buttons setup
        createButtons();
    }

    /**
     * 
     * Add the form listeners
     * 
     */
    protected abstract void addListeners();

    @Override
    public void createButtons() {
        super.createButtons();

        status = new Status();
        status.setBusy(MSGS.waitMsg());
        status.hide();
        status.setAutoWidth(true);
        getButtonBar().add(status);

        getButtonBar().add(new FillToolItem());

        submitButton = new Button(getSubmitButtonText());
        submitButton.setSize(60, 25);
        submitButton.setStyleAttribute("margin-right", "2px");
        submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                preSubmit();
            }
        });

        cancelButton = new Button(getCancelButtonText());
        cancelButton.setSize(60, 25);
        cancelButton.setStyleAttribute("margin-left", "3px");
        cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                exitStatus = null;
                hide();
            }
        });

        addButton(submitButton);
        addButton(cancelButton);
    }

    protected String getSubmitButtonText() {
        return MSGS.submitButton();
    }

    protected String getCancelButtonText() {
        return MSGS.cancelButton();
    }

    protected void preSubmit() {
        gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

            @Override
            public void onFailure(Throwable ex) {
                FailureHandler.handle(ex);
            }

            @Override
            public void onSuccess(GwtXSRFToken xsrfToken) {
                setXsrfToken(xsrfToken);

                mask();
                submitButton.disable();
                cancelButton.disable();
                status.show();

                submit();
            }
        });
    }

    public void setXsrfToken(GwtXSRFToken xsrfToken) {
        this.xsrfToken = xsrfToken;
    }

    public abstract void submit();

    public Boolean getExitStatus() {
        return exitStatus;
    }

    public String getExitMessage() {
        return exitMessage;
    }

    public void maskDialog() {
        formPanel.mask(MSGS.loading());
    }

    public void unmaskDialog() {
        formPanel.unmask();
    }
}
