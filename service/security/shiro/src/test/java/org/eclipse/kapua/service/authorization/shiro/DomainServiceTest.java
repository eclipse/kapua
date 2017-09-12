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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainPredicates;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.test.KapuaTest;
import org.junit.Test;

public class DomainServiceTest extends KapuaTest {

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

            DomainCreator domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            DomainService domainService = locator.getService(DomainService.class);
            Domain domain = domainService.create(domainCreator);

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

            DomainCreator domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            DomainService domainService = locator.getService(DomainService.class);
            Domain domain = domainService.create(domainCreator);

            assertNotNull(domain);
            assertNotNull(domain.getId());

            Domain domainFound = domainService.find(null, domain.getId());

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
            DomainService domainService = locator.getService(DomainService.class);

            long initialCount = domainService.count(domainFactory.newQuery(null));

            // Domain 1
            Set<Actions> domainActions = new HashSet<>();
            domainActions.add(Actions.read);
            domainActions.add(Actions.write);

            DomainCreator domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            Domain domain1 = domainService.create(domainCreator);

            // Domain 2
            domainActions = new HashSet<>();
            domainActions.add(Actions.read);
            domainActions.add(Actions.write);

            domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            Domain domain2 = domainService.create(domainCreator);

            //
            // Test query
            DomainQuery query = domainFactory.newQuery(null);
            DomainListResult result = domainService.query(query);
            long count = domainService.count(query);

            assertNotNull(result);
            assertEquals(initialCount + 2, count);
            assertEquals(count, result.getSize());

            //
            // Test name filtered query
            query = domainFactory.newQuery(null);

            query.setPredicate(new AttributePredicate<String>(DomainPredicates.NAME, domain1.getName()));
            result = domainService.query(query);
            count = domainService.count(query);

            assertNotNull(result);
            assertEquals(1, count);
            assertEquals(count, result.getSize());
            assertEquals(domain1, result.getItem(0));

            //
            // Test name filtered query
            query = domainFactory.newQuery(null);

            query.setPredicate(new AttributePredicate<String>(DomainPredicates.SERVICE_NAME, domain2.getServiceName()));
            result = domainService.query(query);
            count = domainService.count(query);

            assertNotNull(result);
            assertEquals(1, count);
            assertEquals(count, result.getSize());
            assertEquals(domain2, result.getItem(0));

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

            DomainCreator domainCreator = domainFactory.newCreator("test-" + random.nextLong(), DomainServiceTest.class.getName() + random.nextLong());
            domainCreator.setActions(domainActions);

            DomainService domainService = locator.getService(DomainService.class);
            Domain domain = domainService.create(domainCreator);
            assertNotNull(domain);

            Domain domainFoundBefore = domainService.find(null, domain.getId());
            assertNotNull(domainFoundBefore);

            domainService.delete(null, domain.getId());

            Domain domainFoundAfter = domainService.find(null, domain.getId());
            assertNull(domainFoundAfter);

            return null;
        });
    }

}
