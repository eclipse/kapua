/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerAttributes;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;

import com.google.common.base.Strings;

@Path("{scopeId}/jobs/{jobId}/triggers")
public class JobTriggers extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final TriggerService triggerService = locator.getService(TriggerService.class);
    private final TriggerFactory triggerFactory = locator.getFactory(TriggerFactory.class);

    /**
     * Gets the {@link Trigger} list for a given {@link Job}.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param jobId   The {@link Job} id to filter results
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link TriggerListResult} of all the jobs triggers associated to the current selected job.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public TriggerListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @QueryParam("name") String name,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {

        TriggerQuery query = triggerFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate(returnJobIdPredicate(jobId, query));

        if (!Strings.isNullOrEmpty(name)) {
            andPredicate = andPredicate.and(query.attributePredicate(TriggerAttributes.NAME, name));
        }

        query.setPredicate(andPredicate);
        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, jobId, query);
    }

    /**
     * Queries the results with the given {@link TriggerQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link TriggerQuery} to use to filter results.
     * @return The {@link TriggerListResult} of all the result matching the given {@link TriggerQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public TriggerListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            TriggerQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(returnJobIdPredicate(jobId, query));
        return triggerService.query(query);
    }

    /**
     * Counts the results with the given {@link TriggerQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link TriggerQuery} to use to filter results.
     * @return The count of all the result matching the given {@link TriggerQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            TriggerQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(returnJobIdPredicate(jobId, query));

        return new CountResult(triggerService.count(query));
    }

    /**
     * Returns the Job specified by the "jobId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link Job}.
     * @param jobId The id of the requested Job.
     * @param triggerId The id of the requested Trigger.
     * @return The requested Job object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{triggerId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Trigger find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @PathParam("triggerId") EntityId triggerId) throws Exception {
        TriggerQuery triggerQuery = triggerFactory.newQuery(scopeId);
        triggerQuery.setPredicate(triggerQuery.andPredicate(
                returnJobIdPredicate(jobId, triggerQuery),
                triggerQuery.attributePredicate(TriggerAttributes.ENTITY_ID, triggerId)
        ));
        triggerQuery.setOffset(0);
        triggerQuery.setLimit(1);
        TriggerListResult triggerListResult = triggerService.query(triggerQuery);

        if (triggerListResult.isEmpty()) {
            throw new KapuaEntityNotFoundException(Trigger.TYPE, triggerId);
        }

        return triggerListResult.getFirstItem();
    }

    private AndPredicate returnJobIdPredicate(KapuaId jobId, TriggerQuery query) {
        AttributePredicate<String> kapuaPropertyNameAttributePredicate = query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_NAME, "jobId");
        AttributePredicate<String> kapuaPropertyValueAttributePredicate = query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_VALUE, jobId.toCompactId());
        AttributePredicate<String> kapuaPropertyTypeAttributePredicate = query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_TYPE, KapuaId.class.getName());

        return query.andPredicate(
                kapuaPropertyNameAttributePredicate,
                kapuaPropertyValueAttributePredicate,
                kapuaPropertyTypeAttributePredicate
        );
    }

}
