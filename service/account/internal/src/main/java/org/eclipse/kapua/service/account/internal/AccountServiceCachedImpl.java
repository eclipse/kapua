/*******************************************************************************
 * Copyright (c) 2026 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.jpa.EventStorer;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountRepository;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.storage.TxManager;

/**
 * {@link AccountService} implementation that caches {@link #findByName(String)} results in a {@link LocalCache}.
 * <p>
 * Meant for trusted, read-only callers on hot paths (i.e. message processing), that can tolerate stale {@link Account}s:
 * <ul>
 *     <li>Cached {@link Account}s are not invalidated when updated or deleted: they are returned as they were until their entry expires.</li>
 *     <li>Cached {@link Account}s are shared among callers: they must not be modified.</li>
 *     <li>Permissions are checked only when the {@link Account} is not cached.</li>
 *     <li>Not found {@link Account}s are never cached.</li>
 * </ul>
 * All other methods behave as in {@link AccountServiceImpl}.
 *
 * @since 2.1.0
 */
public class AccountServiceCachedImpl extends AccountServiceImpl {

    private final LocalCache<String, Account> accountsByNameCache; //BE AWARE that account could be staled. This is way less important for identifiers like its id, hence YMMV depending what you need from the account

    /**
     * Constructor.
     *
     * @param txManager                   The {@link TxManager}.
     * @param accountRepository           The {@link AccountRepository}.
     * @param permissionFactory           The {@link PermissionFactory}.
     * @param authorizationService        The {@link AuthorizationService}.
     * @param serviceConfigurationManager The {@link ServiceConfigurationManager}.
     * @param eventStorer                 The {@link EventStorer}.
     * @param accountMapper               The {@link AccountMapper}.
     * @param accountsByNameCache         The {@link LocalCache} of {@link Account}s by {@link Account#getName()}.
     * @since 2.1.0
     */
    public AccountServiceCachedImpl(
            TxManager txManager,
            AccountRepository accountRepository,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            ServiceConfigurationManager serviceConfigurationManager,
            EventStorer eventStorer,
            AccountMapper accountMapper,
            LocalCache<String, Account> accountsByNameCache) {
        super(txManager, accountRepository, permissionFactory, authorizationService, serviceConfigurationManager, eventStorer, accountMapper);
        this.accountsByNameCache = accountsByNameCache;
    }

    @Override
    public Account findByName(String name) throws KapuaException {
        // Argument validation
        ArgumentValidator.notEmptyOrNull(name, "name");

        // Check cache
        Account account = accountsByNameCache.get(name);
        if (account == null) {
            // Do find
            account = super.findByName(name);
            if (account != null) {
                accountsByNameCache.put(name, account);
            }
        }

        // Return result
        return account;
    }
}
