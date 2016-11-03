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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionPredicates;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;

/**
 * DeviceConnectionService exposes APIs to retrieve Device connections under a scope.
 * It includes APIs to find, list, and update devices connections associated with a scope.
 * 
 * @since 1.0
 * 
 */
public class DeviceConnectionServiceImpl implements DeviceConnectionService
{

    @Override
    public DeviceConnection create(DeviceConnectionCreator deviceConnectionCreator)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnectionCreator, "deviceConnectionCreator");
        ArgumentValidator.notNull(deviceConnectionCreator.getScopeId(), "deviceConnectionCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(deviceConnectionCreator.getClientId(), "deviceConnectionCreator.clientId");
        ArgumentValidator.notNull(deviceConnectionCreator.getUserId(), "deviceConnectionCreator.userId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceConnectionDomain.DEVICE_CONNECTION, Actions.write, deviceConnectionCreator.getScopeId()));

        //
        // Create the connection
        DeviceConnection deviceConnection = null;
        EntityManager em = DeviceEntityManagerFactory.getEntityManager();
        try {
            em.beginTransaction();

            deviceConnection = DeviceConnectionDAO.create(em, deviceConnectionCreator);
            em.commit();

            deviceConnection = DeviceConnectionDAO.find(em, deviceConnection.getId());
        }
        catch (Exception e) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }

        return deviceConnection;
    }

    @Override
    public DeviceConnection update(DeviceConnection deviceConnection)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnection, "deviceConnection");
        ArgumentValidator.notNull(deviceConnection.getId(), "deviceConnection.id");
        ArgumentValidator.notNull(deviceConnection.getScopeId(), "deviceConnection.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceConnectionDomain.DEVICE_CONNECTION, Actions.write, deviceConnection.getScopeId()));

        //
        // Do update
        DeviceConnection deviceConnectionUpdated = null;
        EntityManager em = DeviceEntityManagerFactory.getEntityManager();
        try {
            if (DeviceConnectionDAO.find(em, deviceConnection.getId()) == null) {
                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnection.getId());
            }

            em.beginTransaction();
            deviceConnectionUpdated = DeviceConnectionDAO.update(em, deviceConnection);
            em.commit();
        }
        catch (Exception e) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }

        return deviceConnectionUpdated;
    }

    @Override
    public DeviceConnection find(KapuaId scopeId, KapuaId entityId)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "entityId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceConnectionDomain.DEVICE_CONNECTION, Actions.read, scopeId));

        //
        // Do find
        DeviceConnection deviceConnection = null;
        EntityManager em = DeviceEntityManagerFactory.getEntityManager();
        try {
            deviceConnection = DeviceConnectionDAO.find(em, entityId);
        }
        catch (Exception e) {
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }

        return deviceConnection;
    }

    @Override
    public DeviceConnection findByClientId(KapuaId scopeId, String clientId)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notEmptyOrNull(clientId, "clientId");

        //
        // Build query
        DeviceConnectionQueryImpl query = new DeviceConnectionQueryImpl(scopeId);
        KapuaPredicate predicate = new AttributePredicate<String>(DeviceConnectionPredicates.CLIENT_ID, clientId);
        query.setPredicate(predicate);

        //
        // Query and parse result
        DeviceConnection device = null;
        DeviceConnectionListResult result = query(query);
        if (result.getSize() == 1) {
            device = result.getItem(0);
        }

        return device;
    }

    @Override
    public DeviceConnectionListResult query(KapuaQuery<DeviceConnection> query)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceConnectionDomain.DEVICE_CONNECTION, Actions.read, query.getScopeId()));

        //
        // Do Query
        DeviceConnectionListResult result = null;
        EntityManager em = DeviceEntityManagerFactory.getEntityManager();
        try {
            result = DeviceConnectionDAO.query(em, query);
        }
        catch (Exception e) {
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }

        return result;
    }

    @Override
    public long count(KapuaQuery<DeviceConnection> query)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceConnectionDomain.DEVICE_CONNECTION, Actions.read, query.getScopeId()));

        //
        // Do count
        long count = 0;
        EntityManager em = DeviceEntityManagerFactory.getEntityManager();
        try {
            count = DeviceConnectionDAO.count(em, query);
        }
        catch (Exception e) {
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }

        return count;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId deviceConnectionId)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnectionId, "deviceConnection.id");
        ArgumentValidator.notNull(scopeId, "deviceConnection.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceConnectionDomain.DEVICE_CONNECTION, Actions.write, scopeId));

        //
        // Do delete
        EntityManager em = DeviceEntityManagerFactory.getEntityManager();
        try {
            if (DeviceConnectionDAO.find(em, deviceConnectionId) == null) {
                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnectionId);
            }

            em.beginTransaction();
            DeviceConnectionDAO.delete(em, deviceConnectionId);
            em.commit();
        }
        catch (Exception e) {
            em.rollback();
            throw KapuaExceptionUtils.convertPersistenceException(e);
        }
        finally {
            em.close();
        }
    }

    @Override
    public void connect(DeviceConnectionCreator creator)
        throws KapuaException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void disconnect(KapuaId scopeId, String clientId)
        throws KapuaException
    {
        // TODO Auto-generated method stub

    }

}
