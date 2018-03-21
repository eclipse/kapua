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

import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainPredicates;
import org.eclipse.kapua.test.KapuaTest;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class DomainRegistryServiceTest extends KapuaTest {

    KapuaEid scope = new KapuaEid(IdGenerator.generate());

    // Tests

    @Test
    public void testCreate()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);

            Set<Actions> domainActions = new HashSet<>();
            domainActions.add(Actions.read);
            domainActions.add(Actions.write);

            DomainCreator domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainRegistryServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            DomainRegistryService domainRegistryService = locator.getService(DomainRegistryService.class);
            Domain domain = domainRegistryService.create(domainCreator);

            assertNotNull(domain);
            assertNotNull(domain.getId());
            assertNotNull(domain.getCreatedOn());
            assertNotNull(domain.getCreatedBy());
            assertEquals(domainCreator.getName(), domain.getName());
            assertEquals(domainCreator.getServiceName(), domain.getServiceName());

            assertNotNull(domain.getActions());
            assertEquals(domainCreator.getActions().size(), domain.getActions().size());

            for (Actions a : domainCreator.getActions()) {
                assertTrue(domain.getActions().contains(a));
            }

            return null;
        });
    }

    @Test
    public void testFind()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);

            Set<Actions> domainActions = new HashSet<>();
            domainActions.add(Actions.read);
            domainActions.add(Actions.write);

            DomainCreator domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainRegistryServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            DomainRegistryService domainRegistryService = locator.getService(DomainRegistryService.class);
            Domain domain = domainRegistryService.create(domainCreator);

            assertNotNull(domain);
            assertNotNull(domain.getId());

            Domain domainFound = domainRegistryService.find(null, domain.getId());

            assertNotNull(domainFound);
            assertEquals(domain.getId(), domainFound.getId());
            assertEquals(domain.getCreatedOn(), domainFound.getCreatedOn());
            assertEquals(domain.getCreatedBy(), domainFound.getCreatedBy());
            assertEquals(domain.getName(), domainFound.getName());
            assertEquals(domain.getServiceName(), domainFound.getServiceName());

            assertNotNull(domainFound.getActions());
            assertEquals(domain.getActions().size(), domainFound.getActions().size());

            for (Actions a : domain.getActions()) {
                assertTrue(domainFound.getActions().contains(a));
            }

            return null;
        });
    }

    @Test
    public void testQueryAndCount()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainRegistryService domainRegistryService = locator.getService(DomainRegistryService.class);

            long initialCount = domainRegistryService.count(domainFactory.newQuery(null));

            // Domain 1
            Set<Actions> domainActions = new HashSet<>();
            domainActions.add(Actions.read);
            domainActions.add(Actions.write);

            DomainCreator domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainRegistryServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            Domain domain1 = domainRegistryService.create(domainCreator);

            // Domain 2
            domainActions = new HashSet<>();
            domainActions.add(Actions.read);
            domainActions.add(Actions.write);

            domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainRegistryServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            Domain domain2 = domainRegistryService.create(domainCreator);

            //
            // Test query
            DomainQuery query = domainFactory.newQuery(null);
            DomainListResult result = domainRegistryService.query(query);
            long count = domainRegistryService.count(query);

            assertNotNull(result);
            assertEquals(initialCount + 2, count);
            assertEquals(count, result.getSize());

            //
            // Test name filtered query
            query = domainFactory.newQuery(null);

            query.setPredicate(new AttributePredicateImpl<>(DomainPredicates.NAME, domain1.getName()));
            result = domainRegistryService.query(query);
            count = domainRegistryService.count(query);

            assertNotNull(result);
            assertEquals(1, count);
            assertEquals(count, result.getSize());
            assertEquals(domain1, result.getFirstItem());

            //
            // Test name filtered query
            query = domainFactory.newQuery(null);

            query.setPredicate(new AttributePredicateImpl<>(DomainPredicates.SERVICE_NAME, domain2.getServiceName()));
            result = domainRegistryService.query(query);
            count = domainRegistryService.count(query);

            assertNotNull(result);
            assertEquals(1, count);
            assertEquals(count, result.getSize());
            assertEquals(domain2, result.getFirstItem());

            return null;
        });
    }

    @Test
    public void testDelete()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);

            Set<Actions> domainActions = new HashSet<>();
            domainActions.add(Actions.read);
            domainActions.add(Actions.write);

            DomainCreator domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainRegistryServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            DomainRegistryService domainRegistryService = locator.getService(DomainRegistryService.class);
            Domain domain = domainRegistryService.create(domainCreator);
            assertNotNull(domain);

            Domain domainFoundBefore = domainRegistryService.find(null, domain.getId());
            assertNotNull(domainFoundBefore);

            domainRegistryService.delete(null, domain.getId());

            Domain domainFoundAfter = domainRegistryService.find(null, domain.getId());
            assertNull(domainFoundAfter);

            return null;
        });
    }

}
