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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;

/**
 * {@link DomainFactory} implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class DomainFactoryImpl implements DomainFactory {

    @Override
    public DomainCreator newCreator(String name, String serviceName) {
        return new DomainCreatorImpl(name, serviceName);
    }

    @Override
    public DomainListResult newDomainListResult() {
        return new DomainListResultImpl();
    }

    @Override
    public DomainQuery newQuery() {
        return new DomainQueryImpl();
    }

}
