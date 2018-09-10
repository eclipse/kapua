/*******************************************************************************
 * Copyright (c) 2011 , 2018 Eurotech and/or its affiliates and others
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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaDuplicateNameInAnotherAccountError;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalAccessException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaMaxNumberOfItemsReachedException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountAttributes;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountDomains;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.Map;
import java.util.Objects;

/**
 * {@link AccountService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class AccountServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Account, AccountCreator, AccountService, AccountListResult, AccountQuery, AccountFactory>
        implements AccountService {

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
        super(AccountService.class.getName(), AccountDomains.ACCOUNT_DOMAIN, AccountEntityManagerFactory.getInstance(), AccountService.class, AccountFactory.class);
    }

    @Override
    //@RaiseServiceEvent
    public Account create(AccountCreator accountCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(accountCreator, "accountCreator");
        ArgumentValidator.notNull(accountCreator.getScopeId(), "accountCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(accountCreator.getName(), "accountCreator.name");
        ArgumentValidator.notEmptyOrNull(accountCreator.getOrganizationName(), "accountCreator.organizationName");
        ArgumentValidator.notEmptyOrNull(accountCreator.getOrganizationEmail(), "accountCreator.organizationEmail");
        ArgumentValidator.match(accountCreator.getOrganizationEmail(), CommonsValidationRegex.EMAIL_REGEXP, "accountCreator.organizationEmail");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.write, accountCreator.getScopeId()));

        //
        // Check child account policy
        if (allowedChildEntities(accountCreator.getScopeId()) <= 0) {
            throw new KapuaMaxNumberOfItemsReachedException("Accounts");
        }

        //
        // Check if the parent account exists
        if (findById(accountCreator.getScopeId()) == null) {
            throw new KapuaIllegalArgumentException("scopeId", "parent account does not exist: " + accountCreator.getScopeId() + "::");
        }

        //
        // check if the account collides with the SystemSettingKey#COMMONS_CONTROL_TOPIC_CLASSIFIER
        if (!StringUtils.isEmpty(SystemSetting.getInstance().getMessageClassifier())) {
            if (SystemSetting.getInstance().getMessageClassifier().equals(accountCreator.getName())) {
                throw new KapuaIllegalArgumentException("name", "Reserved account name");// obfuscate this message? or change to something more clear like "the account name collides with some system
                // configuration parameter"?
            }
        }

        //
        // Check duplicate name
        AccountQuery query = new AccountQueryImpl(accountCreator.getScopeId());
        query.setPredicate(new AttributePredicateImpl<>(AccountAttributes.NAME, accountCreator.getName()));
        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(accountCreator.getName());
        }

        if (findByName(accountCreator.getName()) != null) {
            throw new KapuaDuplicateNameInAnotherAccountError(accountCreator.getName());
        }

        // check that expiration date is no later than parent expiration date
        Account parentAccount = KapuaSecurityUtils.doPrivileged(() -> find(accountCreator.getScopeId()));
        if (parentAccount != null && parentAccount.getExpirationDate() != null) {
            // parent account never expires no check is needed
            if (accountCreator.getExpirationDate() == null || parentAccount.getExpirationDate().before(accountCreator.getExpirationDate())) {
                // if current account expiration date is null it will be obviously after parent expiration date
                throw new KapuaIllegalArgumentException("expirationDate", accountCreator.getExpirationDate() != null ? accountCreator.getExpirationDate().toString() : "no expiration date set");
            }
        }

        return entityManagerSession.onTransactedInsert(em -> {
            Account account = AccountDAO.create(em, accountCreator);
            em.persist(account);

            // Set the parent account path
            String parentAccountPath = AccountDAO.find(em, null, accountCreator.getScopeId()).getParentAccountPath() + "/" + account.getId();
            account.setParentAccountPath(parentAccountPath);
            return AccountDAO.update(em, account);
        });
    }

    @Override
    //@RaiseServiceEvent
    public Account update(Account account) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(account.getId(), "account.id");
        ArgumentValidator.notEmptyOrNull(account.getName(), "account.name");
        ArgumentValidator.notNull(account.getOrganization(), "account.organization");
        ArgumentValidator.match(account.getOrganization().getEmail(), CommonsValidationRegex.EMAIL_REGEXP, "account.organization.email");

        //
        // Check Access
        if (KapuaSecurityUtils.getSession().getScopeId().equals(account.getId())) {
            // Editing self
            authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.write, account.getId()));
        } else {
            // Editing child
            authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.write, account.getScopeId()));
        }

        //
        // Check existence
        Account oldAccount = find(account.getId());
        if (oldAccount == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, account.getId());
        }

        // check that expiration date is no later than parent expiration date
        Account parentAccount = null;
        if (oldAccount.getScopeId() != null) {
            parentAccount = KapuaSecurityUtils.doPrivileged(() -> find(oldAccount.getScopeId()));
        }
        if (parentAccount != null && parentAccount.getExpirationDate() != null) {
            // if parent account never expires no check is needed
            if (account.getExpirationDate() == null || parentAccount.getExpirationDate().before(account.getExpirationDate())) {
                // if current account expiration date is null it will be obviously after parent expiration date
                throw new KapuaIllegalArgumentException("expirationDate", account.getExpirationDate() != null ? account.getExpirationDate().toString() : "no expiration date set");
            }
        }

        // check that expiration date is after all the children account
        if (account.getExpirationDate() != null) {
            // if expiration date is null it means the account never expires, so it will be obviously later its children
            AccountListResult childrenAccounts = findChildsRecursively(account.getId());
            if (childrenAccounts.getItems().stream().anyMatch(childAccount -> {
                // if child account expiration date is null it will be obviously after current account expiration date
                return childAccount.getExpirationDate() == null || childAccount.getExpirationDate().after(account.getExpirationDate());
            })) {
                throw new KapuaIllegalArgumentException("expirationDate", account.getExpirationDate() != null ? account.getExpirationDate().toString() : "no expiration date set");
            }
        }

        //
        // Do update
        return entityManagerSession.onTransactedResult(em -> {

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
    //@RaiseServiceEvent
    public void delete(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(accountId, "scopeId");
        ArgumentValidator.notNull(scopeId, "accountId");

        //
        // Check Access
        Actions action = Actions.delete;
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, action, scopeId));

        //
        // Check if it has children
        if (!findChildAccountsTrusted(accountId).isEmpty()) {
            throw new KapuaAccountException(KapuaAccountErrorCodes.OPERATION_NOT_ALLOWED, null, "This account cannot be deleted. Delete its child first.");
        }

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> {
            // Entity needs to be loaded in the context of the same EntityManger to be able to delete it afterwards
            Account accountx = AccountDAO.find(em, scopeId, accountId);
            if (accountx == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, accountId);
            }

            // do not allow deletion of the kapua admin account
            SystemSetting settings = SystemSetting.getInstance();
            if (settings.getString(SystemSettingKey.SYS_PROVISION_ACCOUNT_NAME).equals(accountx.getName())) {
                throw new KapuaIllegalAccessException(action.name());
            }

            if (settings.getString(SystemSettingKey.SYS_ADMIN_USERNAME).equals(accountx.getName())) {
                throw new KapuaIllegalAccessException(action.name());
            }

            AccountDAO.delete(em, scopeId, accountId);
        });
    }

    @Override
    public Account find(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(accountId, "accountId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return findById(accountId);
    }

    @Override
    public Account find(KapuaId accountId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(accountId, "accountId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.read, accountId));

        //
        // Make sure account exists
        return findById(accountId);
    }

    @Override
    public Account findByName(String name) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notEmptyOrNull(name, "name");

        //
        // Do find
        return entityManagerSession.onResult(em -> {
            Account account = AccountDAO.findByName(em, name);

            //
            // Check Access
            if (account != null) {
                authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.read, account.getId()));
            }

            return account;
        });
    }

    @Override
    public AccountListResult findChildsRecursively(KapuaId scopeId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");

        //
        // Make sure account exists
        Account account = findById(scopeId);
        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, scopeId);
        }

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.read, account.getId()));

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
    public AccountListResult query(KapuaQuery<Account> query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.onResult(em -> AccountDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<Account> query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.onResult(em -> AccountDAO.count(em, query));
    }

    /**
     * Find an {@link Account} without authorization checks.
     *
     * @param accountId
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    private Account findById(KapuaId accountId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(accountId, "accountId");

        //
        // Do find
        return entityManagerSession.onResult(em -> AccountDAO.find(em, null, accountId));
    }

    private AccountListResult findChildAccountsTrusted(KapuaId accountId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(accountId, "accountId");
        ArgumentValidator.notNull(accountId.getId(), "accountId.id");

        //
        // Do find
        return entityManagerSession.onResult(em -> AccountDAO.query(em, new AccountQueryImpl(accountId)));
    }

    @Override
    protected Map<String, Object> getConfigValues(Account entity) throws KapuaException {
        return super.getConfigValues(entity.getId());
    }
}
