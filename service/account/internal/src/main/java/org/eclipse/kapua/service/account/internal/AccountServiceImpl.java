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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.account.internal;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.Map;
import java.util.Objects;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

/**
 * {@link AccountService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class AccountServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Account, AccountCreator, AccountService, AccountListResult, AccountQuery, AccountFactory>
        implements AccountService {

    private static final Domain ACCOUNT_DOMAIN = new AccountDomain();

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionFactory permissionFactory;

    /**
     * Constructor.
     * 
     * @since 1.0.0
     */
    public AccountServiceImpl() {
        super(AccountService.class.getName(), ACCOUNT_DOMAIN, AccountEntityManagerFactory.getInstance(), AccountService.class, AccountFactory.class);
    }

    @Override
    public Account create(AccountCreator accountCreator)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(accountCreator, "accountCreator");
        ArgumentValidator.notNull(accountCreator.getScopeId(), "accountCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(accountCreator.getName(), "accountCreator.name");
        ArgumentValidator.notEmptyOrNull(accountCreator.getOrganizationName(), "accountCreator.organizationName");
        ArgumentValidator.notEmptyOrNull(accountCreator.getOrganizationEmail(), "accountCreator.organizationEmail");
        ArgumentValidator.match(accountCreator.getOrganizationEmail(), ArgumentValidator.EMAIL_REGEXP, "accountCreator.organizationEmail");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.write, accountCreator.getScopeId()));

        // Check if the parent account exists
        if (findById(accountCreator.getScopeId()) == null) {
            throw new KapuaIllegalArgumentException("scopeId", "parent account does not exist: " + accountCreator.getScopeId() + "::");
        }

        // Check child account policy
        if (allowedChildEntities(accountCreator.getScopeId()) <= 0) {
            throw new KapuaIllegalArgumentException("scopeId", "max child account reached");
        }

        Account createdAccount = entityManagerSession.onTransactedInsert(em -> {
            Account account = AccountDAO.create(em, accountCreator);
            em.persist(account);

            // Set the parent account path
            String parentAccountPath = AccountDAO.find(em, accountCreator.getScopeId()).getParentAccountPath() + "/" + account.getId();
            account.setParentAccountPath(parentAccountPath);
            return AccountDAO.update(em, account);
        });

        return createdAccount;
    }

    @Override
    public Account update(Account account)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(account.getId(), "account.id");
        ArgumentValidator.notEmptyOrNull(account.getName(), "account.name");
        ArgumentValidator.notNull(account.getOrganization(), "account.organization");
        ArgumentValidator.match(account.getOrganization().getEmail(), ArgumentValidator.EMAIL_REGEXP, "account.organization.email");

        //
        // Check Access
        if (KapuaSecurityUtils.getSession().getScopeId().equals(account.getId())) {
            // Editing self
            authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.write, account.getId()));
        } else {
            // Editing child
            authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.write, account.getScopeId()));
        }

        //
        // Do update
        return entityManagerSession.onTransactedResult(em -> {
            Account oldAccount = AccountDAO.find(em, account.getId());
            if (oldAccount == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, account.getId());
            }

            //
            // Verify unchanged parent account ID and parent account path
            if (!Objects.equals(oldAccount.getScopeId(), account.getScopeId())) {
                throw new KapuaAccountException(KapuaAccountErrorCodes.ILLEGAL_ARGUMENT, null, "account.scopeId");
            }
            if (!oldAccount.getParentAccountPath().equals(account.getParentAccountPath())) {
                throw new KapuaAccountException(KapuaAccountErrorCodes.ILLEGAL_ARGUMENT, null, "account.parentAccountPath");
            }
            if (!oldAccount.getName().equals(account.getName())) {
                throw new KapuaAccountException(KapuaAccountErrorCodes.ILLEGAL_ARGUMENT, null, "account.name");
            }

            // Update
            return AccountDAO.update(em, account);
        });
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId accountId)
            throws KapuaException {

        //
        // Validation of the fields
        ArgumentValidator.notNull(accountId, "scopeId");
        ArgumentValidator.notNull(scopeId, "accountId");

        //
        // Check Access
        Actions action = Actions.write;
        authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, action, scopeId));

        //
        // Check if it has children
        if (!findChildAccountsTrusted(accountId).isEmpty()) {
            throw new KapuaAccountException(KapuaAccountErrorCodes.OPERATION_NOT_ALLOWED, null, "This account cannot be deleted. Delete its child first.");
        }

        entityManagerSession.onTransactedAction(em -> {
            // Entity needs to be loaded in the context of the same EntityManger to be able to delete it afterwards
            Account accountx = AccountDAO.find(em, accountId);
            if (accountx == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, accountId);
            }

            // do not allow deletion of the kapua admin account
            SystemSetting settings = SystemSetting.getInstance();
            if (settings.getString(SystemSettingKey.SYS_PROVISION_ACCOUNT_NAME).equals(accountx.getName())) {
                throw new KapuaIllegalAccessException(action.name());
            }

            if (settings.getString(SystemSettingKey.SYS_ADMIN_ACCOUNT).equals(accountx.getName())) {
                throw new KapuaIllegalAccessException(action.name());
            }

            AccountDAO.delete(em, accountId);
        });
    }

    @Override
    public Account find(KapuaId scopeId, KapuaId accountId)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(accountId, "accountId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.read, scopeId));

        //
        // Make sure account exists
        return findById(accountId);
    }

    @Override
    public Account find(KapuaId accountId)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(accountId, "accountId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.read, accountId));

        //
        // Make sure account exists
        return findById(accountId);
    }

    @Override
    public Account findByName(String name)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notEmptyOrNull(name, "name");

        return entityManagerSession.onResult(em -> {
            Account account = AccountDAO.findByName(em, name);
            // Check Access
            if (account != null) {
                authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.read, account.getId()));
            }

            return account;
        });
    }

    @Override
    public AccountListResult findChildsRecursively(KapuaId id)
            throws KapuaException {

        //
        // Validation of the fields
        ArgumentValidator.notNull(id, "scopeId");

        //
        // Make sure account exists
        Account account = findById(id);
        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, id);
        }

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.read, account.getId()));

        return entityManagerSession.onResult(em -> {
            AccountListResult result = null;
            TypedQuery<Account> q;
            q = em.createNamedQuery("Account.findChildAccountsRecursive", Account.class);
            q.setParameter("parentAccountPath", "\\" + account.getParentAccountPath() + "/%");

            result = new AccountListResultImpl();
            result.addItems(q.getResultList());
            return result;
        });
    }

    @Override
    public AccountListResult query(KapuaQuery<Account> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> AccountDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<Account> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(ACCOUNT_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> AccountDAO.count(em, query));
    }

    /**
     * Find an {@link Account} without authorization checks.
     *
     * @param accountId
     * @return
     * @throws KapuaException
     * 
     * @since 1.0.0
     */
    private Account findById(KapuaId accountId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(accountId, "accountId");

        return entityManagerSession.onResult(em -> AccountDAO.find(em, accountId));
    }

    private AccountListResult findChildAccountsTrusted(KapuaId accountId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(accountId, "accountId");
        ArgumentValidator.notNull(accountId.getId(), "accountId.id");

        return entityManagerSession.onResult(em -> {
            AccountQuery query = new AccountQueryImpl(accountId);
            return AccountDAO.query(em, query);
        });
    }

    @Override
    protected Map<String, Object> getConfigValues(Account entity) throws KapuaException {
        return super.getConfigValues(entity.getId());
    }

}
