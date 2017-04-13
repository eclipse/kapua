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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro;

import javax.inject.Singleton;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;

@Singleton
public class DomainServiceTestData {

    Domain domain;
    DomainCreator domainCreator;
    DomainListResult domainList;
    KapuaId domainId;
    long initial_count;

    public void clearData() {
        domain = null;
        domainCreator = null;
        domainList = null;
        domainId = null;
        initial_count = 0;
    }
}
