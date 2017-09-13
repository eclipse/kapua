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
package org.eclipse.kapua.app.console.core.client;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.core.shared.service.GwtAuthorizationService;
import org.eclipse.kapua.app.console.core.shared.service.GwtSettingsService;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.core.shared.model.authentication.GwtLoginCredential;
import org.eclipse.kapua.app.console.core.shared.service.GwtAuthorizationServiceAsync;
import org.eclipse.kapua.app.console.core.shared.service.GwtSettingsServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * Login Dialog
 *
 * Two-step verification - First step: username and password / cookies verification
 *
 */
public class LoginDialog extends Dialog {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private final GwtAuthorizationServiceAsync gwtAuthorizationService = GWT.create(GwtAuthorizationService.class);
    private final GwtSettingsServiceAsync gwtSettingService = GWT.create(GwtSettingsService.class);

    public GwtSession currentSession;
    public TextField<String> username;
    public TextField<String> password;

    public Button reset;
    public Button login;
    public Button ssoLogin;
    public Status status;

    private boolean allowMainScreen;

    public void setAllowMainScreen(boolean main) {
        this.allowMainScreen = main;
    }

    public boolean isAllowMainScreen() {
        return this.allowMainScreen;
    }

    public LoginDialog() {
        FormLayout layout = new FormLayout();

        layout.setLabelWidth(90);
        layout.setDefaultWidth(160);
        setLayout(layout);

        setButtonAlign(HorizontalAlignment.LEFT);
        setButtons(""); // don't show OK button
        setIcon(IconHelper.createStyle("user"));
        setHeading(MSGS.loginTitle());
        setModal(false);
        setBodyBorder(true);
        setBodyStyle("padding: 8px;background: none");
        setWidth(300);
        setResizable(false);
        setClosable(false);

        KeyListener keyListener = new KeyListener() {

            public void componentKeyUp(ComponentEvent event) {
                validate();
                if (event.getKeyCode() == 13) {
                    if (username.getValue() != null && username.getValue().trim().length() > 0 &&
                            password.getValue() != null && password.getValue().trim().length() > 0) {
                        onSubmit();
                    }
                }
            }
        };

        Listener<BaseEvent> changeListener = new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                validate();
            }
        };

        username = new TextField<String>();
        username.setFieldLabel(MSGS.loginUsername());
        username.addKeyListener(keyListener);
        username.addListener(Events.OnBlur, changeListener);

        add(username);

        password = new TextField<String>();
        password.setPassword(true);
        password.setFieldLabel(MSGS.loginPassword());
        password.addKeyListener(keyListener);
        password.addListener(Events.OnBlur, changeListener);

        add(password);

        setFocusWidget(username);

        gwtSettingService.getSsoEnabled(new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                ConsoleInfo.display(MSGS.loginSsoEnabledError(), caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                ssoLogin.setVisible(result);
            }
        });

    }

    public GwtSession getCurrentSession() {
        return currentSession;
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

        reset = new Button(MSGS.loginReset());
        reset.addSelectionListener(new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                username.reset();
                password.reset();
                validate();
                username.focus();
            }
        });

        login = new Button(MSGS.loginLogin());
        login.disable();
        login.addSelectionListener(new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                onSubmit();
            }
        });

        ssoLogin = new Button(MSGS.loginSsoLogin());
        ssoLogin.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                doSsoLogin();
            }

        });

        addButton(reset);
        addButton(login);
        addButton(ssoLogin);
    }

    protected void doSsoLogin() {
        gwtSettingService.getSsoLoginUri(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ConsoleInfo.display(MSGS.loginSsoLogin(), caught.getLocalizedMessage());
            }

            @Override
            public void onSuccess(String result) {
                Window.Location.assign(result);
            }

        });
    }

    /**
     *
     * Login submit
     *
     */
    protected void onSubmit() {
        status.show();
        getButtonBar().disable();

        performLogin();
    }

    // Login
    public void performLogin() {

        final GwtLoginCredential credentials = new GwtLoginCredential(username.getValue(), password.getValue());

        // FIXME: use some Credentials object instead of using GwtUser!
        gwtAuthorizationService.login(credentials, new AsyncCallback<GwtSession>() {

            @Override
            public void onFailure(Throwable caught) {
                ConsoleInfo.display(MSGS.loginError(), caught.getLocalizedMessage());
                reset();
            }

            @Override
            public void onSuccess(final GwtSession gwtSession) {
                currentSession = gwtSession;
                callMainScreen();
            }
        });
    }

    public void performLogout() {
        gwtAuthorizationService.logout(new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(Void arg0) {
                ConsoleInfo.display(MSGS.popupInfo(), MSGS.loggedOut());
                reset();
                show();
            }
        });
    }

    public void callMainScreen() {
        setAllowMainScreen(true);
        hide();
    }

    protected boolean hasValue(TextField<String> field) {
        return field.getValue() != null &&
                !field.getValue().isEmpty();
    }

    protected void validate() {
        login.setEnabled(hasValue(username) &&
                username.isValid() &&
                hasValue(password));
    }

    public void reset() {
        username.reset();
        password.reset();

        validate();
        username.focus();
        status.hide();
        getButtonBar().enable();
    }

}
