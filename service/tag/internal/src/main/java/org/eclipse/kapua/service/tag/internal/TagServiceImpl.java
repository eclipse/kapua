/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.service.internal.KapuaNamedEntityServiceUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagDomains;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

/**
 * {@link TagService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class TagServiceImpl extends AbstractKapuaService implements TagService {

    private PermissionFactory permissionFactory;
    private AuthorizationService authorizationService;
    private final ServiceConfigurationManager serviceConfigurationManager;

    /**
     * Deprecated constructor
     *
     * @deprecated since 2.0.0 - Please use {@link #TagServiceImpl(TagEntityManagerFactory, PermissionFactory, AuthorizationService, ServiceConfigurationManager)} instead. This constructor might be removed in future releases
     */
    @Deprecated
    public TagServiceImpl() {
        super(TagEntityManagerFactory.getInstance(), null);
        serviceConfigurationManager = null;
    }

    /**
     * Injectable Constructor
     *
     * @param entityManagerFactory The {@link TagEntityManagerFactory} instance
     * @param permissionFactory    The {@link PermissionFactory} instance
     * @param authorizationService The {@link AuthorizationService} instance
     * @since 2.0.0
     */
    @Inject
    public TagServiceImpl(
            TagEntityManagerFactory entityManagerFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            @Named("TagServiceConfigurationManager") ServiceConfigurationManager serviceConfigurationManager) {
        super(entityManagerFactory, null);
        this.permissionFactory = permissionFactory;
        this.authorizationService = authorizationService;
        this.serviceConfigurationManager = serviceConfigurationManager;
    }

    @Override
    public Tag create(TagCreator tagCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(tagCreator, "tagCreator");
        ArgumentValidator.notNull(tagCreator.getScopeId(), "tagCreator.scopeId");
        ArgumentValidator.validateEntityName(tagCreator.getName(), "tagCreator.name");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(TagDomains.TAG_DOMAIN, Actions.write, tagCreator.getScopeId()));

        //
        // Check entity limit
        serviceConfigurationManager.checkAllowedEntities(tagCreator.getScopeId(), "Tags");

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(this, tagCreator);

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> TagDAO.create(em, tagCreator));
    }

    @Override
    public Tag update(Tag tag) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(tag, "tag");
        ArgumentValidator.notNull(tag.getId(), "tag.id");
        ArgumentValidator.notNull(tag.getScopeId(), "tag.scopeId");
        ArgumentValidator.validateEntityName(tag.getName(), "tag.name");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(TagDomains.TAG_DOMAIN, Actions.write, tag.getScopeId()));

        //
        // Check existence
        if (find(tag.getScopeId(), tag.getId()) == null) {
            throw new KapuaEntityNotFoundException(Tag.TYPE, tag.getId());
        }

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(this, tag);

        //
        // Do Update
        return entityManagerSession.doTransactedAction(em -> TagDAO.update(em, tag));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId tagId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(tagId, "tagId");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(TagDomains.TAG_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, tagId) == null) {
            throw new KapuaEntityNotFoundException(Tag.TYPE, tagId);
        }

        //
        //
        entityManagerSession.doTransactedAction(em -> TagDAO.delete(em, scopeId, tagId));
    }

    @Override
    public Tag find(KapuaId scopeId, KapuaId tagId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(tagId, "tagId");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(TagDomains.TAG_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> TagDAO.find(em, scopeId, tagId));
    }

    @Override
    public TagListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(TagDomains.TAG_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> TagDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        getAuthorizationService().checkPermission(getPermissionFactory().newPermission(TagDomains.TAG_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(em -> TagDAO.count(em, query));
    }


    /**
     * AuthorizationService should be provided by the Locator, but in most cases when this class is instantiated through the deprecated constructor the Locator is not yet ready,
     * therefore fetching of the required instance is demanded to this artificial getter.
     *
     * @return The instantiated (hopefully) {@link AuthorizationService} instance
     */
    //TODO: Remove as soon as deprecated constructors are removed, use field directly instead.
    protected AuthorizationService getAuthorizationService() {
        if (authorizationService == null) {
            authorizationService = KapuaLocator.getInstance().getService(AuthorizationService.class);
        }
        return authorizationService;
    }

    /**
     * PermissionFactory should be provided by the Locator, but in most cases when this class is instantiated through this constructor the Locator is not yet ready,
     * therefore fetching of the required instance is demanded to this artificial getter.
     *
     * @return The instantiated (hopefully) {@link PermissionFactory} instance
     */
    //TODO: Remove as soon as deprecated constructors are removed, use field directly instead.
    protected PermissionFactory getPermissionFactory() {
        if (permissionFactory == null) {
            permissionFactory = KapuaLocator.getInstance().getFactory(PermissionFactory.class);
        }
        return permissionFactory;
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException {
        return serviceConfigurationManager.getConfigMetadata(scopeId, true);
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        return serviceConfigurationManager.getConfigValues(scopeId, true);
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {
        serviceConfigurationManager.setConfigValues(scopeId, Optional.ofNullable(parentId), values);
    }
}
