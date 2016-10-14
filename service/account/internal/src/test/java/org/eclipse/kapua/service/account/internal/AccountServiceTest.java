/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.account.internal;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.Organization;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ignoring those tests until we have build fixed. Then we should enabled embedded MariaDB and turn tests on again.
 */
@Ignore
public class AccountServiceTest extends AbstractAccountServiceTest
{
    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(AccountServiceTest.class);

    @Test
    public void testCreate()
        throws Exception
    {
        // KapuaPeid accountPeid = KapuaEidGenerator.generate();//
        KapuaId scopeId = new KapuaEid(BigInteger.valueOf(1));

        KapuaLocator locator = KapuaLocator.getInstance();

        long accountSerial = (new Date()).getTime();
        AccountCreator accountCreator = this.getTestAccountCreator(scopeId, accountSerial);

        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.create(accountCreator);

        //
        // Account asserts
        assertNotNull(account);
        assertNotNull(account.getId());
        assertNotNull(account.getId().getId());
        assertTrue(account.getOptlock() >= 0);
        assertTrue(account.getScopeId().equals(scopeId));
        assertTrue(account.getName().equals(accountCreator.getOrganizationName()));
        assertNotNull(account.getOrganization());
        assertTrue(account.getOrganization().getEmail().equals(accountCreator.getOrganizationEmail()));
        assertNotNull(account.getCreatedOn());
        assertNotNull(account.getCreatedBy());
        assertNotNull(account.getModifiedOn());
        assertNotNull(account.getModifiedBy());
    }

    @Test
    public void testUpdate()
        throws Exception
    {
        // KapuaPeid accountPeid = KapuaEidGenerator.generate();//
        KapuaId scopeId = new KapuaEid(BigInteger.valueOf(1));

        KapuaLocator locator = KapuaLocator.getInstance();

        long accountSerial = (new Date()).getTime();
        AccountCreator accountCreator = this.getTestAccountCreator(scopeId, accountSerial);

        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.create(accountCreator);

        account = accountService.find(account.getScopeId(), account.getId());

        Organization org = account.getOrganization();
        org.setAddressLine1("5th evenue, NY");
        account.setOrganization(org);

        Account accountUpd = accountService.update(account);

        //
        // Account asserts
        assertNotNull(accountUpd);
        assertNotNull(accountUpd.getId());
        assertNotNull(accountUpd.getId().getId());
        assertTrue(accountUpd.getOptlock() >= 0);
        assertTrue(accountUpd.getId().equals(account.getId()));
        assertTrue(accountUpd.getScopeId().equals(account.getScopeId()));
        assertTrue(accountUpd.getName().equals(account.getName()));
        assertNotNull(accountUpd.getOrganization());
        assertTrue(accountUpd.getOrganization().getAddressLine1().equals(org.getAddressLine1()));
        assertNotNull(accountUpd.getCreatedOn().equals(account.getCreatedOn()));
        assertNotNull(accountUpd.getCreatedBy().equals(account.getCreatedBy()));
        assertNotNull(account.getModifiedOn());
        assertNotNull(account.getModifiedBy());
    }

    @Test
    public void testDelete()
        throws Exception
    {
        // KapuaPeid accountPeid = KapuaEidGenerator.generate();//
        KapuaId scopeId = new KapuaEid(BigInteger.valueOf(1));

        KapuaLocator locator = KapuaLocator.getInstance();

        long accountSerial = (new Date()).getTime();
        AccountCreator accountCreator = this.getTestAccountCreator(scopeId, accountSerial);

        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.create(accountCreator);

        accountService.delete(scopeId, account.getId());

        try {
            account = accountService.find(account.getScopeId(), account.getId());
        }
        catch (KapuaEntityNotFoundException ex) {
            account = null;
        }

        assertTrue(account == null);
    }

    @Test
    public void testFind()
        throws Exception
    {
        // KapuaPeid accountPeid = KapuaEidGenerator.generate();//
        KapuaId scopeId = new KapuaEid(BigInteger.valueOf(1));

        KapuaLocator locator = KapuaLocator.getInstance();

        long accountSerial = (new Date()).getTime();
        AccountCreator accountCreator = this.getTestAccountCreator(scopeId, accountSerial);

        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.create(accountCreator);

        account = accountService.find(account.getScopeId(), account.getId());

        //
        // Account asserts
        assertNotNull(account);
        assertNotNull(account.getId());
        assertNotNull(account.getId().getId());
        assertTrue(account.getOptlock() >= 0);
        assertTrue(account.getScopeId().equals(accountCreator.getScopeId()));
        assertTrue(account.getName().equals(accountCreator.getName()));
        assertNotNull(account.getOrganization());
        assertTrue(account.getOrganization().getEmail().equals(accountCreator.getOrganizationEmail()));
    }

    @Test
    public void testConfiguration()
        throws Exception
    {
        // KapuaPeid accountPeid = KapuaEidGenerator.generate();//
        KapuaId scopeId = new KapuaEid(BigInteger.valueOf(1));

        KapuaLocator locator = KapuaLocator.getInstance();

        AccountService accountService = locator.getService(AccountService.class);
        KapuaTocd ocd = accountService.getConfigMetadata();
        Map<String, Object> values = accountService.getConfigValues(scopeId);
        accountService.setConfigValues(scopeId, values);

        assertTrue(null == null);
    }

    private AccountCreator getTestAccountCreator(KapuaId scopeId, long random)
    {
        KapuaLocator locator = KapuaLocator.getInstance();

        long accountSerial = (new Date()).getTime();
        String testAccount = String.format("test-%d", accountSerial);
        AccountFactory accountFactory = locator.getFactory(AccountFactory.class);
        AccountCreator accountCreator = accountFactory.newAccountCreator(scopeId, testAccount);

        accountCreator.setAccountPassword("!aA1234567890");
        accountCreator.setOrganizationName(testAccount);
        accountCreator.setOrganizationEmail(String.format("theuser@%s.com", testAccount));

        return accountCreator;
    }
}
