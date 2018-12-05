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
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;

/**
 * {@link Tag} DAO
 * 
 * @since 1.0.0
 */
public class TagDAO extends ServiceDAO {

    /**
     * Creates and returns new {@link Tag}
     * 
     * @param em
     *            The {@link EntityManager} that holds the transaction.
     * @param creator
     *            The {@link TagCreator} object from which create the new {@link Tag}.
     * @return The newly created {@link Tag}.
     * @throws KapuaException
     *             On create error.
     * @since 1.0.0
     */
    public static Tag create(EntityManager em, TagCreator creator)
            throws KapuaException {
        Tag tag = new TagImpl(creator.getScopeId());
        tag.setName(creator.getName());

        return ServiceDAO.create(em, tag);
    }

    /**
     * Updates and returns the updated {@link Tag}
     * 
     * @param em
     *            The {@link EntityManager} that holds the transaction.
     * @param tag
     *            The {@link Tag} to update
     * @return The updated {@link Tag}.
     * @throws KapuaEntityNotFoundException
     *             If {@link Tag} is not found.
     */
    public static Tag update(EntityManager em, Tag tag) throws KapuaEntityNotFoundException {
        TagImpl tagImpl = (TagImpl) tag;
        return ServiceDAO.update(em, TagImpl.class, tagImpl);
    }

    /**
     * Finds the {@link Tag} by {@link Tag} identifier
     * 
     * @param em
     *            The {@link EntityManager} that holds the transaction.
     * @param scopeId
     * @param tagId
     *            The {@link Tag} id to search.
     * @return The found {@link Tag} or {@code null} if not found.
     * @since 1.0.0
     */
    public static Tag find(EntityManager em, KapuaId scopeId, KapuaId tagId) {
        return ServiceDAO.find(em,TagImpl.class, scopeId, tagId);
    }

    /**
     * Returns the {@link Tag} list matching the provided query.
     *
     * @param em
     *            The {@link EntityManager} that holds the transaction.
     * @param tagQuery
     *            The {@link TagQuery} used to filter results.
     * @return The list of {@link Tag}s that matches the given query.
     * @throws KapuaException
     *             On query error.
     * @since 1.0.0
     */
    public static TagListResult query(EntityManager em, KapuaQuery<Tag> tagQuery)
            throws KapuaException {
        return ServiceDAO.query(em, Tag.class, TagImpl.class, new TagListResultImpl(), tagQuery);
    }

    /**
     * Return the {@link Tag} count matching the provided query
     *
     * @param em
     *            The {@link EntityManager} that holds the transaction.
     * @param tagQuery
     *            The {@link TagQuery} used to filter results
     * @return The count of {@link Tag}s that matches the given query.
     * @throws KapuaException
     * @since 1.0.0
     */
    public static long count(EntityManager em, KapuaQuery<Tag> tagQuery)
            throws KapuaException {
        return ServiceDAO.count(em, Tag.class, TagImpl.class, tagQuery);
    }

    /**
     * Deletes the {@link Tag} by {@link Tag} identifier
     *
     * @param em
     *            The {@link EntityManager} that holds the transaction.
     * @param scopeId
     * @param tagId
     *            The {@link Tag} id to delete.
     * @throws KapuaEntityNotFoundException
     *             If {@link Tag} is not found.
     * @since 1.0.0
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId tagId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, TagImpl.class, scopeId, tagId);
    }
}
