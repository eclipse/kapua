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
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
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
import org.eclipse.kapua.service.account.internal.exception.KapuaAccountErrorCodes;
import org.eclipse.kapua.service.account.internal.exception.KapuaAccountException;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
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

    private static final String NO_EXPIRATION_DATE_SET = "no expiration date set";

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public AccountServiceImpl() {
        super(AccountService.class.getName(), AccountDomains.ACCOUNT_DOMAIN, AccountEntityManagerFactory.getInstance(), AccountCacheFactory.getInstance(), AccountService.class, AccountFactory.class);
    }

    @Override
    //@RaiseServiceEvent
    public Account create(AccountCreator accountCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(accountCreator, "accountCreator");
        ArgumentValidator.notNull(accountCreator.getScopeId(), "accountCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(accountCreator.getName(), "accountCreator.name");
        ArgumentValidator.match(accountCreator.getName(), CommonsValidationRegex.NAME_REGEXP, "accountCreator.name");
        ArgumentValidator.notEmptyOrNull(accountCreator.getOrganizationName(), "accountCreator.organizationName");
        ArgumentValidator.notEmptyOrNull(accountCreator.getOrganizationEmail(), "accountCreator.organizationEmail");
        ArgumentValidator.match(accountCreator.getOrganizationEmail(), CommonsValidationRegex.EMAIL_REGEXP, "accountCreator.organizationEmail");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.write, accountCreator.getScopeId()));

        //
        // Check entity limit
        checkAllowedEntities(accountCreator.getScopeId(), "Accounts");

        //
        // Check if the parent account exists
        if (find(accountCreator.getScopeId()) == null) {
            throw new KapuaIllegalArgumentException(KapuaEntityAttributes.SCOPE_ID, "parent account does not exist: " + accountCreator.getScopeId() + "::");
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
        query.setPredicate(query.attributePredicate(KapuaNamedEntityAttributes.NAME, accountCreator.getName()));

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
                throw new KapuaIllegalArgumentException(AccountAttributes.EXPIRATION_DATE, accountCreator.getExpirationDate() != null ? accountCreator.getExpirationDate().toString() : NO_EXPIRATION_DATE_SET);
            }
        }

        //
        // Do create
        return entityManagerSession.doTransactedAction(
                EntityManagerContainer.<Account>create()
                        .onResultHandler(em -> {
                            Account account = AccountDAO.create(em, accountCreator);
                            em.persist(account);

                            // Set the parent account path
                            String parentAccountPath = AccountDAO.find(em, KapuaId.ANY, accountCreator.getScopeId()).getParentAccountPath() + "/" + account.getId();
                            account.setParentAccountPath(parentAccountPath);

                            // Return updated account
                            return AccountDAO.update(em, account);
                        }));
    }

    @Override
    //@RaiseServiceEvent
    public Account update(Account account) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(account.getId(), "account.id");
        ArgumentValidator.notEmptyOrNull(account.getName(), "account.name");
        ArgumentValidator.match(account.getName(), CommonsValidationRegex.NAME_REGEXP, "account.name");
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

        if (account.getExpirationDate() != null) {
            SystemSetting setting = SystemSetting.getInstance();
            //check if the updated account is an admin account
            if (setting.getString(SystemSettingKey.SYS_ADMIN_ACCOUNT).equals(account.getName())) {
                //throw exception if trying to set an expiration date for an admin account
                throw new KapuaAccountException(KapuaAccountErrorCodes.OPERATION_NOT_ALLOWED, null, "Admin account cannot have an expiration date set");
            }
        }

        //
        // Check existence
        Account oldAccount = find(account.getId());
        if (oldAccount == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, account.getId());
        }

        //
        // Check if user tries to change expiration date of the account in which it is defined (the account is not the admin one considering previous checks)
        if (KapuaSecurityUtils.getSession().getScopeId().equals(account.getId())) {
            // Editing self - aka user that edits its account
             if ( (oldAccount.getExpirationDate() == null && account.getExpirationDate() != null) || //old exp. date was "no expiration" and now the update restricts it
                     (oldAccount.getExpirationDate() != null && ! oldAccount.getExpirationDate().equals(account.getExpirationDate())) ) { //old exp. date was some date and the update refers to another date
                 // Editing the expiration date
                 throw new KapuaAccountException(KapuaAccountErrorCodes.OPERATION_NOT_ALLOWED, null, "A user cannot modify expiration date of the account in which it's defined");
             }
        }

        //
        // Check that expiration date is no later than parent expiration date
        Account parentAccount = null;
        if (oldAccount.getScopeId() != null) {
            parentAccount = KapuaSecurityUtils.doPrivileged(() -> find(oldAccount.getScopeId()));
        }

        if (parentAccount != null && parentAccount.getExpirationDate() != null) {
            // if parent account never expires no check is needed
            if (account.getExpirationDate() == null || parentAccount.getExpirationDate().before(account.getExpirationDate())) {
                // if current account expiration date is null it will be obviously after parent expiration date
                throw new KapuaIllegalArgumentException(AccountAttributes.EXPIRATION_DATE, account.getExpirationDate() != null ? account.getExpirationDate().toString() : NO_EXPIRATION_DATE_SET);
            }
        }

        if (account.getExpirationDate() != null) {
            // check that expiration date is after all the children account
            // if expiration date is null it means the account never expires, so it will be obviously later its children
            AccountListResult childrenAccounts = findChildrenRecursively(account.getId());
            // if child account expiration date is null it will be obviously after current account expiration date
            if (childrenAccounts.getItems().stream().anyMatch(childAccount -> childAccount.getExpirationDate() == null || childAccount.getExpirationDate().after(account.getExpirationDate()))) {
                throw new KapuaIllegalArgumentException(AccountAttributes.EXPIRATION_DATE, account.getExpirationDate() != null ? account.getExpirationDate().toString() : NO_EXPIRATION_DATE_SET);
            }
        }

        //
        // Verify unchanged parent account ID and parent account path
        if (!Objects.equals(oldAccount.getScopeId(), account.getScopeId())) {
            throw new KapuaIllegalArgumentException("account.scopeId", account.getScopeId().toStringId());
        }
        if (!oldAccount.getParentAccountPath().equals(account.getParentAccountPath())) {
            throw new KapuaIllegalArgumentException("account.parentAccountPath", account.getParentAccountPath());
        }
        if (!oldAccount.getName().equals(account.getName())) {
            throw new KapuaIllegalArgumentException("account.name", account.getName());
        }

        //
        // Do update
        return entityManagerSession.doTransactedAction(
                EntityManagerContainer.<Account>create()
                        .onBeforeHandler(() -> {
                            entityCache.remove(null, account);
                            return null;
                        })
                        .onResultHandler(em -> AccountDAO.update(em, account))
        );
    }

    @Override
    //@RaiseServiceEvent
    public void delete(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accountId, KapuaEntityAttributes.ENTITY_ID);

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
        entityManagerSession.doTransactedAction(
                EntityManagerContainer.<Account>create()
                        .onResultHandler(em -> {
                            Account account = scopeId.equals(accountId) ?
                                    AccountDAO.find(em, KapuaId.ANY, accountId) :
                                    AccountDAO.find(em, scopeId, accountId);

                            if (account == null) {
                                throw new KapuaEntityNotFoundException(Account.TYPE, accountId);
                            }

                            // do not allow deletion of the kapua admin account
                            SystemSetting settings = SystemSetting.getInstance();
                            if (settings.getString(SystemSettingKey.SYS_PROVISION_ACCOUNT_NAME).equals(account.getName())) {
                                throw new KapuaIllegalAccessException(action.name());
                            }

                            if (settings.getString(SystemSettingKey.SYS_ADMIN_USERNAME).equals(account.getName())) {
                                throw new KapuaIllegalAccessException(action.name());
                            }

                            return AccountDAO.delete(em, scopeId, accountId);
                        })
                        .onAfterHandler((emptyParam) -> entityCache.remove(scopeId, accountId)));
    }

    @Override
    public Account find(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accountId, KapuaEntityAttributes.ENTITY_ID);

        // If looking for a ScopeId equals to Account KapuaId, we are looking for our Account,
        // so we need to use the un-scoped find (which checks for permissions).
        // Otherwise, we will never find our account on our scope.
        // In our scope we will find only our direct child Accounts
        if (scopeId.equals(accountId)) {
            return find(accountId);
        }

        //
        // Check Access
        checkAccountPermission(scopeId, accountId, AccountDomains.ACCOUNT_DOMAIN, Actions.read);

        //
        // Do find
        return entityManagerSession.doAction(
                EntityManagerContainer.<Account>create()
                        .onBeforeHandler(() -> (Account) entityCache.get(scopeId, accountId))
                        .onResultHandler(em -> AccountDAO.find(em, scopeId, accountId))
                        .onAfterHandler(entityCache::put)
        );
    }

    @Override
    public Account find(KapuaId accountId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(accountId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Do find
        Account account = entityManagerSession.doAction(
                EntityManagerContainer.<Account>create()
                        .onBeforeHandler(() -> (Account) entityCache.get(null, accountId))
                        .onResultHandler(em -> AccountDAO.find(em, KapuaId.ANY, accountId))
                        .onAfterHandler(entityCache::put)
        );

        //
        // Check Access
        if (account != null) {
            checkAccountPermission(account.getScopeId(), account.getId(), AccountDomains.ACCOUNT_DOMAIN, Actions.read);
        }

        //
        // Return result
        return account;
    }

    @Override
    public Account findByName(String name) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notEmptyOrNull(name, "name");

        //
        // Do find
        Account account = entityManagerSession.doAction(
                EntityManagerContainer.<Account>create()
                        .onBeforeHandler(() -> (Account) ((NamedEntityCache) entityCache).get(null, name))
                        .onResultHandler(em -> AccountDAO.findByName(em, name))
                        .onAfterHandler(entityCache::put));

        //
        // Check access
        if (account != null) {
            checkAccountPermission(account.getScopeId(), account.getId(), AccountDomains.ACCOUNT_DOMAIN, Actions.read);
        }

        //
        // Return result
        return account;
    }

    @Override
    public AccountListResult findChildrenRecursively(KapuaId scopeId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);

        //
        // Check Access
        Account account = find(scopeId);

        //
        // Make sure account exists
        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, scopeId);
        }

        //
        // Check access
        checkAccountPermission(account.getScopeId(), account.getId(), AccountDomains.ACCOUNT_DOMAIN, Actions.read, true);

        //
        // Do find
        return entityManagerSession.doAction(
                EntityManagerContainer.<AccountListResult>create()
                        .onResultHandler(em -> {
                            TypedQuery<Account> q = em.createNamedQuery("Account.findChildAccountsRecursive", Account.class);
                            q.setParameter("parentAccountPath", "\\" + account.getParentAccountPath() + "/%");

                            AccountListResult result = new AccountListResultImpl();
                            result.addItems(q.getResultList());
                            return result;
                        }));
    }

    @Override
    public AccountListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(
                EntityManagerContainer.<AccountListResult>create()
                        .onResultHandler(em -> AccountDAO.query(em, query))
        );
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AccountDomains.ACCOUNT_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(
                EntityManagerContainer.<Long>create()
                        .onResultHandler(em -> AccountDAO.count(em, query))
        );
    }

    private AccountListResult findChildAccountsTrusted(KapuaId accountId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(accountId, KapuaEntityAttributes.ENTITY_ID);
        ArgumentValidator.notNull(accountId.getId(), "accountId.id");

        //
        // Do find
        return entityManagerSession.doAction(
                EntityManagerContainer.<AccountListResult>create()
                        .onResultHandler(em -> AccountDAO.query(em, new AccountQueryImpl(accountId)))
        );
    }

    private void checkAccountPermission(KapuaId scopeId, KapuaId accountId, Domain domain, Actions action) throws KapuaException {
        checkAccountPermission(scopeId, accountId, domain, action, false);
    }

    /**
     * Checks if the current session can retrieve the {@link Account}, by both having an explicit permission or because
     * it's looking for its own {@link Account}
     *
     * @param accountId The {@link KapuaId} of the {@link Account} to look for
     */
    private void checkAccountPermission(KapuaId scopeId, KapuaId accountId, Domain domain, Actions action, boolean forwardable) throws KapuaException {
        if (KapuaSecurityUtils.getSession().getScopeId().equals(accountId)) {
            // I'm looking for myself, so let's check if I have the correct permission
            authorizationService.checkPermission(permissionFactory.newPermission(domain, action, accountId, null, forwardable));
        } else {
            // I'm looking for another account, so I need to check the permission on the account scope
            authorizationService.checkPermission(permissionFactory.newPermission(domain, action, scopeId, null, forwardable));
        }
    }
}
