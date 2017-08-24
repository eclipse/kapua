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

import java.util.List;

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
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenServiceAsync;

public class FileUploadDialog extends Dialog {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    // XSRF
    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);
    private HiddenField<String> xsrfTokenField;

    private FormPanel formPanel;
    private FileUploadField fileUploadField;
    private List<HiddenField<?>> hiddenFields;
    private Button submitButton;
    private Button cancelButton;
    private Status status;
    private String url;

    public FileUploadDialog(String url, List<HiddenField<?>> hiddenFields) {
        super();
        this.url = url;
        this.hiddenFields = hiddenFields;
        setButtonAlign(HorizontalAlignment.RIGHT);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        setLayout(new FormLayout());
        setBodyBorder(false);
        // setButtonAlign(HorizontalAlignment.CENTER); // Must be called beforehand
        setModal(true);
        setButtons("");
        setAutoWidth(true);
        setScrollMode(Scroll.AUTO);
        setHideOnButtonClick(false);

        formPanel = new FormPanel();
        formPanel.setFrame(false);
        formPanel.setHeaderVisible(false);
        formPanel.setBodyBorder(false);
        formPanel.setAction(url);
        formPanel.setEncoding(Encoding.MULTIPART);
        formPanel.setMethod(Method.POST);
        formPanel.setButtonAlign(HorizontalAlignment.CENTER);

        formPanel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                String htmlResponse = be.getResultHtml();
                if (htmlResponse == null || htmlResponse.isEmpty()) {
                    MessageBox.info(MSGS.information(), MSGS.fileUploadSuccess(), null);
                } else {
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

        fileUploadField = new FileUploadField();
        fileUploadField.setAllowBlank(false);
        fileUploadField.setName("uploadedFile");
        fileUploadField.setFieldLabel("File");

        formPanel.add(fileUploadField);
        if (hiddenFields != null) {
            for (HiddenField<?> hf : hiddenFields) {
                formPanel.add(hf);
            }
        }

        //
        // xsrfToken Hidden field
        //
        gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

            @Override
            public void onFailure(Throwable ex) {
                FailureHandler.handle(ex);
            }

            @Override
            public void onSuccess(GwtXSRFToken token) {
                xsrfTokenField.setValue(token.getToken());
            }
        });

        xsrfTokenField = new HiddenField<String>();
        xsrfTokenField.setId("xsrfToken");
        xsrfTokenField.setName("xsrfToken");
        xsrfTokenField.setValue("");

        formPanel.add(xsrfTokenField);
        //

        add(formPanel);
    }

    @Override
    protected void createButtons() {
        super.createButtons();

        status = new Status();
        status.setBusy(MSGS.waitMsg());
        status.hide();
        status.setAutoWidth(true);
        getButtonBar().add(status);

        getButtonBar().add(new FillToolItem());

        submitButton = new Button(MSGS.uploadButton());
        submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                submit();
            }
        });

        cancelButton = new Button(MSGS.cancelButton());
        cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });

        addButton(cancelButton);
        addButton(submitButton);
    }

    private void submit() {
        if (!formPanel.isValid()) {
            return;
        }
        submitButton.disable();
        cancelButton.disable();
        status.show();
        formPanel.submit();
    }
}
