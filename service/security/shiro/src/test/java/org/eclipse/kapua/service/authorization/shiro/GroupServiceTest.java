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

import java.math.BigInteger;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupCreatorImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupPredicates;
import org.eclipse.kapua.service.authorization.group.shiro.GroupQueryImpl;
import org.eclipse.kapua.test.KapuaTest;
import org.eclipse.kapua.test.ResourceLimitsConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GroupServiceTest extends KapuaTest {

    public static final String DROP_FILTER = "athz_*_drop.sql";

    KapuaEid scope = new KapuaEid(IdGenerator.generate());

    // Database fixtures

    @BeforeClass
    public static void beforeClass() throws KapuaException {
        enableH2Connection();
    }

    @AfterClass
    public static void afterClass() throws KapuaException {
        // scriptSession(AuthorizationEntityManagerFactory.getInstance(), DROP_FILTER);
    }

    @Before
    public void before() {
        // Setup JAXB context
        XmlUtil.setContextProvider(new ShiroJAXBContextProvider());
    }

    // Tests

    @Test
    public void testCreate()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            // Create group
            GroupCreator groupCreator = new GroupCreatorImpl(scope, "test-" + random.nextLong());

            //
            // Create
            GroupService groupService = locator.getService(GroupService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(groupService);
            Group group = groupService.create(groupCreator);

            //
            // Assert
            assertNotNull(group);
            assertNotNull(group.getId());
            assertEquals(groupCreator.getScopeId(), group.getScopeId());
            assertEquals(groupCreator.getName(), group.getName());
            assertNotNull(group.getCreatedBy());
            assertNotNull(group.getCreatedOn());
            assertNotNull(group.getModifiedBy());
            assertNotNull(group.getModifiedOn());

            return null;
        });
    }

    @Test
    public void testUpdate()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            GroupCreator groupCreator = new GroupCreatorImpl(scope, "test-" + random.nextLong());

            GroupService groupService = locator.getService(GroupService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(groupService);
            Group group = groupService.create(groupCreator);

            assertNotNull(group);
            assertNotNull(group.getId());
            assertEquals(groupCreator.getScopeId(), group.getScopeId());

            //
            // Update
            group.setName("updated-" + random.nextLong());
            Thread.sleep(50); // Added some delay to make sure the modification time-stamps are really different
            Group groupUpdated1 = groupService.update(group);

            //
            // Assert
            assertNotNull(groupUpdated1);
            assertEquals(group.getScopeId(), groupUpdated1.getScopeId());
            assertEquals(group.getScopeId(), groupUpdated1.getScopeId());
            assertEquals(group.getName(), groupUpdated1.getName());
            assertEquals(group.getCreatedBy(), groupUpdated1.getCreatedBy());
            assertEquals(group.getCreatedOn(), groupUpdated1.getCreatedOn());
            assertEquals(group.getModifiedBy(), groupUpdated1.getModifiedBy());
            assertNotEquals(group.getModifiedOn(), groupUpdated1.getModifiedOn());

            return null;
        });
    }

    @Test
    public void testFind()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            // Create Group
            GroupCreator groupCreator = new GroupCreatorImpl(scope, "test-" + random.nextLong());

            GroupService groupService = locator.getService(GroupService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(groupService);
            Group group = groupService.create(groupCreator);

            assertNotNull(group);
            assertNotNull(group.getId());
            assertEquals(groupCreator.getScopeId(), group.getScopeId());

            //
            // Find
            Group groupFound = groupService.find(scope, group.getId());

            //
            // Assert
            assertNotNull(groupFound);
            assertEquals(group.getScopeId(), groupFound.getScopeId());
            assertEquals(group.getScopeId(), groupFound.getScopeId());
            assertEquals(group.getName(), groupFound.getName());
            assertEquals(group.getCreatedBy(), groupFound.getCreatedBy());
            assertEquals(group.getCreatedOn(), groupFound.getCreatedOn());
            assertEquals(group.getModifiedBy(), groupFound.getModifiedBy());
            assertEquals(group.getModifiedOn(), groupFound.getModifiedOn());
            return null;
        });
    }

    @Test
    public void testQueryAndCount()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            // Create group
            GroupCreator groupCreator = new GroupCreatorImpl(scope, "test-" + random.nextLong());

            GroupService groupService = locator.getService(GroupService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(groupService);
            Group group = groupService.create(groupCreator);

            assertNotNull(group);
            assertNotNull(group.getId());
            assertEquals(groupCreator.getScopeId(), group.getScopeId());

            //
            // Query
            GroupQuery query = new GroupQueryImpl(scope);
            query.setPredicate(new AttributePredicate<String>(GroupPredicates.NAME, group.getName()));
            GroupListResult groupsFound = groupService.query(query);
            long groupsCount = groupService.count(query);

            //
            // Assert
            assertNotNull(groupsFound);
            assertEquals(1, groupsCount);
            assertEquals(1, groupsFound.getSize());

            Group groupFound = groupsFound.getItem(0);
            assertNotNull(groupFound);
            assertEquals(group.getScopeId(), groupFound.getScopeId());
            assertEquals(group.getScopeId(), groupFound.getScopeId());
            assertEquals(group.getName(), groupFound.getName());
            assertEquals(group.getCreatedBy(), groupFound.getCreatedBy());
            assertEquals(group.getCreatedOn(), groupFound.getCreatedOn());
            assertEquals(group.getModifiedBy(), groupFound.getModifiedBy());
            assertEquals(group.getModifiedOn(), groupFound.getModifiedOn());

            return null;
        });
    }

    @Test
    public void testDelete()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            // Create group
            GroupCreator groupCreator = new GroupCreatorImpl(scope, "test-" + random.nextLong());

            GroupService groupService = locator.getService(GroupService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(groupService);
            Group group = groupService.create(groupCreator);

            assertNotNull(group);
            assertNotNull(group.getId());
            assertEquals(groupCreator.getScopeId(), group.getScopeId());

            Group groupFound = groupService.find(scope, group.getId());
            assertNotNull(groupFound);

            //
            // Delete
            groupService.delete(scope, group.getId());

            //
            // Assert
            groupFound = groupService.find(scope, group.getId());
            assertNull(groupFound);

            return null;
        });
    }
}
