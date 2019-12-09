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

import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepAttributes;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;

import com.google.common.base.Strings;

@Path("{scopeId}/jobs/{jobId}/steps")
public class JobSteps extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobStepService jobStepService = locator.getService(JobStepService.class);
    private final JobStepFactory jobStepFactory = locator.getFactory(JobStepFactory.class);

    /**
     * Gets the {@link JobStep} list for a given {@link Job}.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param jobId   The {@link Job} id to filter results
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link JobStepListResult} of all the jobs jobSteps associated to the current selected job.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public JobStepListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @QueryParam("name") String name,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {

        JobStepQuery query = jobStepFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate(query.attributePredicate(JobStepAttributes.JOB_ID, jobId));

        if (!Strings.isNullOrEmpty(name)) {
            andPredicate = andPredicate.and(query.attributePredicate(JobStepAttributes.NAME, name));
        }

        query.setPredicate(andPredicate);
        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, jobId, query);
    }

    /**
     * Queries the results with the given {@link JobStepQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link JobStepQuery} to use to filter results.
     * @return The {@link JobStepListResult} of all the result matching the given {@link JobStepQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JobStepListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            JobStepQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(JobStepAttributes.JOB_ID, jobId));
        return jobStepService.query(query);
    }

    /**
     * Counts the results with the given {@link JobStepQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link JobStepQuery} to use to filter results.
     * @return The count of all the result matching the given {@link JobStepQuery} parameter.
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
            JobStepQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(JobStepAttributes.JOB_ID, jobId));

        return new CountResult(jobStepService.count(query));
    }

    /**
     * Returns the Job specified by the "jobId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link Job}.
     * @param jobId The id of the requested Job.
     * @param stepId The id of the requested JobStep.
     * @return The requested Job object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{stepId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public JobStep find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @PathParam("stepId") EntityId stepId) throws Exception {
        return jobStepService.find(scopeId, stepId);
    }

}
