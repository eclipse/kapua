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
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceBase;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.jpa.EventStorer;
import org.eclipse.kapua.commons.model.domains.Domains;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountAttributes;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountRepository;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.exception.KapuaAccountErrorCodes;
import org.eclipse.kapua.service.account.internal.exception.KapuaAccountException;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;
import java.util.Optional;

/**
 * {@link AccountService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class AccountServiceImpl
        extends KapuaConfigurableServiceBase
        implements AccountService {

    private static final String NO_EXPIRATION_DATE_SET = "no expiration date set";
    private final AccountRepository accountRepository;
    private final EventStorer eventStorer;

    /**
     * Injectable constructor
     *
     * @param accountRepository           The {@link AccountRepository} instance
     * @param permissionFactory           The {@link PermissionFactory} instance
     * @param authorizationService        The {@link AuthorizationService} instance
     * @param serviceConfigurationManager The {@link ServiceConfigurationManager} instance
     * @param eventStorer
     * @since 2.0.0
     */
    @Inject
    public AccountServiceImpl(
            TxManager txManager,
            AccountRepository accountRepository,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            ServiceConfigurationManager serviceConfigurationManager,
            EventStorer eventStorer) {
        super(txManager, serviceConfigurationManager, Domains.ACCOUNT, authorizationService, permissionFactory);
        this.accountRepository = accountRepository;
        this.eventStorer = eventStorer;
    }

    @Override
    public Account create(AccountCreator accountCreator) throws KapuaException {
        // Argument validation
        ArgumentValidator.notNull(accountCreator, "accountCreator");
        ArgumentValidator.notNull(accountCreator.getScopeId(), "accountCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(accountCreator.getName(), "accountCreator.name");
        ArgumentValidator.match(accountCreator.getName(), CommonsValidationRegex.NAME_REGEXP, "accountCreator.name");
        ArgumentValidator.notEmptyOrNull(accountCreator.getOrganizationName(), "accountCreator.organizationName");
        ArgumentValidator.notEmptyOrNull(accountCreator.getOrganizationEmail(), "accountCreator.organizationEmail");
        ArgumentValidator.match(accountCreator.getOrganizationEmail(), CommonsValidationRegex.EMAIL_REGEXP, "accountCreator.organizationEmail");

        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.ACCOUNT, Actions.write, accountCreator.getScopeId()));

        return txManager.execute(tx -> {
            // Check entity limit
            serviceConfigurationManager.checkAllowedEntities(tx, accountCreator.getScopeId(), "Accounts");
            // Check if the parent account exists
            final Account parentAccount = accountRepository.find(tx, KapuaId.ANY, accountCreator.getScopeId())
                    .orElseThrow(() -> new KapuaIllegalArgumentException(KapuaEntityAttributes.SCOPE_ID, "parent account does not exist: " + accountCreator.getScopeId() + "::"));
            // check if the account collides with the SystemSettingKey#COMMONS_CONTROL_TOPIC_CLASSIFIER
            if (!StringUtils.isEmpty(SystemSetting.getInstance().getMessageClassifier())) {
                if (SystemSetting.getInstance().getMessageClassifier().equals(accountCreator.getName())) {
                    throw new KapuaIllegalArgumentException("name", "Reserved account name");// obfuscate this message? or change to something more clear like "the account name collides with some system
                    // configuration parameter"?
                }
            }
            // Check duplicate name
            if (accountRepository.countEntitiesWithNameInScope(tx, accountCreator.getScopeId(), accountCreator.getName()) > 0) {
                throw new KapuaDuplicateNameException(accountCreator.getName());
            }
            if (accountRepository.countEntitiesWithName(tx, accountCreator.getName()) > 0) {
                throw new KapuaDuplicateNameInAnotherAccountError(accountCreator.getName());
            }
            // Check that expiration date is no later than parent expiration date
            if (Optional.ofNullable(parentAccount.getExpirationDate()).isPresent()) {
                // parent account never expires no check is needed
                if (accountCreator.getExpirationDate() == null || parentAccount.getExpirationDate().before(accountCreator.getExpirationDate())) {
                    // if current account expiration date is null it will be obviously after parent expiration date
                    throw new KapuaIllegalArgumentException(AccountAttributes.EXPIRATION_DATE, accountCreator.getExpirationDate() != null ? accountCreator.getExpirationDate().toString() : NO_EXPIRATION_DATE_SET);
                }
            }
            // Do create
            final OrganizationImpl organizationImpl = new OrganizationImpl();
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

            final Account accountImpl = new AccountImpl(accountCreator.getScopeId(), accountCreator.getName());
            accountImpl.setOrganization(organizationImpl);
            accountImpl.setExpirationDate(accountCreator.getExpirationDate());
            final Account createdAccount = accountRepository.create(tx, accountImpl);
            String parentAccountPath = parentAccount.getParentAccountPath() + "/" + createdAccount.getId();
            createdAccount.setParentAccountPath(parentAccountPath);
            return accountRepository.update(tx, createdAccount);
        });
    }

    @Override
    public Account update(Account account) throws KapuaException {
        // Argument validation
        ArgumentValidator.notNull(account.getId(), "account.id");
        ArgumentValidator.notEmptyOrNull(account.getName(), "account.name");
        ArgumentValidator.match(account.getName(), CommonsValidationRegex.NAME_REGEXP, "account.name");
        ArgumentValidator.notNull(account.getOrganization(), "account.organization");
        ArgumentValidator.match(account.getOrganization().getEmail(), CommonsValidationRegex.EMAIL_REGEXP, "account.organization.email");

        // Check Access
        if (KapuaSecurityUtils.getSession().getScopeId().equals(account.getId())) {
            // Editing self
            authorizationService.checkPermission(permissionFactory.newPermission(Domains.ACCOUNT, Actions.write, account.getId()));
        } else {
            // Editing child
            authorizationService.checkPermission(permissionFactory.newPermission(Domains.ACCOUNT, Actions.write, account.getScopeId()));
        }

        if (account.getExpirationDate() != null) {
            SystemSetting setting = SystemSetting.getInstance();
            //check if the updated account is an admin account
            if (setting.getString(SystemSettingKey.SYS_ADMIN_ACCOUNT).equals(account.getName())) {
                //throw exception if trying to set an expiration date for an admin account
                throw new KapuaAccountException(KapuaAccountErrorCodes.OPERATION_NOT_ALLOWED, null, "Admin account cannot have an expiration date set");
            }
        }

        return txManager.execute(tx -> {
            // Check existence
            Account oldAccount = accountRepository.find(tx, KapuaId.ANY, account.getId())
                    .orElseThrow(() -> new KapuaEntityNotFoundException(Account.TYPE, account.getId()));
            // Check if user tries to change expiration date of the account in which it is defined (the account is not the admin one considering previous checks)
            if (KapuaSecurityUtils.getSession().getScopeId().equals(account.getId())) {
                // Editing self - aka user that edits its account
                if ((oldAccount.getExpirationDate() == null && account.getExpirationDate() != null) || //old exp. date was "no expiration" and now the update restricts it
                        (oldAccount.getExpirationDate() != null && !oldAccount.getExpirationDate().equals(account.getExpirationDate()))) { //old exp. date was some date and the update refers to another date
                    // Editing the expiration date
                    throw new KapuaAccountException(KapuaAccountErrorCodes.OPERATION_NOT_ALLOWED, null, "A user cannot modify expiration date of the account in which it's defined");
                }
            }
            // Check that expiration date is no later than parent expiration date
            Optional<Account> parentAccount = Optional.empty();
            if (oldAccount.getScopeId() != null) {
                parentAccount = accountRepository.find(tx, KapuaId.ANY, oldAccount.getScopeId());
            }

            if (parentAccount.flatMap(pa -> Optional.ofNullable(pa.getExpirationDate())).isPresent()) {
                // if parent account never expires no check is needed
                if (account.getExpirationDate() == null || parentAccount.get().getExpirationDate().before(account.getExpirationDate())) {
                    // if current account expiration date is null it will be obviously after parent expiration date
                    throw new KapuaIllegalArgumentException(AccountAttributes.EXPIRATION_DATE, account.getExpirationDate() != null ? account.getExpirationDate().toString() : NO_EXPIRATION_DATE_SET);
                }
            }

            if (account.getExpirationDate() != null) {
                // check that expiration date is after all the children account
                // if expiration date is null it means the account never expires, so it will be obviously later its children
                AccountListResult childrenAccounts = accountRepository.findChildAccountsRecursive(tx, account.getParentAccountPath());
                // if child account expiration date is null it will be obviously after current account expiration date
                if (childrenAccounts.getItems().stream().anyMatch(childAccount -> childAccount.getExpirationDate() == null || childAccount.getExpirationDate().after(account.getExpirationDate()))) {
                    throw new KapuaIllegalArgumentException(AccountAttributes.EXPIRATION_DATE, account.getExpirationDate() != null ? account.getExpirationDate().toString() : NO_EXPIRATION_DATE_SET);
                }
            }
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

            // Do update
            return accountRepository.update(tx, account);
        });
    }

    @Override
    //@RaiseServiceEvent
    public void delete(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        // Argument validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(accountId, KapuaEntityAttributes.ENTITY_ID);
        // Check Access
        Actions action = Actions.delete;
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.ACCOUNT, action, scopeId));
        // Check if it has children
        if (!findChildAccountsTrusted(accountId).isEmpty()) {
            throw new KapuaAccountException(KapuaAccountErrorCodes.OPERATION_NOT_ALLOWED, null, "This account cannot be deleted. Delete its child first.");
        }

        txManager.execute(
                tx -> {
                    // Do delete
                    final Account account = (scopeId.equals(accountId) ?
                            accountRepository.find(tx, KapuaId.ANY, accountId) :
                            accountRepository.find(tx, scopeId, accountId))
                            .orElseThrow(() -> new KapuaEntityNotFoundException(Account.TYPE, accountId));
                    // do not allow deletion of the kapua admin account
                    SystemSetting settings = SystemSetting.getInstance();
                    if (settings.getString(SystemSettingKey.SYS_PROVISION_ACCOUNT_NAME).equals(account.getName())) {
                        throw new KapuaIllegalAccessException(action.name());
                    }

                    if (settings.getString(SystemSettingKey.SYS_ADMIN_USERNAME).equals(account.getName())) {
                        throw new KapuaIllegalAccessException(action.name());
                    }

                    return accountRepository.delete(tx, account);
                },
                eventStorer::accept
        );
    }

    @Override
    public Account find(KapuaId scopeId, KapuaId accountId) throws KapuaException {
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

        // Check Access
        checkAccountPermission(scopeId, accountId, Domains.ACCOUNT, Actions.read);

        // Do find
        return txManager.execute(tx -> accountRepository.find(tx, scopeId, accountId))
                .orElse(null);
    }

    @Override
    public Account find(KapuaId accountId) throws KapuaException {
        // Argument validation
        ArgumentValidator.notNull(accountId, KapuaEntityAttributes.ENTITY_ID);

        // Do find
        Account account = txManager.execute(tx -> accountRepository.find(tx, KapuaId.ANY, accountId))
                .orElse(null);

        // Check Access
        if (account != null) {
            checkAccountPermission(account.getScopeId(), account.getId(), Domains.ACCOUNT, Actions.read);
        }

        // Return result
        return account;
    }

    @Override
    public Account findByName(String name) throws KapuaException {
        // Argument validation
        ArgumentValidator.notEmptyOrNull(name, "name");

        // Do find
        Account account = txManager.execute(tx -> accountRepository.findByName(tx, name))
                .orElse(null);

        // Check access
        if (account != null) {
            checkAccountPermission(account.getScopeId(), account.getId(), Domains.ACCOUNT, Actions.read);
        }

        // Return result
        return account;
    }

    @Override
    public AccountListResult findChildrenRecursively(KapuaId scopeId) throws KapuaException {
        // Argument validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);

        return txManager.execute(tx -> {
            // Check Access
            Account account = accountRepository.find(tx, KapuaId.ANY, scopeId)
                    // Make sure account exists
                    .orElseThrow(() -> new KapuaEntityNotFoundException(Account.TYPE, scopeId));

            // Check access
            checkAccountPermission(account.getScopeId(), account.getId(), Domains.ACCOUNT, Actions.read, true);

            // Do find
            return accountRepository.findChildAccountsRecursive(tx, account.getParentAccountPath());
        });
    }

    @Override
    public AccountListResult query(KapuaQuery query) throws KapuaException {
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.ACCOUNT, Actions.read, query.getScopeId()));

        // Do query
        return txManager.execute(tx -> accountRepository.query(tx, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        // Argument validation
        ArgumentValidator.notNull(query, "query");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(Domains.ACCOUNT, Actions.read, query.getScopeId()));
        // Do count
        return txManager.execute(tx -> accountRepository.count(tx, query));
    }

    private AccountListResult findChildAccountsTrusted(KapuaId accountId)
            throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(accountId, KapuaEntityAttributes.ENTITY_ID);
        ArgumentValidator.notNull(accountId.getId(), "accountId.id");
        // Do find
        return txManager.execute(tx -> accountRepository.query(tx, new AccountQueryImpl(accountId)));
    }

    private void checkAccountPermission(KapuaId scopeId, KapuaId accountId, String domain, Actions action) throws KapuaException {
        checkAccountPermission(scopeId, accountId, domain, action, false);
    }

    /**
     * Checks if the current session can retrieve the {@link Account}, by both having an explicit permission or because
     * it's looking for its own {@link Account}
     *
     * @param accountId The {@link KapuaId} of the {@link Account} to look for
     */
    private void checkAccountPermission(KapuaId scopeId, KapuaId accountId, String domain, Actions action, boolean forwardable) throws KapuaException {
        if (KapuaSecurityUtils.getSession().getScopeId().equals(accountId)) {
            // I'm looking for myself, so let's check if I have the correct permission
            authorizationService.checkPermission(permissionFactory.newPermission(domain, action, accountId, null, forwardable));
        } else {
            // I'm looking for another account, so I need to check the permission on the account scope
            authorizationService.checkPermission(permissionFactory.newPermission(domain, action, scopeId, null, forwardable));
        }
    }
}
