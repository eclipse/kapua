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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;

/**
 * {@link Account} {@link ServiceDAO}
 * 
 * @since 1.0
 */
public class AccountDAO {

    private AccountDAO() {
    }

    /**
     * Creates and return new Account
     * 
     * @param em
     * @param accountCreator
     * @return
     * @throws KapuaException
     */
    public static Account create(EntityManager em, AccountCreator accountCreator)
            throws KapuaException {
        //
        // Create Account

        OrganizationImpl organizationImpl = new OrganizationImpl();
        organizationImpl.setName(accountCreator.getOrganizationName());
        organizationImpl.setPersonName(accountCreator.getOrganizationPersonName());
        organizationImpl.setEmail(accountCreator.getOrganizationEmail());
        organizationImpl.setPhoneNumber(accountCreator.getOrganizationPhoneNumber());
        organizationImpl.setAddressLine1(accountCreator.getOrganizationAddressLine1());
        organizationImpl.setAddressLine2(accountCreator.getOrganizationAddressLine2());
        organizationImpl.setCity(accountCreator.getOrganizationCity());
        organizationImpl.setZipPostCode(accountCreator.getOrganizationZipPostCode());
        organizationImpl.setStateProvinceCounty(accountCreator.getOrganizationStateProvinceCounty());
        organizationImpl.setCountry(accountCreator.getOrganizationCountry());

        AccountImpl accountImpl = new AccountImpl(accountCreator.getScopeId(), accountCreator.getName());
        accountImpl.setOrganization(organizationImpl);
        accountImpl.setExpirationDate(accountCreator.getExpirationDate());

        return ServiceDAO.create(em, accountImpl);
    }

    /**
     * Updates the provided account
     * 
     * @param em
     * @param account
     * @return
     * @throws KapuaException
     */
    public static Account update(EntityManager em, Account account)
            throws KapuaException {
        //
        // Update account
        AccountImpl accountImpl = (AccountImpl) account;

        return ServiceDAO.update(em, AccountImpl.class, accountImpl);
    }

    /**
     * Finds the account by account identifier
     */
    public static Account find(EntityManager em, KapuaId scopeId, KapuaId accountId) {
        return ServiceDAO.find(em, AccountImpl.class, scopeId, accountId);
    }

    /**
     * Finds the account by name
     *
     * @param em
     * @param name
     * @return
     */
    public static Account findByName(EntityManager em, String name) {
        return ServiceDAO.findByField(em, AccountImpl.class, "name", name);
    }

    /**
     * Returns the account list matching the provided query
     *
     * @param em
     * @param accountQuery
     * @return
     * @throws KapuaException
     */
    public static AccountListResult query(EntityManager em, KapuaQuery<Account> accountQuery)
            throws KapuaException {
        return ServiceDAO.query(em, Account.class, AccountImpl.class, new AccountListResultImpl(), accountQuery);
    }

    /**
     * Returns the account count matching the provided query
     *
     * @param em
     * @param accountQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<Account> accountQuery)
            throws KapuaException {
        return ServiceDAO.count(em, Account.class, AccountImpl.class, accountQuery);
    }

    /**
     * Deletes the account by account identifier
     *
     * @param em
     * @param scopeId
     * @param accountId
     * @throws KapuaEntityNotFoundException
     *             If the {@link Account} is not found
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId accountId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, AccountImpl.class, scopeId, accountId);
    }
}
