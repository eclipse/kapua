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
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobAttributes;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;

import com.google.common.base.Strings;

@Path("{scopeId}/jobs")
public class Jobs extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobService jobService = locator.getService(JobService.class);
    private final JobFactory jobFactory = locator.getFactory(JobFactory.class);

    /**
     * Gets the {@link Job} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param name    The {@link Job} name to filter results
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link JobListResult} of all the jobs associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public JobListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @QueryParam("name") String name,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        JobQuery query = jobFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        if (!Strings.isNullOrEmpty(name)) {
            andPredicate.and(query.attributePredicate(JobAttributes.NAME, name));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link JobQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link JobQuery} to use to filter results.
     * @return The {@link JobListResult} of all the result matching the given {@link JobQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JobListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            JobQuery query) throws Exception {
        query.setScopeId(scopeId);

        return jobService.query(query);
    }

    /**
     * Counts the results with the given {@link JobQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link JobQuery} to use to filter results.
     * @return The count of all the result matching the given {@link JobQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            JobQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(jobService.count(query));
    }

    /**
     * Returns the Job specified by the "jobId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link Job}.
     * @param jobId The id of the requested Job.
     * @return The requested Job object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{jobId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Job find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId) throws Exception {
        Job job = jobService.find(scopeId, jobId);

        if (job == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        return job;
    }

}
