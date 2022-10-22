/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;

import javax.validation.constraints.NotNull;

/**
 * {@link Account} {@link ServiceDAO}.
 *
 * @since 1.0.0
 */
public class AccountDAO {

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private AccountDAO() {
    }

    /**
     * Persists an Account from the given {@link AccountCreator}.
     *
     * @param em             The {@link EntityManager} than owns the transaction.
     * @param accountCreator The {@link AccountCreator} to persist.
     * @return The newly persisted {@link Account}
     * @throws KapuaException
     * @since 1.0.0
     */
    public static Account create(@NotNull EntityManager em, @NotNull AccountCreator accountCreator)
            throws KapuaException {
        OrganizationImpl organizationImpl = new OrganizationImpl();
        organizationImpl.setName(accountCreator.getOrganizationName());
        organizationImpl.setPersonName(accountCreator.getOrganizationPersonName());
        organizationImpl.setEmail(accountCreator.getOrganizationEmail());
        organizationImpl.setPhoneNumber(accountCreator.getOrganizationPhoneNumber());
        organizationImpl.setAddressLine1(accountCreator.getOrganizationAddressLine1());
        organizationImpl.setAddressLine2(accountCreator.getOrganizationAddressLine2());
        organizationImpl.setAddressLine3(accountCreator.getOrganizationAddressLine3());
        organizationImpl.setCity(accountCreator.getOrganizationCity());
        organizationImpl.setZipPostCode(accountCreator.getOrganizationZipPostCode());
        organizationImpl.setStateProvinceCounty(accountCreator.getOrganizationStateProvinceCounty());
        organizationImpl.setCountry(accountCreator.getOrganizationCountry());

        AccountImpl accountImpl = new AccountImpl(accountCreator.getScopeId(), accountCreator.getName());
        accountImpl.setOrganization(organizationImpl);
        accountImpl.setExpirationDate(accountCreator.getExpirationDate());

        // Do create
        return ServiceDAO.create(em, accountImpl);
    }

    /**
     * Updates the given {@link Account}
     *
     * @param em      The {@link EntityManager} than owns the transaction.
     * @param account The {@link Account} to updated.
     * @return The updated {@link Account}
     * @throws KapuaException
     * @since 1.0.0
     */
    public static Account update(@NotNull EntityManager em, @NotNull Account account)
            throws KapuaException {
        AccountImpl accountImpl = (AccountImpl) account;

        // Do update
        return ServiceDAO.update(em, AccountImpl.class, accountImpl);
    }

    /**
     * Finds an {@link Account} by {@link Account#getScopeId()} and {@link Account#getId()}
     *
     * @param em        The {@link EntityManager} than owns the transaction.
     * @param scopeId   The {@link Account#getScopeId()}.
     * @param accountId The {@link Account#getId()}.
     * @return The {@link Account} found or {@code null}
     * @since 1.0.0
     */
    public static Account find(@NotNull EntityManager em, @NotNull KapuaId scopeId, @NotNull KapuaId accountId) {
        return ServiceDAO.find(em, AccountImpl.class, scopeId, accountId);
    }

    /**
     * Finds an {@link Account} by {@link Account#getName()}.
     *
     * @param em   The {@link EntityManager} than owns the transaction.
     * @param name The {@link Account#getName()}.
     * @return The {@link Account} found or {@code null}
     * @since 1.0.0
     */
    public static Account findByName(@NotNull EntityManager em, @NotNull String name) {
        return ServiceDAO.findByField(em, AccountImpl.class, KapuaNamedEntityAttributes.NAME, name);
    }

    /**
     * Finds the {@link Account}s that matches the given {@link AccountQuery}.
     *
     * @param em           The {@link EntityManager} than owns the transaction.
     * @param accountQuery The {@link AccountQuery} to filter results.
     * @return The {@link AccountListResult} that matches the {@link AccountQuery}.
     * @throws KapuaException
     * @since 1.0.0
     */
    public static AccountListResult query(@NotNull EntityManager em, @NotNull KapuaQuery accountQuery)
            throws KapuaException {
        return ServiceDAO.query(em, Account.class, AccountImpl.class, new AccountListResultImpl(), accountQuery);
    }

    /**
     * Counts the {@link Account}s that matches the given {@link AccountQuery}.
     *
     * @param em           The {@link EntityManager} than owns the transaction.
     * @param accountQuery The {@link AccountQuery} to filter results.
     * @return The count that matches the {@link AccountQuery}.
     * @throws KapuaException
     * @since 1.0.0
     */
    public static long count(@NotNull EntityManager em, @NotNull KapuaQuery accountQuery)
            throws KapuaException {
        return ServiceDAO.count(em, Account.class, AccountImpl.class, accountQuery);
    }

    /**
     * Deletes an {@link Account} by {@link Account#getScopeId()} and {@link Account#getId()}
     *
     * @param em        The {@link EntityManager} than owns the transaction.
     * @param scopeId   The {@link Account#getScopeId()}.
     * @param accountId The {@link Account#getId()}.
     * @return The {@link Account} deleted
     * @throws KapuaEntityNotFoundException if {@link Account} is not found before deletion.
     * @since 1.0.0
     */
    public static Account delete(@NotNull EntityManager em, @NotNull KapuaId scopeId, @NotNull KapuaId accountId)
            throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, AccountImpl.class, scopeId, accountId);
    }
}
