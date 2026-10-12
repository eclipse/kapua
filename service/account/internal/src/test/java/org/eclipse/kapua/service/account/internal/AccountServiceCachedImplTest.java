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
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.commons.cache.CacheConfig;
import org.eclipse.kapua.commons.cache.ExpiryPolicy;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountRepository;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.storage.TxContext;
import org.eclipse.kapua.storage.TxManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.BiConsumer;

@Category(JUnitTests.class)
public class AccountServiceCachedImplTest {

    private static final String ACCOUNT_NAME = "account";
    private static final KapuaId SCOPE_ID = new KapuaEid(BigInteger.ONE);
    private static final KapuaId ACCOUNT_ID = new KapuaEid(BigInteger.TEN);

    private AccountRepository accountRepository;
    private AuthorizationService authorizationService;
    private Account account;

    @Before
    public void setUp() {
        KapuaSession.createFrom(SCOPE_ID, new KapuaEid(BigInteger.valueOf(2)));

        accountRepository = Mockito.mock(AccountRepository.class);
        authorizationService = Mockito.mock(AuthorizationService.class);

        account = Mockito.mock(Account.class);
        Mockito.when(account.getId()).thenReturn(ACCOUNT_ID);
        Mockito.when(account.getScopeId()).thenReturn(SCOPE_ID);
    }

    @After
    public void tearDown() {
        KapuaSecurityUtils.clearSession();
    }

    @Test
    public void findByNameCachedTest() throws Exception {
        Mockito.when(accountRepository.findByName(Mockito.any(TxContext.class), Mockito.eq(ACCOUNT_NAME))).thenReturn(Optional.of(account));
        AccountServiceCachedImpl accountService = newAccountService(true);

        Assert.assertSame(account, accountService.findByName(ACCOUNT_NAME));
        Assert.assertSame(account, accountService.findByName(ACCOUNT_NAME));

        // Repository and permission check are hit only once
        Mockito.verify(accountRepository, Mockito.times(1)).findByName(Mockito.any(TxContext.class), Mockito.eq(ACCOUNT_NAME));
        Mockito.verify(authorizationService, Mockito.times(1)).checkPermission(Mockito.any(Permission.class));
    }

    @Test
    public void findByNameNotFoundNotCachedTest() throws Exception {
        Mockito.when(accountRepository.findByName(Mockito.any(TxContext.class), Mockito.eq(ACCOUNT_NAME))).thenReturn(Optional.empty(), Optional.of(account));
        AccountServiceCachedImpl accountService = newAccountService(true);

        Assert.assertNull(accountService.findByName(ACCOUNT_NAME));
        // Created after the first lookup: must be found
        Assert.assertSame(account, accountService.findByName(ACCOUNT_NAME));
        Assert.assertSame(account, accountService.findByName(ACCOUNT_NAME));

        Mockito.verify(accountRepository, Mockito.times(2)).findByName(Mockito.any(TxContext.class), Mockito.eq(ACCOUNT_NAME));
    }

    @Test
    public void findByNameErrorNotCachedTest() throws Exception {
        Mockito.when(accountRepository.findByName(Mockito.any(TxContext.class), Mockito.eq(ACCOUNT_NAME)))
                .thenThrow(new RuntimeException("test"))
                .thenReturn(Optional.of(account));
        AccountServiceCachedImpl accountService = newAccountService(true);

        try {
            accountService.findByName(ACCOUNT_NAME);
            Assert.fail("Exception expected");
        } catch (RuntimeException e) {
            // expected
        }
        Assert.assertSame(account, accountService.findByName(ACCOUNT_NAME));

        Mockito.verify(accountRepository, Mockito.times(2)).findByName(Mockito.any(TxContext.class), Mockito.eq(ACCOUNT_NAME));
    }

    @Test
    public void findByNameCacheDisabledTest() throws Exception {
        Mockito.when(accountRepository.findByName(Mockito.any(TxContext.class), Mockito.eq(ACCOUNT_NAME))).thenReturn(Optional.of(account));
        AccountServiceCachedImpl accountService = newAccountService(false);

        Assert.assertSame(account, accountService.findByName(ACCOUNT_NAME));
        Assert.assertSame(account, accountService.findByName(ACCOUNT_NAME));

        Mockito.verify(accountRepository, Mockito.times(2)).findByName(Mockito.any(TxContext.class), Mockito.eq(ACCOUNT_NAME));
    }

    @Test(expected = KapuaIllegalNullArgumentException.class)
    public void findByNameNullTest() throws Exception {
        newAccountService(true).findByName(null);
    }

    //
    // Private methods
    //

    private AccountServiceCachedImpl newAccountService(boolean cacheEnabled) {
        CacheConfig cacheConfig = new CacheConfig(cacheEnabled ? 100 : 0, 3600, ExpiryPolicy.MODIFIED);
        return new AccountServiceCachedImpl(
                new PassThroughTxManager(),
                accountRepository,
                Mockito.mock(PermissionFactory.class),
                authorizationService,
                null,
                null,
                null,
                new LocalCache<>(cacheConfig, null));
    }

    private static class PassThroughTxManager implements TxManager {

        @Override
        @SafeVarargs
        public final <R> R execute(TxConsumer<R> transactionConsumer, BiConsumer<TxContext, R>... justBeforeCommitAdditionalTxConsumers) throws KapuaException {
            return transactionConsumer.execute(Mockito.mock(TxContext.class));
        }

        @Override
        public TxContext getTxContext() {
            return Mockito.mock(TxContext.class);
        }
    }
}
