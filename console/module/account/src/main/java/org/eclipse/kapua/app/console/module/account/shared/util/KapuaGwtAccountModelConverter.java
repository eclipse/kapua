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
package org.eclipse.kapua.app.console.module.account.shared.util;

import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtOrganization;
import org.eclipse.kapua.commons.util.SystemUtils;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.Organization;

import java.net.URISyntaxException;

public class KapuaGwtAccountModelConverter {

    private KapuaGwtAccountModelConverter() {
    }

    /**
     * Converts a {@link Account} into a {@link GwtAccount} for GWT usage.
     *
     * @param account The {@link Account} to convertKapuaId.
     * @return The converted {@link GwtAccount}
     * @since 1.0.0
     */
    public static GwtAccount convertAccount(Account account) {
        GwtAccount gwtAccount = new GwtAccount();

        //
        // Convert commons attributes
        KapuaGwtCommonsModelConverter.convertUpdatableEntity(account, gwtAccount);

        //
        // Convert other attributes
        gwtAccount.setName(account.getName());
        gwtAccount.setGwtOrganization(convertOrganization(account.getOrganization()));
        gwtAccount.setParentAccountId(account.getScopeId() != null ? account.getScopeId().toCompactId() : null);
        gwtAccount.setOptlock(account.getOptlock());
        gwtAccount.set("orgName", account.getOrganization().getName());
        gwtAccount.set("orgEmail", account.getOrganization().getEmail());

        try {
            gwtAccount.setBrokerURL(SystemUtils.getBrokerURI().toString());
        } catch (URISyntaxException use) {
            gwtAccount.setBrokerURL("");
        }

        //
        // Return converted entity
        return gwtAccount;
    }

    /**
     * Converts a {@link Organization} into a {@link GwtOrganization} for GWT usage.
     *
     * @param organization The {@link Organization} to convertKapuaId.
     * @return The converted {@link GwtOrganization}.
     * @since 1.0.0
     */
    public static GwtOrganization convertOrganization(Organization organization) {
        GwtOrganization gwtOrganization = new GwtOrganization();

        gwtOrganization.setName(organization.getName());
        gwtOrganization.setPersonName(organization.getPersonName());
        gwtOrganization.setEmailAddress(organization.getEmail());
        gwtOrganization.setPhoneNumber(organization.getPhoneNumber());
        gwtOrganization.setAddressLine1(organization.getAddressLine1());
        gwtOrganization.setAddressLine2(organization.getAddressLine2());
        gwtOrganization.setZipPostCode(organization.getZipPostCode());
        gwtOrganization.setCity(organization.getCity());
        gwtOrganization.setStateProvinceCounty(organization.getStateProvinceCounty());
        gwtOrganization.setCountry(organization.getCountry());

        //
        // Return converted entity
        return gwtOrganization;
    }
}
