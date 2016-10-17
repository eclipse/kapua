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
package org.eclipse.kapua.app.console.client.device;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.Constants;
import org.eclipse.kapua.app.console.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceTabCommand extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final static String SERVLET_URL = "console/file/command";

    private final static int COMMAND_TIMEOUT_SECS = 60;

    // XSRF
    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);
    private HiddenField<String> xsrfTokenField;

    /**
     * 
     */
    private boolean m_dirty;
    private boolean m_initialized;
    private GwtDevice m_selectedDevice;

    private LayoutContainer m_commandInput;
    private FormPanel m_formPanel;
    private HiddenField<String> m_accountField;
    private HiddenField<String> m_deviceIdField;
    private HiddenField<Integer> m_timeoutField;

    private FileUploadField m_fileUploadField;
    private TextField<String> m_commandField;
    private TextField<String> m_passwordField;

    private ButtonBar m_buttonBar;
    private Button m_executeButton;
    private Button m_resetButton;

    private LayoutContainer m_commandOutput;
    private TextArea m_result;

    protected boolean resetProcess;

    public DeviceTabCommand(GwtSession currentSession) {
        m_dirty = false;
        m_initialized = false;
    }

    public void setDevice(GwtDevice selectedDevice) {
        m_dirty = true;
        m_selectedDevice = selectedDevice;
    }

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        // init components
        initCommandInput();
        initCommandOutput();

        ContentPanel devicesCommandPanel = new ContentPanel();
        devicesCommandPanel.setBorders(false);
        devicesCommandPanel.setBodyBorder(false);
        devicesCommandPanel.setHeaderVisible(false);
        devicesCommandPanel.setScrollMode(Scroll.AUTO);
        devicesCommandPanel.setLayout(new FitLayout());

        devicesCommandPanel.setTopComponent(m_commandInput);
        devicesCommandPanel.add(m_commandOutput);

        add(devicesCommandPanel);
        m_initialized = true;
    }

    private void initCommandInput() {
        FormData formData = new FormData("-20");

        FormLayout layout = new FormLayout();
        layout.setLabelWidth(Constants.LABEL_WIDTH_FORM);

        FieldSet fieldSet = new FieldSet();
        fieldSet.setBorders(false);
        fieldSet.setLayout(layout);
        fieldSet.setStyleAttribute("margin", "0px");
        fieldSet.setStyleAttribute("padding", "0px");

        m_formPanel = new FormPanel();
        m_formPanel.setFrame(true);
        m_formPanel.setHeaderVisible(false);
        m_formPanel.setBorders(false);
        m_formPanel.setBodyBorder(false);
        m_formPanel.setAction(SERVLET_URL);
        m_formPanel.setEncoding(Encoding.MULTIPART);
        m_formPanel.setMethod(Method.POST);
        m_formPanel.add(fieldSet);
        m_formPanel.addListener(Events.Render, new Listener<BaseEvent>() {

            public void handleEvent(BaseEvent be) {
                NodeList<com.google.gwt.dom.client.Element> nl = m_formPanel.getElement().getElementsByTagName("form");
                if (nl.getLength() > 0) {
                    com.google.gwt.dom.client.Element elemForm = nl.getItem(0);
                    elemForm.setAttribute("autocomplete", "off");
                }
            }
        });

        m_formPanel.setButtonAlign(HorizontalAlignment.RIGHT);
        m_buttonBar = m_formPanel.getButtonBar();
        initButtonBar();

        m_formPanel.addListener(Events.BeforeSubmit, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (!m_selectedDevice.isOnline()) {
                    MessageBox.alert(MSGS.alerts(), MSGS.deviceOffline(), new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            m_commandInput.unmask();
                        }
                    });
                    be.setCancelled(true);
                }
            }
        });

        m_formPanel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                String htmlResult = be.getResultHtml();

                //
                // Some browsers will return <pre> (i.e. Firefox)
                // Other add styles and return <pre {some style}> (i.e. Safari, Chrome)
                //
                if (!htmlResult.startsWith("<pre")) {
                    int errorMessageStartIndex = htmlResult.indexOf("<pre");
                    errorMessageStartIndex = htmlResult.indexOf(">", errorMessageStartIndex) + 1;
                    int errorMessageEndIndex = htmlResult.indexOf("</pre>");

                    String errorMessage = htmlResult.substring(errorMessageStartIndex, errorMessageEndIndex);

                    MessageBox.alert(MSGS.error(), MSGS.fileUploadFailure() + ":<br/>" + errorMessage, null);
                    m_commandInput.unmask();
                } else {
                    int outputMessageStartIndex = htmlResult.indexOf("<pre");
                    outputMessageStartIndex = htmlResult.indexOf(">", outputMessageStartIndex) + 1;
                    int outputMessageEndIndex = htmlResult.indexOf("</pre>");

                    String output = htmlResult.substring(outputMessageStartIndex, outputMessageEndIndex);

                    m_result.setValue(KapuaSafeHtmlUtils.htmlUnescape(output));
                    m_commandInput.unmask();
                }
            }
        });

        m_accountField = new HiddenField<String>();
        m_accountField.setName("scopeIdString");
        fieldSet.add(m_accountField);

        m_deviceIdField = new HiddenField<String>();
        m_deviceIdField.setName("deviceIdString");
        fieldSet.add(m_deviceIdField);

        m_timeoutField = new HiddenField<Integer>();
        m_timeoutField.setName("timeout");
        fieldSet.add(m_timeoutField);

        //
        // xsrfToken Hidden field
        //
        xsrfTokenField = new HiddenField<String>();
        xsrfTokenField.setId("xsrfToken");
        xsrfTokenField.setName("xsrfToken");
        xsrfTokenField.setValue("");
        fieldSet.add(xsrfTokenField);

        m_commandField = new TextField<String>();
        m_commandField.setName("command");
        m_commandField.setAllowBlank(false);
        m_commandField.setFieldLabel("* " + MSGS.deviceCommandExecute());
        m_commandField.setLayoutData(layout);
        fieldSet.add(m_commandField, formData);

        m_fileUploadField = new FileUploadField();
        m_fileUploadField.setAllowBlank(true);
        m_fileUploadField.setName("file");
        m_fileUploadField.setLayoutData(layout);
        m_fileUploadField.setFieldLabel(MSGS.deviceCommandFile());
        fieldSet.add(m_fileUploadField, formData);

        m_passwordField = new TextField<String>();
        m_passwordField.setName("password");
        m_passwordField.setFieldLabel(MSGS.deviceCommandPassword());
        m_passwordField.setToolTip(MSGS.deviceCommandPasswordTooltip());
        m_passwordField.setPassword(true);
        m_passwordField.setLayoutData(layout);
        fieldSet.add(m_passwordField, formData);

        m_commandInput = m_formPanel;

        m_commandInput.mask(MSGS.deviceNoDeviceSelected());
    }

    private void initButtonBar() {
        m_executeButton = new Button(MSGS.deviceCommandExecute());
        m_executeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (m_formPanel.isValid()) {
                    m_result.clear();
                    m_commandInput.mask(MSGS.deviceCommandExecuting());
                    m_accountField.setValue(m_selectedDevice.getScopeId());
                    m_deviceIdField.setValue(m_selectedDevice.getId());
                    m_timeoutField.setValue(COMMAND_TIMEOUT_SECS);

                    gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                        @Override
                        public void onFailure(Throwable ex) {
                            FailureHandler.handle(ex);
                        }

                        @Override
                        public void onSuccess(GwtXSRFToken token) {
                            xsrfTokenField.setValue(token.getToken());
                            m_formPanel.submit();
                        }
                    });
                }
            }
        });

        m_resetButton = new Button(MSGS.reset());
        m_resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!resetProcess) {
                    resetProcess = true;
                    m_resetButton.setEnabled(false);

                    m_formPanel.reset();

                    m_resetButton.setEnabled(true);
                    resetProcess = false;
                }
            }
        });

        m_buttonBar.add(m_resetButton);
        m_buttonBar.add(m_executeButton);
    }

    private void initCommandOutput() {
        m_commandOutput = new LayoutContainer();
        m_commandOutput.setBorders(false);
        m_commandOutput.setWidth("99.5%");
        m_commandOutput.setLayout(new FitLayout());

        m_result = new TextArea();
        m_result.setBorders(false);
        m_result.setReadOnly(true);
        m_result.setEmptyText(MSGS.deviceCommandNoOutput());
        m_result.setBorders(false);
        m_commandOutput.add(m_result);
    }

    // --------------------------------------------------------------------------------------
    //
    // Device Configuration Management
    //
    // --------------------------------------------------------------------------------------

    public void refresh() {
        if (m_dirty && m_initialized) {
            m_dirty = false;

            if (m_selectedDevice != null) {
                m_commandInput.unmask();
            }
        }
    }

    // --------------------------------------------------------------------------------------
    //
    // Unload of the GXT Component
    //
    // --------------------------------------------------------------------------------------

    public void onUnload() {
        super.onUnload();
    }

}
