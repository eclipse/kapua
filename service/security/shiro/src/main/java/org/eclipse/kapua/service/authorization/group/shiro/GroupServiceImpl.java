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
package org.eclipse.kapua.service.authorization.group.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link GroupService} implementation.
 *
 * @since 1.0
 *
 */
@KapuaProvider
public class GroupServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Group, GroupCreator, GroupService, GroupListResult, GroupQuery, GroupFactory> implements GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

    private static final Domain GROUP_DOMAIN = new GroupDomain();

    public GroupServiceImpl() {
        super(GroupService.class.getName(), GROUP_DOMAIN, AuthorizationEntityManagerFactory.getInstance(), GroupService.class, GroupFactory.class);
    }

    @Override
    public Group create(GroupCreator groupCreator)
            throws KapuaException {
        ArgumentValidator.notNull(groupCreator, "groupCreator");
        ArgumentValidator.notNull(groupCreator.getScopeId(), "roleCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(groupCreator.getName(), "groupCreator.name");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(GROUP_DOMAIN, Actions.write, groupCreator.getScopeId()));

        if (allowedChildEntities(groupCreator.getScopeId()) <= 0) {
            throw new KapuaIllegalArgumentException("scopeId", "max groups reached");
        }

        return entityManagerSession.onTransactedInsert(em -> GroupDAO.create(em, groupCreator));
    }

    @Override
    public Group update(Group group) throws KapuaException {
        ArgumentValidator.notNull(group, "group");
        ArgumentValidator.notNull(group.getScopeId(), "group.scopeId");
        ArgumentValidator.notEmptyOrNull(group.getName(), "group.name");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(GROUP_DOMAIN, Actions.write, group.getScopeId()));
        return entityManagerSession.onTransactedInsert(em -> {

            Group currentGroup = GroupDAO.find(em, group.getId());
            if (currentGroup == null) {
                throw new KapuaEntityNotFoundException(Group.TYPE, group.getId());
            }

            return GroupDAO.update(em, group);
        });
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId groupId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(groupId, "groupId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(GROUP_DOMAIN, Actions.delete, scopeId));

        entityManagerSession.onTransactedAction(em -> {
            if (GroupDAO.find(em, groupId) == null) {
                throw new KapuaEntityNotFoundException(Group.TYPE, groupId);
            }

            GroupDAO.delete(em, groupId);
        });
    }

    @Override
    public Group find(KapuaId scopeId, KapuaId groupId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(groupId, "groupId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(GROUP_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.onResult(em -> GroupDAO.find(em, groupId));
    }

    @Override
    public GroupListResult query(KapuaQuery<Group> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(GROUP_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> GroupDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<Group> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(GROUP_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> GroupDAO.count(em, query));
    }

    //@ListenServiceEvent(fromAddress="account")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }
        LOGGER.info("GroupService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteGroupByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    private void deleteGroupByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        GroupFactory groupFactory = locator.getFactory(GroupFactory.class);

        GroupQuery query = groupFactory.newQuery(accountId);

        GroupListResult groupsToDelete = query(query);

        for (Group g : groupsToDelete.getItems()) {
            delete(g.getScopeId(), g.getId());
        }
    }
}
