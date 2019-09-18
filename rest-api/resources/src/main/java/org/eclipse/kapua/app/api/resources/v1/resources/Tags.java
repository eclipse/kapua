/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources;

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagAttributes;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.TagService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("{scopeId}/tags")
public class Tags extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final TagService tagService = locator.getService(TagService.class);
    private final TagFactory tagFactory = locator.getFactory(TagFactory.class);

    /**
     * Gets the {@link Tag} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param name    The {@link Tag} name to filter results
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link TagListResult} of all the tags associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public TagListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @QueryParam("name") String name,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        TagQuery query = tagFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        if (!Strings.isNullOrEmpty(name)) {
            andPredicate.and(query.attributePredicate(TagAttributes.NAME, name));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link TagQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link TagQuery} to use to filter results.
     * @return The {@link TagListResult} of all the result matching the given {@link TagQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TagListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            TagQuery query) throws Exception {
        query.setScopeId(scopeId);

        return tagService.query(query);
    }

    /**
     * Counts the results with the given {@link TagQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link TagQuery} to use to filter results.
     * @return The count of all the result matching the given {@link TagQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            TagQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(tagService.count(query));
    }

    /**
     * Creates a new Tag based on the information provided in TagCreator
     * parameter.
     *
     * @param scopeId    The {@link ScopeId} in which to create the {@link Tag}
     * @param tagCreator Provides the information for the new {@link Tag} to be created.
     * @return The newly created {@link Tag} object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Tag create(
            @PathParam("scopeId") ScopeId scopeId,
            TagCreator tagCreator) throws Exception {
        tagCreator.setScopeId(scopeId);

        return tagService.create(tagCreator);
    }

    /**
     * Returns the Tag specified by the "tagId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link Tag}.
     * @param tagId   The id of the requested Tag.
     * @return The requested Tag object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @GET
    @Path("{tagId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Tag find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("tagId") EntityId tagId) throws Exception {
        Tag tag = tagService.find(scopeId, tagId);

        if (tag == null) {
            throw new KapuaEntityNotFoundException(Tag.TYPE, tagId);
        }

        return tag;
    }

    /**
     * Updates the Tag based on the information provided in the Tag parameter.
     *
     * @param scopeId The ScopeId of the requested {@link Tag}.
     * @param tagId   The id of the requested {@link Tag}
     * @param tag     The modified Tag whose attributed need to be updated.
     * @return The updated tag.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @PUT
    @Path("{tagId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Tag update(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("tagId") EntityId tagId,
            Tag tag) throws Exception {
        tag.setScopeId(scopeId);
        tag.setId(tagId);

        return tagService.update(tag);
    }

    /**
     * Deletes the Tag specified by the "tagId" path parameter.
     *
     * @param scopeId The ScopeId of the requested {@link Tag}.
     * @param tagId   The id of the Tag to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @DELETE
    @Path("{tagId}")
    public Response deleteTag(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("tagId") EntityId tagId) throws Exception {
        tagService.delete(scopeId, tagId);

        return returnOk();
    }
}
