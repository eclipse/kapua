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
package org.eclipse.kapua.test.account;

import java.math.BigInteger;
import java.util.Date;
import java.util.Properties;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.Organization;

public class AccountMock implements Account {

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
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Date getModifiedOn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Subject getModifiedBy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getOptlock() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setOptlock(int optlock) {
        // TODO Auto-generated method stub

    }

    @Override
    public Properties getEntityAttributes()
            throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntityAttributes(Properties props)
            throws KapuaException {
        // TODO Auto-generated method stub

    }

    @Override
    public Properties getEntityProperties()
            throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntityProperties(Properties props)
            throws KapuaException {
        // TODO Auto-generated method stub

    }

    @Override
    public KapuaId getId() {
        return this.id;
    }

    @Override
    public KapuaId getScopeId() {
        return this.scopeId;
    }

    @Override
    public Date getCreatedOn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Subject getCreatedBy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Organization getOrganization() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOrganization(Organization organization) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getParentAccountPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setParentAccountPath(String parentAccountPath) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(KapuaId id) {
        this.id = new KapuaEid(id);
    }

}
