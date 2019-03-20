/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.test.account;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.Organization;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class AccountMock implements Account {

    private static final long serialVersionUID = 2950823934061491685L;

    private static long longId = 1;

    private KapuaId id;
    private KapuaId scopeId;
    private String name;

    public AccountMock(KapuaId scopeId, String name) {
        setId(new KapuaEid(BigInteger.valueOf(longId++)));
        setScopeId(scopeId);
        setName(name);
    }

    @Override
    public KapuaId getId() {
        return this.id;
    }

    @Override
    public void setId(KapuaId id) {
        this.id = KapuaEid.parseKapuaId(id);
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = KapuaEid.parseKapuaId(scopeId);
    }

    @Override
    public KapuaId getScopeId() {
        return this.scopeId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDescription(String description) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getModifiedOn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getModifiedBy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getOptlock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOptlock(int optlock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getEntityAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEntityAttributes(Properties props) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getEntityProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEntityProperties(Properties props) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getCreatedOn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getCreatedBy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Organization getOrganization() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOrganization(Organization organization) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getParentAccountPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setParentAccountPath(String parentAccountPath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Account> getChildAccounts() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getExpirationDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setExpirationDate(Date expirationDate) {
        throw new UnsupportedOperationException();
    }
}
