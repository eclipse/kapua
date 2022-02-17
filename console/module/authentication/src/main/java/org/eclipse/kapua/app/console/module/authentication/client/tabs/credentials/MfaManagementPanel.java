/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials;

import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.KapuaButton;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.ContentPanel;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenServiceAsync;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtMfaCredentialOptions;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtMfaCredentialOptionsCreator;
import org.eclipse.kapua.app.console.module.authentication.shared.model.permission.CredentialSessionPermission;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtMfaCredentialOptionsService;
import org.eclipse.kapua.app.console.module.authentication.shared.service.GwtMfaCredentialOptionsServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class MfaManagementPanel extends ContentPanel {

    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);
    private static final GwtSecurityTokenServiceAsync GWT_XSRF_SERVICE = GWT.create(GwtSecurityTokenService.class);
    private static final GwtMfaCredentialOptionsServiceAsync GWT_MFA_CREDENTIAL_OPTIONS_SERVICE = GWT.create(GwtMfaCredentialOptionsService.class);

    private final GwtSession currentSession;
    private String userId;
    private String accountId;
    private String username;
    private String accountName;
    private boolean selfManagement;
    private Dialog selfManagementDialog;

    private GwtMfaCredentialOptions gwtMfaCredentialOptions;

    private Text scratchCodesArea;

    // MFA Toolbar buttons
    private KapuaButton enableMfa;
    private KapuaButton forgetTrustedMachine;
    private KapuaButton help;

    private boolean keyEnabled;

    // HELP
    private com.extjs.gxt.ui.client.widget.form.FormPanel helpPanel;
    private boolean toggleHelp;
    private FormData formData;

    private Image barcodeImage;
    private Image androidImage;
    private Image appleImage;

    private Text enabledText;

    private MfaManagementPanel(GwtSession currentSession) {
        setHeaderVisible(false);
        this.currentSession = currentSession;
    }

    public MfaManagementPanel(GwtSession currentSession, String userId, String username, String accountId, String accountName) {
        // Management Panel in Users Tab
        this(currentSession);
        this.selfManagement = false;
        this.userId = userId;
        this.username = username;
        this.accountId = accountId;
        this.accountName = accountName;
    }

    public MfaManagementPanel(GwtSession currentSession, Dialog selfManagementDialog) {
        // Self Management Panel in Dialog
        this(currentSession);
        this.selfManagement = true;
        this.selfManagementDialog = selfManagementDialog;
        this.userId = currentSession.getUserId();
        this.username = currentSession.getUserName();
        this.accountId = currentSession.getAccountId();
        this.accountName = currentSession.getAccountName();
    }

    // Build the MFA toolbar
    protected ToolBar createToolBar() {
        ToolBar menuToolBar = new ToolBar();

        enableMfa = new KapuaButton(MSGS.mfaFormEnableMfa(), new KapuaIcon(IconSet.CHECK), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent myce) {
                GWT_XSRF_SERVICE.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        FailureHandler.handle(caught);
                        doUnmask();
                    }

                    @Override
                    public void onSuccess(final GwtXSRFToken xsrfToken) {
                        getButtonBar().disable();

                        keyEnabled = gwtMfaCredentialOptions != null;
                        if (!keyEnabled) {
                            doMask(MSGS.maskEnableMfa());
                            // MFA is disabled, so enable it
                            final GwtMfaCredentialOptionsCreator gwtMfaCredentialOptionsCreator = new GwtMfaCredentialOptionsCreator();
                            gwtMfaCredentialOptionsCreator.setScopeId(accountId);
                            gwtMfaCredentialOptionsCreator.setUserId(userId);

                            GWT_MFA_CREDENTIAL_OPTIONS_SERVICE.create(xsrfToken, gwtMfaCredentialOptionsCreator, selfManagement, new AsyncCallback<GwtMfaCredentialOptions>() {

                                @Override
                                public void onFailure(Throwable ex) {
                                    FailureHandler.handle(ex);
                                    doUnmask();
                                }

                                @Override
                                public void onSuccess(final GwtMfaCredentialOptions mfaCredentialOptions) {
                                    keyEnabled = true;
                                    try {
                                        MfaManagementPanel.this.gwtMfaCredentialOptions = mfaCredentialOptions;
                                        barcodeImage.setUrl("data:image/png;base64," + mfaCredentialOptions.getQRCodeImage());
                                    } catch (Exception e) {
                                        FailureHandler.handle(e);
                                        doUnmask();
                                    }
                                }
                            });

                        } else {
                            final DisableMfaDialog disableMfaDialog = new DisableMfaDialog(username, accountId, gwtMfaCredentialOptions.getId(), selfManagement);
                            disableMfaDialog.addListener(Events.Hide, new Listener<WindowEvent>() {

                                @Override
                                public void handleEvent(WindowEvent be) {
                                    if (be.getButtonClicked().equals(disableMfaDialog.getButtonById(Dialog.YES))) {
                                        GWT_MFA_CREDENTIAL_OPTIONS_SERVICE.find(accountId, gwtMfaCredentialOptions.getId(), selfManagement, new AsyncCallback<GwtMfaCredentialOptions>() {

                                            @Override
                                            public void onFailure(Throwable caught) {
                                                FailureHandler.handle(caught);
                                            }

                                            @Override
                                            public void onSuccess(GwtMfaCredentialOptions result) {
                                                MfaManagementPanel.this.gwtMfaCredentialOptions = null;
                                                barcodeImage.setVisible(false);
                                                scratchCodesArea.setVisible(false);

                                                updateUIComponents(MfaManagementPanel.this.gwtMfaCredentialOptions);
                                                keyEnabled = false;

                                                doUnmask();
                                            }
                                        });
                                    }
                                }

                            });
                            disableMfaDialog.show();
                        }
                    }
                });
            }
        });

        // Forget Trust Machine button
        forgetTrustedMachine = new KapuaButton(MSGS.mfaFormRevokeTrustedMachine(), new KapuaIcon(IconSet.LOCK), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent myce) {
                final ForgetTrustMachineDialog dialog = new ForgetTrustMachineDialog(username, accountId, gwtMfaCredentialOptions.getId(), selfManagement);
                dialog.addListener(Events.Hide, new Listener<WindowEvent>() {

                    @Override
                    public void handleEvent(WindowEvent be) {
                        if (be.getButtonClicked().equals(dialog.getButtonById(Dialog.YES))) {
                            GWT_MFA_CREDENTIAL_OPTIONS_SERVICE.find(accountId, gwtMfaCredentialOptions.getId(), selfManagement, new AsyncCallback<GwtMfaCredentialOptions>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    FailureHandler.handle(caught);
                                }

                                @Override
                                public void onSuccess(GwtMfaCredentialOptions result) {
                                    gwtMfaCredentialOptions = result;
                                    updateUIComponents(gwtMfaCredentialOptions);
                                }
                            });
                        }
                    }
                });
                dialog.show();
            }

        });

        help = new KapuaButton(MSGS.mfaFormHelp(), new KapuaIcon(IconSet.QUESTION), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                toggleHelp = !toggleHelp;
                if (toggleHelp) {
                    showHelp();
                    help.setText("Hide help");
                } else {
                    hideHelp();
                    help.setText("Help");
                }
            }
        });

        menuToolBar.add(enableMfa);
        menuToolBar.add(new SeparatorToolItem());
        menuToolBar.add(forgetTrustedMachine);
        menuToolBar.add(new SeparatorToolItem());
        menuToolBar.add(help);

        return menuToolBar;
    }

    private void buildHelpPanel() {
        formData = new FormData("-30");

        helpPanel = new com.extjs.gxt.ui.client.widget.form.FormPanel();
        helpPanel.setFrame(false);
        helpPanel.setBodyBorder(false);
        helpPanel.setHeaderVisible(false);
        helpPanel.setAutoHeight(true);

        // Explanation header
        FieldSet helpFieldSet = new FieldSet();
        helpFieldSet.setBorders(true);
        helpFieldSet.setHeading(MSGS.mfaHeaderHelp());

        LabelField text3 = new LabelField();
        text3.setText(MSGS.mfaFormBarcodeLabel3());
        text3.setName("explanation3");

        LabelField text4 = new LabelField();
        text4.setText(MSGS.mfaFormBarcodeLabel4());
        text4.setName("explanation4");

        FormLayout iconsLayout = new FormLayout();

        FieldSet iconsFieldSet = new FieldSet();
        iconsFieldSet.setBorders(false);
        iconsFieldSet.setLayout(iconsLayout);

        // images
        androidImage = new Image("img/mfa/android.png");
        Image blankImage = new Image("img/mfa/blank.png");
        appleImage = new Image("img/mfa/apple.png");

        appleImage.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                com.google.gwt.user.client.Window.open("https://itunes.apple.com/us/app/google-authenticator/id388497605?mt=8&uo=4", "_blank", "");
            }
        });

        appleImage.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                appleImage.getElement().getStyle().setCursor(Cursor.POINTER);
            }
        });

        androidImage.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                com.google.gwt.user.client.Window.open("https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2", "_blank", "");
            }
        });

        androidImage.addMouseOverHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                androidImage.getElement().getStyle().setCursor(Cursor.POINTER);
            }
        });

        iconsFieldSet.add(androidImage);
        iconsFieldSet.add(blankImage);
        iconsFieldSet.add(appleImage);

        HorizontalPanel iconsPanel = new HorizontalPanel();
        iconsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        iconsPanel.add(iconsFieldSet);

        //
        LabelField text5 = new LabelField(MSGS.mfaFormBarcodeLabel5());
        text5.setName("explanation5");

        LabelField text6 = new LabelField(MSGS.mfaFormBarcodeLabel6());
        text6.setName("explanation6");

        LabelField text7 = new LabelField(MSGS.mfaFormBarcodeLabel7());
        text7.setName("explanation7");

        //
        helpFieldSet.add(text3, formData);
        helpFieldSet.add(text4, formData);

        helpFieldSet.add(iconsPanel, formData);

        helpFieldSet.add(text5, formData);
        helpFieldSet.add(text6, formData);
        helpFieldSet.add(text7, formData);

        helpPanel.add(helpFieldSet);
    }

    private void showHelp() {
        helpPanel.show();
    }

    private void hideHelp() {
        helpPanel.hide();
    }

    private void printScratchCodes(List<String> scratchCodes) {

        StringBuilder sb = new StringBuilder();

        sb.append("Scratch Codes:<br/><br/>");

        for (String code : scratchCodes) {
            sb.append(code).append("</br>");
        }

        scratchCodesArea.setText(sb.toString());
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        ContentPanel mfaContentPanel = new ContentPanel();
        mfaContentPanel.setBorders(false);
        mfaContentPanel.setBodyBorder(false);
        mfaContentPanel.setHeaderVisible(false);

        // Add Help
        buildHelpPanel();
        mfaContentPanel.add(helpPanel);
        hideHelp();

        // Explanation header
        FormLayout explanationLayout2 = new FormLayout();

        FieldSet explanationHeader = new FieldSet();
        explanationHeader.setBorders(false);
        explanationHeader.setAutoWidth(true);
        explanationHeader.setLayout(explanationLayout2);

        final Text text = new Text();
        text.setText(MSGS.mfaFormBarcodeLabel());

        explanationHeader.add(text);

        final Text text2 = new Text();
        text2.setText(MSGS.mfaFormBarcodeLabel2());
        explanationHeader.add(text2);

        explanationHeader.add(new HTML("<br />"));
        explanationHeader.add(new HTML("<br />"));

        enabledText = new Text();
        explanationHeader.add(enabledText);

        mfaContentPanel.add(explanationHeader);

        //
        // QR Code Image
        barcodeImage = new Image();
        barcodeImage.addLoadHandler(new LoadHandler() {

            @Override
            public void onLoad(LoadEvent event) {
                keyEnabled = true;
                barcodeImage.setVisible(true);

                scratchCodesArea.setVisible(true);
                printScratchCodes(gwtMfaCredentialOptions.getScratchCodes());
                updateUIComponents(gwtMfaCredentialOptions);
                getButtonBar().enable();
                doUnmask();
            }
        });

        //
        // Scratch Codes Text Area
        scratchCodesArea = new Text();
        scratchCodesArea.setStyleAttribute("padding", "10px 0px 0px 5px");
        scratchCodesArea.setStyleAttribute("text-align", "center");
        scratchCodesArea.setStyleName("x-form-label");
        scratchCodesArea.setWidth(110);

        HorizontalPanel imagePanel = new HorizontalPanel();
        imagePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        imagePanel.add(barcodeImage);
        imagePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        imagePanel.add(scratchCodesArea);

        Status internalStatus = new Status();
        internalStatus.setBusy("MSGS.waitMsg()");
        internalStatus.hide();

        mfaContentPanel.setButtonAlign(HorizontalAlignment.LEFT);
        mfaContentPanel.getButtonBar().add(internalStatus);
        mfaContentPanel.getButtonBar().add(new FillToolItem());

        mfaContentPanel.add(imagePanel, formData);

        mfaContentPanel.setButtonAlign(HorizontalAlignment.CENTER);
        mfaContentPanel.setTopComponent(createToolBar());

        add(mfaContentPanel);

        doMask(MSGS.maskMfaManagementPanel());
        GWT_MFA_CREDENTIAL_OPTIONS_SERVICE.findByUserId(accountId, userId, selfManagement, new AsyncCallback<GwtMfaCredentialOptions>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
                doUnmask();
            }

            @Override
            public void onSuccess(GwtMfaCredentialOptions gwtMfaCredentialOptions) {
                MfaManagementPanel.this.gwtMfaCredentialOptions = gwtMfaCredentialOptions;
                updateUIComponents(gwtMfaCredentialOptions);
                doUnmask();
            }
        });

    }

    private void updateUIComponents(GwtMfaCredentialOptions gwtMfaCredentialOptions) {
        boolean multiFactorAuth = gwtMfaCredentialOptions != null;
        boolean hasCredentialWrite = currentSession.hasPermission(CredentialSessionPermission.write());
        boolean hasCredentialDelete = currentSession.hasPermission(CredentialSessionPermission.delete());

        // MFA is enabled, user has credential:write or is managing himself, and there's a trust key defined: enable "revoke trust devices" button
        forgetTrustedMachine.setEnabled(multiFactorAuth && (hasCredentialWrite || selfManagement) && gwtMfaCredentialOptions.getTrustKey() != null);

        // MFA button icon & label
        if (multiFactorAuth) {
            // Always enabled for self management (both by being in the standalone dialog or clicking the user in the tab),
            // otherwise only allow to disable MFA if user has credential:delete
            enableMfa.setEnabled(selfManagement || username.equals(currentSession.getUserName()) || hasCredentialDelete);
            enableMfa.setText(MSGS.mfaButtonDisable());
            enabledText.setText(MSGS.mfaEnabled(username));
        } else {
            // Always enabled for self management (both by being in the standalone dialog or clicking the user in the tab)
            enableMfa.setEnabled((selfManagement || username.equals(currentSession.getUserName())) &&
                    (currentSession.getOpenIDIdToken() == null || currentSession.getOpenIDIdToken().isEmpty()));
            enableMfa.setText(MSGS.mfaButtonEnable());
            enabledText.setText(MSGS.mfaDisabled(username));
        }
        layout(true);
    }

    private void doMask(String message) {
        if (selfManagement) {
            selfManagementDialog.mask(message);
        } else {
            mask(message);
        }
    }

    private void doUnmask() {
        if (selfManagement) {
            selfManagementDialog.unmask();
        } else {
            unmask();
        }
    }

}
