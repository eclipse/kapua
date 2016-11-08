/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.client.ui.dialog;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;

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

public abstract class ActionDialog extends KapuaDialog {

    protected static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    protected final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    protected GwtXSRFToken xsrfToken;

    protected static int FORM_LABEL_WIDTH = 120;
    protected FormPanel m_formPanel;

    protected Button m_submitButton;
    protected Button m_cancelButton;
    protected Status m_status;

    protected Boolean m_exitStatus;
    protected String m_exitMessage;

    public ActionDialog() {
        super();
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        FormLayout formLayout = new FormLayout();
        formLayout.setLabelWidth(FORM_LABEL_WIDTH);

        m_formPanel = new FormPanel();
        m_formPanel.setPadding(0);
        m_formPanel.setFrame(false);
        m_formPanel.setHeaderVisible(false);
        m_formPanel.setBodyBorder(false);
        m_formPanel.setBorders(false);
        m_formPanel.setLayout(formLayout);
        m_formPanel.setEncoding(Encoding.MULTIPART);
        m_formPanel.setMethod(Method.POST);

        addListeners();

        add(m_formPanel);

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

        m_status = new Status();
        m_status.setBusy(MSGS.waitMsg());
        m_status.hide();
        m_status.setAutoWidth(true);
        getButtonBar().add(m_status);

        getButtonBar().add(new FillToolItem());

        m_submitButton = new Button(getSubmitButtonText());
        m_submitButton.setSize(60, 25);
        m_submitButton.setStyleAttribute("margin-right", "2px");
        m_submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                preSubmit();
            }
        });

        m_cancelButton = new Button(getCancelButtonText());
        m_cancelButton.setSize(60, 25);
        m_cancelButton.setStyleAttribute("margin-left", "3px");
        m_cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                m_exitStatus = null;
                hide();
            }
        });

        addButton(m_submitButton);
        addButton(m_cancelButton);
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
                m_submitButton.disable();
                m_cancelButton.disable();
                m_status.show();

                submit();
            }
        });
    }

    public void setXsrfToken(GwtXSRFToken xsrfToken) {
        this.xsrfToken = xsrfToken;
    }

    public abstract void submit();

    public Boolean getExitStatus() {
        return m_exitStatus;
    }

    public String getExitMessage() {
        return m_exitMessage;
    }
}
