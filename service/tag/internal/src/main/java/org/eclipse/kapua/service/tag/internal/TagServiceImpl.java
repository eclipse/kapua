/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.TagService;

/**
 * {@link TagService} implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class TagServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Tag, TagCreator, TagService, TagListResult, TagQuery, TagFactory> implements TagService {

    private static final Domain TAG_DOMAIN = new TagDomain();

    public TagServiceImpl() {
        super(TagService.class.getName(), TAG_DOMAIN, TagEntityManagerFactory.getInstance(), TagService.class, TagFactory.class);
    }

    @Override
    public Tag create(TagCreator tagCreator)
            throws KapuaException {
        ArgumentValidator.notNull(tagCreator, "tagCreator");
        ArgumentValidator.notNull(tagCreator.getScopeId(), "roleCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(tagCreator.getName(), "tagCreator.name");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(TAG_DOMAIN, Actions.write, tagCreator.getScopeId()));

        if (allowedChildEntities(tagCreator.getScopeId()) <= 0) {
            throw new KapuaIllegalArgumentException("scopeId", "max tags reached");
        }

        return entityManagerSession.onTransactedInsert(em -> TagDAO.create(em, tagCreator));
    }

    @Override
    public Tag update(Tag tag) throws KapuaException {
        ArgumentValidator.notNull(tag, "tag");
        ArgumentValidator.notNull(tag.getScopeId(), "tag.scopeId");
        ArgumentValidator.notEmptyOrNull(tag.getName(), "tag.name");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(TAG_DOMAIN, Actions.write, tag.getScopeId()));
        return entityManagerSession.onTransactedInsert(em -> {

            Tag currentTag = TagDAO.find(em, tag.getId());
            if (currentTag == null) {
                throw new KapuaEntityNotFoundException(Tag.TYPE, tag.getId());
            }

            return TagDAO.update(em, tag);
        });
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId tagId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(tagId, "tagId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(TAG_DOMAIN, Actions.delete, scopeId));

        entityManagerSession.onTransactedAction(em -> {
            if (TagDAO.find(em, tagId) == null) {
                throw new KapuaEntityNotFoundException(Tag.TYPE, tagId);
            }

            TagDAO.delete(em, tagId);
        });
    }

    @Override
    public Tag find(KapuaId scopeId, KapuaId tagId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(tagId, "tagId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(TAG_DOMAIN, Actions.read, scopeId));

        return entityManagerSession.onResult(em -> TagDAO.find(em, tagId));
    }

    @Override
    public TagListResult query(KapuaQuery<Tag> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(TAG_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> TagDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<Tag> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(TAG_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> TagDAO.count(em, query));
    }
}
