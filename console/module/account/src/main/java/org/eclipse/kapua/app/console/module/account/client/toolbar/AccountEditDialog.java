/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.Style.Scroll;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtOrganization;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountEditDialog extends AccountAddDialog {

    GwtAccount selectedAccount;

    public AccountEditDialog(GwtSession currentSession, GwtAccount selectedAccount) {
        super(currentSession);
        this.selectedAccount = selectedAccount;
        DialogUtils.resizeDialog(this, 600, 550);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        bodyPanel.setAutoHeight(true);
        setClosable(false);
        setScrollMode(Scroll.AUTO);

        fieldSet.remove(accountNameField);
        accountNameLabel.setVisible(true);

        accountNameLabel.setValue(selectedAccount.getName());
        accountNameField.setValue(selectedAccount.getName());

        organizationName.setValue(selectedAccount.getGwtOrganization().getName());
        organizationName.setOriginalValue(selectedAccount.getGwtOrganization().getName());

        organizationContactName.setValue(selectedAccount.getGwtOrganization().getPersonName());
        organizationContactName.setOriginalValue(selectedAccount.getGwtOrganization().getPersonName());

        organizationEmail.setValue(selectedAccount.getGwtOrganization().getEmailAddress());
        organizationEmail.setOriginalValue(selectedAccount.getGwtOrganization().getEmailAddress());

        organizationPhoneNumber.setValue(selectedAccount.getGwtOrganization().getPhoneNumber());
        organizationPhoneNumber.setOriginalValue(selectedAccount.getGwtOrganization().getPhoneNumber());

        organizationAddressLine1.setValue(selectedAccount.getGwtOrganization().getAddressLine1());
        organizationAddressLine1.setOriginalValue(selectedAccount.getGwtOrganization().getAddressLine1());

        organizationAddressLine2.setValue(selectedAccount.getGwtOrganization().getAddressLine2());
        organizationAddressLine2.setOriginalValue(selectedAccount.getGwtOrganization().getAddressLine2());

        organizationZipPostCode.setValue(selectedAccount.getGwtOrganization().getZipPostCode());
        organizationZipPostCode.setOriginalValue(selectedAccount.getGwtOrganization().getZipPostCode());

        organizationCity.setValue(selectedAccount.getGwtOrganization().getCity());
        organizationCity.setOriginalValue(selectedAccount.getGwtOrganization().getCity());

        organizationStateProvinceCounty.setValue(selectedAccount.getGwtOrganization().getStateProvinceCounty());
        organizationStateProvinceCounty.setOriginalValue(selectedAccount.getGwtOrganization().getStateProvinceCounty());

        organizationCountry.setValue(selectedAccount.getGwtOrganization().getCountry());
        organizationCountry.setOriginalValue(selectedAccount.getGwtOrganization().getCountry());

        accountClusterLabel.setVisible(true);
        optlock.setValue(selectedAccount.getOptlock());
    }

    @Override
    public void submit() {
        // Organization data
        GwtOrganization gwtOrganization = new GwtOrganization();
        gwtOrganization.setName(organizationName.getValue());
        gwtOrganization.setPersonName(organizationContactName.getValue());
        gwtOrganization.setEmailAddress(organizationEmail.getValue());
        gwtOrganization.setPhoneNumber(organizationPhoneNumber.getValue());
        gwtOrganization.setAddressLine1(organizationAddressLine1.getValue());
        gwtOrganization.setAddressLine2(organizationAddressLine2.getValue());
        gwtOrganization.setZipPostCode(organizationZipPostCode.getValue());
        gwtOrganization.setCity(organizationCity.getValue());
        gwtOrganization.setStateProvinceCounty(organizationStateProvinceCounty.getValue());
        gwtOrganization.setCountry(organizationCountry.getValue());
        selectedAccount.setGwtOrganization(gwtOrganization);

        GWT_ACCOUNT_SERVICE.update(xsrfToken,
                selectedAccount,
                new AsyncCallback<GwtAccount>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        FailureHandler.handleFormException(formPanel, caught);
                        status.hide();
                        formPanel.getButtonBar().enable();
                    }

                    @Override
                    public void onSuccess(GwtAccount account) {
                        ConsoleInfo.display(MSGS.info(), MSGS.accountUpdatedConfirmation());
                        hide();
                    }
                });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.accountEditDialogHeader(selectedAccount.getName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.accountEditInfoMessage();
    }
}
