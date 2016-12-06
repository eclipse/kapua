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

import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FileUploadDialog extends Dialog
{

    private static final ConsoleMessages       MSGS           = GWT.create(ConsoleMessages.class);

    // XSRF
    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);
    private HiddenField<String>                xsrfTokenField;

    private FormPanel                          m_formPanel;
    private FileUploadField                    m_fileUploadField;
    private List<HiddenField<?>>               m_hiddenFields;
    private Button                             m_submitButton;
    private Button                             m_cancelButton;
    private Status                             m_status;
    private String                             m_url;

    public FileUploadDialog(String url, List<HiddenField<?>> hiddenFields)
    {
        super();
        m_url = url;
        m_hiddenFields = hiddenFields;
        setButtonAlign(HorizontalAlignment.RIGHT);
    }

    @Override
    protected void onRender(Element parent, int pos)
    {
        super.onRender(parent, pos);

        setLayout(new FormLayout());
        setBodyBorder(false);
        // setButtonAlign(HorizontalAlignment.CENTER); // Must be called beforehand
        setModal(true);
        setButtons("");
        setAutoWidth(true);
        setScrollMode(Scroll.AUTO);
        setHideOnButtonClick(false);

        m_formPanel = new FormPanel();
        m_formPanel.setFrame(false);
        m_formPanel.setHeaderVisible(false);
        m_formPanel.setBodyBorder(false);
        m_formPanel.setAction(m_url);
        m_formPanel.setEncoding(Encoding.MULTIPART);
        m_formPanel.setMethod(Method.POST);
        m_formPanel.setButtonAlign(HorizontalAlignment.CENTER);

        m_formPanel.addListener(Events.Submit, new Listener<FormEvent>() {
            @Override
            public void handleEvent(FormEvent be)
            {
                String htmlResponse = be.getResultHtml();
                if (htmlResponse == null || htmlResponse.isEmpty()) {
                    MessageBox.info(MSGS.information(), MSGS.fileUploadSuccess(), null);
                }
                else {
                    String errMsg = htmlResponse;
                    int startIdx = htmlResponse.indexOf("<pre>");
                    int endIndex = htmlResponse.indexOf("</pre>");
                    if (startIdx != -1 && endIndex != -1) {
                        errMsg = htmlResponse.substring(startIdx + 5, endIndex);
                    }
                    MessageBox.alert(MSGS.error(), MSGS.fileUploadFailure() + ": " + errMsg, null);
                }
                hide();
            }
        });

        m_fileUploadField = new FileUploadField();
        m_fileUploadField.setAllowBlank(false);
        m_fileUploadField.setName("uploadedFile");
        m_fileUploadField.setFieldLabel("File");

        m_formPanel.add(m_fileUploadField);
        if (m_hiddenFields != null) {
            for (HiddenField<?> hf : m_hiddenFields) {
                m_formPanel.add(hf);
            }
        }

        //
        // xsrfToken Hidden field
        //
        gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {
            @Override
            public void onFailure(Throwable ex)
            {
                FailureHandler.handle(ex);
            }

            @Override
            public void onSuccess(GwtXSRFToken token)
            {
                xsrfTokenField.setValue(token.getToken());
            }
        });

        xsrfTokenField = new HiddenField<String>();
        xsrfTokenField.setId("xsrfToken");
        xsrfTokenField.setName("xsrfToken");
        xsrfTokenField.setValue("");

        m_formPanel.add(xsrfTokenField);
        //

        add(m_formPanel);
    }

    @Override
    protected void createButtons()
    {
        super.createButtons();

        m_status = new Status();
        m_status.setBusy(MSGS.waitMsg());
        m_status.hide();
        m_status.setAutoWidth(true);
        getButtonBar().add(m_status);

        getButtonBar().add(new FillToolItem());

        m_submitButton = new Button(MSGS.uploadButton());
        m_submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce)
            {
                submit();
            }
        });

        m_cancelButton = new Button(MSGS.cancelButton());
        m_cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce)
            {
                hide();
            }
        });

        addButton(m_cancelButton);
        addButton(m_submitButton);
    }

    private void submit()
    {
        if (!m_formPanel.isValid()) {
            return;
        }
        m_submitButton.disable();
        m_cancelButton.disable();
        m_status.show();
        m_formPanel.submit();
    }
}
