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

import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceLinker;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.jpa.DuplicateNameCheckerImpl;
import org.eclipse.kapua.commons.service.internal.DuplicateNameChecker;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagDomains;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagRepository;
import org.eclipse.kapua.service.tag.TagService;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link TagService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class TagServiceImpl extends KapuaConfigurableServiceLinker implements TagService {


    private final PermissionFactory permissionFactory;
    private final AuthorizationService authorizationService;
    private final TagFactory tagFactory;
    private final TxManager txManager;
    private final TagRepository tagRepository;
    private final DuplicateNameChecker<Tag> duplicateNameChecker;

    /**
     * Injectable Constructor
     *
     * @param permissionFactory           The {@link PermissionFactory} instance
     * @param authorizationService        The {@link AuthorizationService} instance
     * @param serviceConfigurationManager The {@link ServiceConfigurationManager} instance
     * @param txManager
     * @param tagRepository               The {@link TagRepository} instance
     * @param tagFactory
     * @since 2.0.0
     */
    @Inject
    public TagServiceImpl(
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            ServiceConfigurationManager serviceConfigurationManager,
            TxManager txManager,
            TagRepository tagRepository,
            TagFactory tagFactory) {
        super(serviceConfigurationManager);
        this.permissionFactory = permissionFactory;
        this.authorizationService = authorizationService;
        this.tagRepository = tagRepository;
        this.tagFactory = tagFactory;
        this.txManager = txManager;
        this.duplicateNameChecker = new DuplicateNameCheckerImpl<>(tagRepository, (scopeId) -> tagFactory.newQuery(scopeId));
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
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.write, tagCreator.getScopeId()));

        //
        // Check entity limit
        serviceConfigurationManager.checkAllowedEntities(tagCreator.getScopeId(), "Tags");

        //
        // Check duplicate name
        return txManager.executeWithResult(tx -> {
            final long otherEntitiesWithSameName = duplicateNameChecker.countOtherEntitiesWithName(tx, tagCreator.getScopeId(), tagCreator.getName());
            if (otherEntitiesWithSameName > 0) {
                throw new KapuaDuplicateNameException(tagCreator.getName());
            }

            final Tag toBeCreated = tagFactory.newEntity(tagCreator.getScopeId());
            toBeCreated.setName(tagCreator.getName());
            toBeCreated.setDescription(tagCreator.getDescription());
            //
            // Do create
            return tagRepository.create(tx, toBeCreated);
        });
    }

    @Override
    public Tag update(Tag tag) throws KapuaException {
        // Argument validation
        ArgumentValidator.notNull(tag, "tag");
        ArgumentValidator.notNull(tag.getId(), "tag.id");
        ArgumentValidator.notNull(tag.getScopeId(), "tag.scopeId");
        ArgumentValidator.validateEntityName(tag.getName(), "tag.name");

        // Check Access
        authorizationService.checkPermission(
                permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.write, tag.getScopeId()));

        // Check duplicate name
        return txManager.executeWithResult(tx -> {
            // Check existence
            if (tagRepository.find(tx, tag.getScopeId(), tag.getId()) == null) {
                throw new KapuaEntityNotFoundException(Tag.TYPE, tag.getId());
            }
            // Check duplicate name
            final long otherEntitiesWithSameName = duplicateNameChecker.countOtherEntitiesWithName(
                    tx, tag.getScopeId(), tag.getId(), tag.getName());
            if (otherEntitiesWithSameName > 0) {
                throw new KapuaDuplicateNameException(tag.getName());
            }
            // Do Update
            return tagRepository.update(tx, tag);
        });
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId tagId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(tagId, "tagId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, tagId) == null) {
            throw new KapuaEntityNotFoundException(Tag.TYPE, tagId);
        }

        //
        // Do delete
        //
        txManager.executeWithResult(tx -> tagRepository.delete(tx, scopeId, tagId));
    }

    @Override
    public Tag find(KapuaId scopeId, KapuaId tagId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(tagId, "tagId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return txManager.executeWithResult(tx -> tagRepository.find(tx, scopeId, tagId));
    }

    @Override
    public TagListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return txManager.executeWithResult(tx -> tagRepository.query(tx, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return txManager.executeWithResult(tx -> tagRepository.count(tx, query));
    }
}
