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
        this.id = new KapuaEid(BigInteger.valueOf(longId++));
        this.scopeId = scopeId;
        this.name = name;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        // TODO Auto-generated method stub

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
        return null;
    }

    @Override
    public void setDescription(String description) {
        // Not used
    }

    @Override
    public Date getModifiedOn() {
        return null;
    }

    @Override
    public KapuaId getModifiedBy() {
        return null;
    }

    @Override
    public int getOptlock() {
        return 0;
    }

    @Override
    public void setOptlock(int optlock) {
        // Not used
    }

    @Override
    public Properties getEntityAttributes() {
        return null;
    }

    @Override
    public void setEntityAttributes(Properties props) {
        // Not used
    }

    @Override
    public Properties getEntityProperties() {
        return null;
    }

    @Override
    public void setEntityProperties(Properties props) {
        // Not used
    }

    @Override
    public KapuaId getId() {
        return this.id;
    }

    @Override
    public void setId(KapuaId id) {
        // Not used
    }

    @Override
    public KapuaId getScopeId() {
        return this.scopeId;
    }

    @Override
    public Date getCreatedOn() {
        return null;
    }

    @Override
    public KapuaId getCreatedBy() {
        return null;
    }

    @Override
    public Organization getOrganization() {
        return null;
    }

    @Override
    public void setOrganization(Organization organization) {
        // Not used
    }

    @Override
    public String getParentAccountPath() {
        return null;
    }

    @Override
    public void setParentAccountPath(String parentAccountPath) {
        // Not used
    }

    @Override
    public List<Account> getChildAccounts() {
        return null;
    }

    @Override
    public Date getExpirationDate() {
        return null;
    }

    @Override
    public void setExpirationDate(Date expirationDate) {
        // Not used
    }
}
