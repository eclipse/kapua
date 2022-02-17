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
package org.eclipse.kapua.app.console.module.api.client.ui.dialog;

import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenServiceAsync;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.Constants;

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
        setClosable(false);
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
        setResizable(false);
        setHideOnButtonClick(false);

        formPanel = new FormPanel();
        formPanel.setFrame(false);
        formPanel.setHeaderVisible(false);
        formPanel.setBodyBorder(false);
        formPanel.setAction(url);
        formPanel.setEncoding(Encoding.MULTIPART);
        formPanel.setMethod(Method.POST);
        formPanel.setButtonAlign(HorizontalAlignment.CENTER);
        formPanel.setWidth(Constants.FORM_PANEL_WIDTH_UPLOAD_AND_APPLY);

        formPanel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                String htmlResponse = be.getResultHtml();
                if (htmlResponse == null || htmlResponse.isEmpty()) {
                    ConsoleInfo.display(MSGS.information(), MSGS.fileUploadSuccess());
                } else {
                    String errMsg = htmlResponse;
                    int startIdx = htmlResponse.indexOf("<pre>");
                    int endIndex = htmlResponse.indexOf("</pre>");
                    if (startIdx != -1 && endIndex != -1) {
                        errMsg = htmlResponse.substring(startIdx + 5, endIndex);
                        if (("xmlDeviceConfig").equals(errMsg)) {
                            errMsg = MSGS.fileUploadInvalidShapshotFailure();
                        }
                    }
                    ConsoleInfo.display(MSGS.error(), MSGS.fileUploadFailure() + ": " + errMsg);
                }
                hide();
            }
        });
        FormLayout layout = new FormLayout();
        layout.setLabelWidth(Constants.LABEL_WIDTH_UPLOAD_AND_APPLY);
        FieldSet fieldSet = new FieldSet();
        fieldSet.setBorders(false);
        fieldSet.setLayout(layout);

        fileUploadField = new FileUploadField();
        fileUploadField.setAccept("application/xml");
        fileUploadField.setValidator(new TextFieldValidator(fileUploadField, FieldType.SNAPSHOT_FILE));
        fileUploadField.setAllowBlank(false);
        fileUploadField.setName("uploadedFile");
        fileUploadField.setFieldLabel("File");
        fileUploadField.setInputStyleAttribute("style", "width:300");
        fileUploadField.addListener(Events.OnChange, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (fileUploadField.isDirty()){
                    submitButton.enable();
                } else {
                    submitButton.disable();
                }
            }
        });
        FormData formData = new FormData("-20");
        fieldSet.add(fileUploadField, formData);

        formPanel.add(fieldSet);
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

        submitButton.disable();
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

    public FileUploadField getFileUploadField() {
        return fileUploadField;
    }
}
