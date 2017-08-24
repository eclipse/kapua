/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.account.client.toolbar;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.account.client.messages.ConsoleAccountMessages;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountCreator;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountServiceAsync;

public class AccountAddDialog extends EntityAddEditDialog {

    protected static final ConsoleAccountMessages MSGS = GWT.create(ConsoleAccountMessages.class);
    protected static final GwtAccountServiceAsync GWT_ACCOUNT_SERVICE = GWT.create(GwtAccountService.class);

    private static final int LABEL_WIDTH_FORM = 190;

    protected final FieldSet fieldSet = new FieldSet();

    // Account
    final LabelField parentAccountName = new LabelField();
    protected final LabelField accountNameLabel = new LabelField();
    protected final TextField<String> accountNameField = new TextField<String>();
    protected final TextField<String> accountPassword = new TextField<String>();
    protected final TextField<String> confirmPassword = new TextField<String>();

    // broker cluster
    protected final NumberField optlock = new NumberField();
    protected final LabelField accountClusterLabel = new LabelField();

    // organization
    protected final TextField<String> organizationName = new TextField<String>();
    protected final TextField<String> organizationEmail = new TextField<String>();
    protected final TextField<String> organizationPersonName = new TextField<String>();
    protected final TextField<String> organizationPhoneNumber = new TextField<String>();
    protected final TextField<String> organizationAddressLine1 = new TextField<String>();
    protected final TextField<String> organizationAddressLine2 = new TextField<String>();
    protected final TextField<String> organizationZipPostCode = new TextField<String>();
    protected final TextField<String> organizationCity = new TextField<String>();
    protected final TextField<String> organizationStateProvinceCounty = new TextField<String>();
    protected final TextField<String> organizationCountry = new TextField<String>();

    public AccountAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 600, 700);
    }

    @Override
    public void createBody() {
        FormPanel accountFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        // //////////////////////////////////////////
        // Account Information field set
        // //////////////////////////////////////////
        fieldSet.setHeading(MSGS.accountFormInformation());
        FormLayout layoutAccount = new FormLayout();
        layoutAccount.setLabelWidth(LABEL_WIDTH_FORM);
        fieldSet.setLayout(layoutAccount);

        //
        // Show parent account name
        //
        parentAccountName.setName("parentAccountName");
        parentAccountName.setFieldLabel(MSGS.accountFormParentAccount());
        parentAccountName.setLabelSeparator(":");
        fieldSet.add(parentAccountName);

        //
        // Account name field
        //
        accountNameLabel.setName("accountNameLabel");
        accountNameLabel.setFieldLabel(MSGS.accountFormName());
        accountNameLabel.setLabelSeparator(":");
        accountNameLabel.setVisible(false);
        fieldSet.add(accountNameLabel);

        accountNameField.setAllowBlank(false);
        accountNameField.setName("accountName");
        accountNameField.setFieldLabel("* " + MSGS.accountFormName());
        accountNameField.setValidator(new TextFieldValidator(accountNameField, FieldType.SIMPLE_NAME));
        fieldSet.add(accountNameField);

        accountFormPanel.add(fieldSet);

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
        accountClusterLabel.setName("accountBrokerLabel");
        accountClusterLabel.setFieldLabel(MSGS.accountFormBrokerCluster());
        accountClusterLabel.setLabelSeparator(":");
        accountClusterLabel.setVisible(false);
        fieldSetDeployment.add(accountClusterLabel);

        optlock.setName("optlock");
        optlock.setEditable(false);
        optlock.setVisible(false);
        fieldSetDeployment.add(optlock);

        // add the field set and reset
        // accountFormPanel.add(fieldSetDeployment);

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
        organizationName.setAllowBlank(false);
        organizationName.setName("organizationName");
        organizationName.setFieldLabel("* " + MSGS.accountFormOrgName());
        fieldSetOrg.add(organizationName);

        //
        // Organization email
        //
        organizationEmail.setAllowBlank(false);
        organizationEmail.setName("organizationEmail");
        organizationEmail.setFieldLabel("* " + MSGS.accountFormOrgEmail());
        organizationEmail.setValidator(new TextFieldValidator(organizationEmail, FieldType.EMAIL));
        fieldSetOrg.add(organizationEmail);

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

        organizationPersonName.setName("organizationPersonName");
        organizationPersonName.setFieldLabel(MSGS.accountFormOrgPersonName());
        organizationSubFieldSet.add(organizationPersonName, subFieldsetFormData);

        organizationPhoneNumber.setName("organizationPhoneNumber");
        organizationPhoneNumber.setFieldLabel(MSGS.accountFormOrgPhoneNumber());
        organizationSubFieldSet.add(organizationPhoneNumber, subFieldsetFormData);

        organizationAddressLine1.setName("organizationAddressLine1");
        organizationAddressLine1.setFieldLabel(MSGS.accountFormOrgAddress1());
        organizationSubFieldSet.add(organizationAddressLine1, subFieldsetFormData);

        organizationAddressLine2.setName("organizationAddressLine2");
        organizationAddressLine2.setFieldLabel(MSGS.accountFormOrgAddress2());
        organizationSubFieldSet.add(organizationAddressLine2, subFieldsetFormData);

        organizationZipPostCode.setName("organizationZipPostCode");
        organizationZipPostCode.setFieldLabel(MSGS.accountFormOrgZipPostCode());
        organizationSubFieldSet.add(organizationZipPostCode, subFieldsetFormData);

        organizationCity.setName("organizationCity");
        organizationCity.setFieldLabel(MSGS.accountFormOrgCity());
        organizationSubFieldSet.add(organizationCity, subFieldsetFormData);

        organizationStateProvinceCounty.setName("organizationStateProvinceCounty");
        organizationStateProvinceCounty.setFieldLabel(MSGS.accountFormOrgState());
        organizationSubFieldSet.add(organizationStateProvinceCounty, subFieldsetFormData);

        organizationCountry.setName("organizationCountry");
        organizationCountry.setFieldLabel(MSGS.accountFormOrgCountry());
        organizationSubFieldSet.add(organizationCountry, subFieldsetFormData);

        // add the field set and reset
        fieldSetOrg.add(organizationSubFieldSet);
        accountFormPanel.add(fieldSetOrg);

        bodyPanel.add(accountFormPanel);
    }

    @Override
    public void submit() {
        GwtAccountCreator gwtAccountCreator = new GwtAccountCreator();
        gwtAccountCreator.setParentAccountId(currentSession.getSelectedAccountId());
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

        GWT_ACCOUNT_SERVICE.create(xsrfToken,
                gwtAccountCreator,
                new AsyncCallback<GwtAccount>() {

                    public void onFailure(Throwable caught) {
                        FailureHandler.handleFormException(formPanel, caught);
                        status.hide();
                        formPanel.getButtonBar().enable();
                        unmask();
                        submitButton.enable();
                        cancelButton.enable();
                    }

                    public void onSuccess(GwtAccount account) {
                        ConsoleInfo.display(MSGS.info(), MSGS.accountCreatedConfirmation(account.getUnescapedName()));
                        hide();
                    }
                });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.accountAddDialogHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.accountAddDialogInfoMessage();
    }

}
