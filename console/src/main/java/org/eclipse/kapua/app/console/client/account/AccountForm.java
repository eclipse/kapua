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
package org.eclipse.kapua.app.console.client.account;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.ConfirmPasswordFieldValidator;
import org.eclipse.kapua.app.console.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.client.util.TextFieldValidator;
import org.eclipse.kapua.app.console.client.util.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.shared.model.GwtOrganization;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountCreator;
import org.eclipse.kapua.app.console.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.shared.service.GwtAccountServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.shared.service.GwtSecurityTokenServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AccountForm extends Window
{

    private static final ConsoleMessages       MSGS              = GWT.create(ConsoleMessages.class);

    private final GwtAccountServiceAsync       gwtAccountService = GWT.create(GwtAccountService.class);
    private final GwtSecurityTokenServiceAsync gwtXSRFService    = GWT.create(GwtSecurityTokenService.class);

    private static final int                   LABEL_WIDTH_FORM  = 190;

    private GwtSession                         m_currentSession;
    private GwtAccount                         m_newAccount;
    private GwtAccount                         m_existingAccount;
    private FormPanel                          m_formPanel;
    private Status                             m_status;

    public AccountForm(GwtSession session)
    {
        m_currentSession = session;
        m_newAccount = null;

        setModal(true);
        setLayout(new FitLayout());
        setResizable(false);
        String heading = MSGS.accountFormNew();
        setHeading(heading);

        DialogUtils.resizeDialog(this, 600, 700);
    }

    public AccountForm(GwtSession session, GwtAccount existingAccount)
    {
        this(session);
        m_existingAccount = existingAccount;
        if (m_existingAccount != null) {
            setHeading(MSGS.accountFormUpdate(m_existingAccount.getName()));
        }
    }

    public GwtAccount getNewAccount()
    {
        return m_newAccount;
    }

    public GwtAccount getExistingAccount()
    {
        return m_existingAccount;
    }

    protected void onRender(Element parent, int index)
    {
        super.onRender(parent, index);

        FormData formData = new FormData("-30");

        m_formPanel = new FormPanel();
        m_formPanel.setFrame(false);
        m_formPanel.setBodyBorder(true);
        m_formPanel.setHeaderVisible(false);
        m_formPanel.setScrollMode(Scroll.AUTOY);
        m_formPanel.setLayout(new FlowLayout());

        // //////////////////////////////////////////
        // Account Information field set
        // //////////////////////////////////////////
        FieldSet fieldSet = new FieldSet();
        fieldSet.setHeading(MSGS.accountFormInformation());
        FormLayout layoutAccount = new FormLayout();
        layoutAccount.setLabelWidth(LABEL_WIDTH_FORM);
        fieldSet.setLayout(layoutAccount);

        //
        // Show parent account name
        //
        final LabelField parentAccountName = new LabelField();
        parentAccountName.setName("parentAccountName");
        parentAccountName.setFieldLabel(MSGS.accountFormParentAccount());
        parentAccountName.setLabelSeparator(":");
        fieldSet.add(parentAccountName, formData);

        //
        // Account name field
        //
        final LabelField accountNameLabel = new LabelField();
        accountNameLabel.setName("accountNameLabel");
        accountNameLabel.setFieldLabel(MSGS.accountFormName());
        accountNameLabel.setLabelSeparator(":");
        fieldSet.add(accountNameLabel, formData);

        final TextField<String> accountNameField = new TextField<String>();
        accountNameField.setAllowBlank(false);
        accountNameField.setName("accountName");
        accountNameField.setFieldLabel("* " + MSGS.accountFormName());
        accountNameField.setValidator(new TextFieldValidator(accountNameField, FieldType.SIMPLE_NAME));
        fieldSet.add(accountNameField, formData);

        //
        // passwords
        //
        final TextField<String> accountPassword = new TextField<String>();
        accountPassword.setAllowBlank(false);
        accountPassword.setName("accountPassword");
        accountPassword.setFieldLabel("* " + MSGS.accountFormPassword());
        accountPassword.setValidator(new TextFieldValidator(accountPassword, FieldType.PASSWORD));
        accountPassword.setPassword(true);
        fieldSet.add(accountPassword, formData);

        //
        // Confirm password
        //
        final TextField<String> confirmPassword = new TextField<String>();
        confirmPassword.setAllowBlank(false);
        confirmPassword.setName("confirmPassword");
        confirmPassword.setFieldLabel("* " + MSGS.accountFormConfirmPassword());
        confirmPassword.setValidator(new ConfirmPasswordFieldValidator(confirmPassword, accountPassword));
        confirmPassword.setPassword(true);
        fieldSet.add(confirmPassword, formData);

        m_formPanel.add(fieldSet);

        // //////////////////////////////////////////
        // Deployment Information field set
        // //////////////////////////////////////////
        FieldSet fieldSetDeployment = new FieldSet();
        fieldSetDeployment.setHeading(MSGS.accountFormDeploymentInformation());
        FormLayout layoutDeployment = new FormLayout();
        layoutDeployment.setLabelWidth(LABEL_WIDTH_FORM);
        fieldSetDeployment.setLayout(layoutDeployment);

        //
        // broker cluster
        //
        final LabelField accountClusterLabel = new LabelField();
        accountClusterLabel.setName("accountBrokerLabel");
        accountClusterLabel.setFieldLabel(MSGS.accountFormBrokerCluster());
        accountClusterLabel.setLabelSeparator(":");
        fieldSetDeployment.add(accountClusterLabel, formData);

        final NumberField optlock = new NumberField();
        optlock.setName("optlock");
        optlock.setEditable(false);
        optlock.setVisible(false);
        fieldSetDeployment.add(optlock, formData);

        // add the field set and reset
        m_formPanel.add(fieldSetDeployment);

        // //////////////////////////////////////////
        // Organization Information field set
        // //////////////////////////////////////////
        FieldSet fieldSetOrg = new FieldSet();
        fieldSetOrg.setHeading(MSGS.accountFormOrgInformation());
        FormLayout layoutOrg = new FormLayout();
        layoutOrg.setLabelWidth(LABEL_WIDTH_FORM);
        fieldSetOrg.setLayout(layoutOrg);

        //
        // Organization name
        //
        final TextField<String> organizationName = new TextField<String>();
        organizationName.setAllowBlank(false);
        organizationName.setName("organizationName");
        organizationName.setFieldLabel("* " + MSGS.accountFormOrgName());
        fieldSetOrg.add(organizationName, formData);

        //
        // Organization email
        //
        final TextField<String> organizationEmail = new TextField<String>();
        organizationEmail.setAllowBlank(false);
        organizationEmail.setName("organizationEmail");
        organizationEmail.setFieldLabel("* " + MSGS.accountFormOrgEmail());
        organizationEmail.setValidator(new TextFieldValidator(organizationEmail, FieldType.EMAIL));
        fieldSetOrg.add(organizationEmail, formData);

        // //////////////////////////////////////////
        // Organization Information sub field set
        // //////////////////////////////////////////
        FieldSet organizationSubFieldSet = new FieldSet();
        organizationSubFieldSet.setHeading(MSGS.accountFormOrgMoreInformation());
        organizationSubFieldSet.setBorders(false);
        organizationSubFieldSet.setCollapsible(true);
        organizationSubFieldSet.setWidth(515);

        FormLayout organizationSubLayout = new FormLayout();
        organizationSubLayout.setLabelWidth(LABEL_WIDTH_FORM - 11);
        organizationSubFieldSet.setLayout(organizationSubLayout);

        //
        // Other organization data
        //

        FormData subFieldsetFormData = new FormData("-7");

        final TextField<String> organizationPersonName = new TextField<String>();
        organizationPersonName.setName("organizationPersonName");
        organizationPersonName.setFieldLabel(MSGS.accountFormOrgPersonName());
        organizationSubFieldSet.add(organizationPersonName, subFieldsetFormData);

        final TextField<String> organizationPhoneNumber = new TextField<String>();
        organizationPhoneNumber.setName("organizationPhoneNumber");
        organizationPhoneNumber.setFieldLabel(MSGS.accountFormOrgPhoneNumber());
        organizationSubFieldSet.add(organizationPhoneNumber, subFieldsetFormData);

        final TextField<String> organizationAddressLine1 = new TextField<String>();
        organizationAddressLine1.setName("organizationAddressLine1");
        organizationAddressLine1.setFieldLabel(MSGS.accountFormOrgAddress1());
        organizationSubFieldSet.add(organizationAddressLine1, subFieldsetFormData);

        final TextField<String> organizationAddressLine2 = new TextField<String>();
        organizationAddressLine2.setName("organizationAddressLine2");
        organizationAddressLine2.setFieldLabel(MSGS.accountFormOrgAddress2());
        organizationSubFieldSet.add(organizationAddressLine2, subFieldsetFormData);

        final TextField<String> organizationZipPostCode = new TextField<String>();
        organizationZipPostCode.setName("organizationZipPostCode");
        organizationZipPostCode.setFieldLabel(MSGS.accountFormOrgZipPostCode());
        organizationSubFieldSet.add(organizationZipPostCode, subFieldsetFormData);

        final TextField<String> organizationCity = new TextField<String>();
        organizationCity.setName("organizationCity");
        organizationCity.setFieldLabel(MSGS.accountFormOrgCity());
        organizationSubFieldSet.add(organizationCity, subFieldsetFormData);

        final TextField<String> organizationStateProvinceCounty = new TextField<String>();
        organizationStateProvinceCounty.setName("organizationStateProvinceCounty");
        organizationStateProvinceCounty.setFieldLabel(MSGS.accountFormOrgState());
        organizationSubFieldSet.add(organizationStateProvinceCounty, subFieldsetFormData);

        final TextField<String> organizationCountry = new TextField<String>();
        organizationCountry.setName("organizationCountry");
        organizationCountry.setFieldLabel(MSGS.accountFormOrgCountry());
        organizationSubFieldSet.add(organizationCountry, subFieldsetFormData);

        // add the field set and reset
        fieldSetOrg.add(organizationSubFieldSet);
        m_formPanel.add(fieldSetOrg);

        //
        // If is a new account
        //
        if (m_existingAccount == null) {
            // Show editable name, password, confirm password
            accountNameLabel.setVisible(false);
            accountClusterLabel.setVisible(false);
        }
        //
        // If is an update of an existing account
        //
        else {
            // Show parent account name and account name
            accountNameField.setVisible(false);
            accountPassword.setVisible(false);
            confirmPassword.setVisible(false);
        }

        m_status = new Status();
        m_status.setBusy(MSGS.waitMsg());
        m_status.hide();
        m_status.setAutoWidth(true);

        m_formPanel.setButtonAlign(HorizontalAlignment.LEFT);
        m_formPanel.getButtonBar().add(m_status);
        m_formPanel.getButtonBar().add(new FillToolItem());

        //
        // Behave of Submit Button
        //
        m_formPanel.addButton(new Button(MSGS.submitButton(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce)
            {

                // make sure all visible fields are valid before performing the action
                for (Field<?> field : m_formPanel.getFields()) {
                    if (field.isVisible() && !field.isValid()) {
                        MessageBox.alert(MSGS.error(), MSGS.formErrors(), null);
                        return;
                    }
                }

                //
                // Hold the dialog until the action comes back
                m_status.show();
                m_formPanel.getButtonBar().disable();

                //
                // Create new account
                //
                if (m_existingAccount == null) {

                    final GwtAccountCreator gwtAccountCreator = new GwtAccountCreator();
                    gwtAccountCreator.setParentAccountId(m_currentSession.getSelectedAccount().getId());
                    gwtAccountCreator.setAccountName(accountNameField.getValue());
                    gwtAccountCreator.setAccountPassword(accountPassword.getValue());

                    // Organization data
                    gwtAccountCreator.setOrganizationName(organizationName.getValue());
                    gwtAccountCreator.setOrganizationPersonName(organizationPersonName.getValue());
                    gwtAccountCreator.setOrganizationEmail(organizationEmail.getValue());
                    gwtAccountCreator.setOrganizationPhoneNumber(organizationPhoneNumber.getValue());
                    gwtAccountCreator.setOrganizationAddressLine1(organizationAddressLine1.getValue());
                    gwtAccountCreator.setOrganizationAddressLine2(organizationAddressLine2.getValue());
                    gwtAccountCreator.setOrganizationCity(organizationCity.getValue());
                    gwtAccountCreator.setOrganizationZipPostCode(organizationZipPostCode.getValue());
                    gwtAccountCreator.setOrganizationStateProvinceCounty(organizationStateProvinceCounty.getValue());
                    gwtAccountCreator.setOrganizationCountry(organizationCountry.getValue());

                    //
                    // Call to create an account
                    // Getting XSRF token
                    gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                        @Override
                        public void onFailure(Throwable ex)
                        {
                            FailureHandler.handle(ex);
                        }

                        @Override
                        public void onSuccess(GwtXSRFToken token)
                        {
                            gwtAccountService.create(token,
                                                     gwtAccountCreator,
                                                     new AsyncCallback<GwtAccount>() {
                                                         public void onFailure(Throwable caught)
                                                         {
                                                             FailureHandler.handleFormException(m_formPanel, caught);
                                                             m_status.hide();
                                                             m_formPanel.getButtonBar().enable();
                                                         }

                                                         public void onSuccess(GwtAccount account)
                                                         {
                                                             ConsoleInfo.display(MSGS.info(), MSGS.accountCreatedConfirmation(account.getUnescapedName()));
                                                             m_newAccount = account;
                                                             // gwtAccountUtils.loadChildAccounts();
                                                             hide();
                                                         }
                                                     });
                        }
                    });
                }
                //
                // Update the account
                //
                else {

                    // Organization data
                    GwtOrganization gwtOrganization = new GwtOrganization();
                    gwtOrganization.setName(organizationName.getValue());
                    gwtOrganization.setPersonName(organizationPersonName.getValue());
                    gwtOrganization.setEmailAddress(organizationEmail.getValue());
                    gwtOrganization.setPhoneNumber(organizationPhoneNumber.getValue());
                    gwtOrganization.setAddressLine1(organizationAddressLine1.getValue());
                    gwtOrganization.setAddressLine2(organizationAddressLine2.getValue());
                    gwtOrganization.setZipPostCode(organizationZipPostCode.getValue());
                    gwtOrganization.setCity(organizationCity.getValue());
                    gwtOrganization.setStateProvinceCounty(organizationStateProvinceCounty.getValue());
                    gwtOrganization.setCountry(organizationCountry.getValue());
                    m_existingAccount.setGwtOrganization(gwtOrganization);

                    //
                    // Call to update the account
                    // Getting XSRF token
                    gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                        @Override
                        public void onFailure(Throwable ex)
                        {
                            FailureHandler.handle(ex);
                        }

                        @Override
                        public void onSuccess(GwtXSRFToken token)
                        {
                            gwtAccountService.update(token,
                                                     m_existingAccount,
                                                     new AsyncCallback<GwtAccount>() {
                                                         public void onFailure(Throwable caught)
                                                         {
                                                             FailureHandler.handleFormException(m_formPanel, caught);
                                                             m_status.hide();
                                                             m_formPanel.getButtonBar().enable();
                                                         }

                                                         public void onSuccess(GwtAccount account)
                                                         {
                                                             ConsoleInfo.display(MSGS.info(), MSGS.accountUpdatedConfirmation(account.getUnescapedName()));
                                                             m_existingAccount = account;
                                                             hide();
                                                         }
                                                     });
                        }
                    });

                }
            }
        }));

        //
        // Cancel Button
        //
        m_formPanel.addButton(new Button(MSGS.cancelButton(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce)
            {
                hide();
            }
        }));
        m_formPanel.setButtonAlign(HorizontalAlignment.CENTER);

        //
        // Populate field if necessary
        //
        parentAccountName.setValue(m_currentSession.getSelectedAccount().getName());
        if (m_existingAccount != null) {
            gwtAccountService.find(m_existingAccount.getId(), new AsyncCallback<GwtAccount>() {
                public void onFailure(Throwable caught)
                {
                    FailureHandler.handle(caught);
                }

                public void onSuccess(GwtAccount account)
                {
                    // set value and original value as we want to track the Dirty state
                    accountNameLabel.setValue(account.getName());
                    accountNameField.setValue(account.getName());
                    accountNameField.setOriginalValue(account.getName());

                    accountClusterLabel.setValue(account.getBrokerURL());

                    organizationName.setValue(account.getGwtOrganization().getName());
                    organizationName.setOriginalValue(account.getGwtOrganization().getName());

                    organizationPersonName.setValue(account.getGwtOrganization().getPersonName());
                    organizationPersonName.setOriginalValue(account.getGwtOrganization().getPersonName());

                    organizationEmail.setValue(account.getGwtOrganization().getEmailAddress());
                    organizationEmail.setOriginalValue(account.getGwtOrganization().getEmailAddress());

                    organizationPhoneNumber.setValue(account.getGwtOrganization().getPhoneNumber());
                    organizationPhoneNumber.setOriginalValue(account.getGwtOrganization().getPhoneNumber());

                    organizationAddressLine1.setValue(account.getGwtOrganization().getAddressLine1());
                    organizationAddressLine1.setOriginalValue(account.getGwtOrganization().getAddressLine1());

                    organizationAddressLine2.setValue(account.getGwtOrganization().getAddressLine2());
                    organizationAddressLine2.setOriginalValue(account.getGwtOrganization().getAddressLine2());

                    organizationZipPostCode.setValue(account.getGwtOrganization().getZipPostCode());
                    organizationZipPostCode.setOriginalValue(account.getGwtOrganization().getZipPostCode());

                    organizationCity.setValue(account.getGwtOrganization().getCity());
                    organizationCity.setOriginalValue(account.getGwtOrganization().getCity());

                    organizationStateProvinceCounty.setValue(account.getGwtOrganization().getStateProvinceCounty());
                    organizationStateProvinceCounty.setOriginalValue(account.getGwtOrganization().getStateProvinceCounty());

                    organizationCountry.setValue(account.getGwtOrganization().getCountry());
                    organizationCountry.setOriginalValue(account.getGwtOrganization().getCountry());

                    optlock.setValue(account.getOptlock());
                }
            });
        }

        add(m_formPanel);
    }
}
