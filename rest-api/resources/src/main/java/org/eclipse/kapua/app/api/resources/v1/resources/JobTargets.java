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
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.execution.JobExecutionAttributes;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetAttributes;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;

@Path("{scopeId}/jobs/{jobId}/targets")
public class JobTargets extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobTargetService jobTargetService = locator.getService(JobTargetService.class);
    private final JobExecutionService jobExecutionService = locator.getService(JobExecutionService.class);
    private final JobTargetFactory jobTargetFactory = locator.getFactory(JobTargetFactory.class);
    private final JobExecutionFactory jobExecutionFactory = locator.getFactory(JobExecutionFactory.class);

    /**
     * Gets the {@link JobTarget} list for a given {@link Job}.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param jobId   The {@link Job} id to filter results
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link JobTargetListResult} of all the jobs targets associated to the current selected job.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public JobTargetListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        JobTargetQuery query = jobTargetFactory.newQuery(scopeId);

        query.setPredicate(query.attributePredicate(JobTargetAttributes.JOB_ID, jobId));

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, jobId, query);
    }

    /**
     * Queries the results with the given {@link JobTargetQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link JobTargetQuery} to use to filter results.
     * @return The {@link JobTargetListResult} of all the result matching the given {@link JobTargetQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JobTargetListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            JobTargetQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(JobTargetAttributes.JOB_ID, jobId));
        return jobTargetService.query(query);
    }

    /**
     * Counts the results with the given {@link JobTargetQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link JobTargetQuery} to use to filter results.
     * @return The count of all the result matching the given {@link JobTargetQuery} parameter.
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
            JobTargetQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(JobTargetAttributes.JOB_ID, jobId));

        return new CountResult(jobTargetService.count(query));
    }

    /**
     * Returns the Job specified by the "jobId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link Job}.
     * @param jobId The id of the requested Job.
     * @param targetId The id of the requested JobTarget.
     * @return The requested Job object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{targetId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public JobTarget find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @PathParam("targetId") EntityId targetId) throws Exception {
        JobTargetQuery jobTargetQuery = jobTargetFactory.newQuery(scopeId);
        jobTargetQuery.setPredicate(jobTargetQuery.andPredicate(
                jobTargetQuery.attributePredicate(JobTargetAttributes.JOB_ID, jobId),
                jobTargetQuery.attributePredicate(JobTargetAttributes.ENTITY_ID, targetId)
        ));
        jobTargetQuery.setOffset(0);
        jobTargetQuery.setLimit(1);
        JobTargetListResult jobTargetListResult = jobTargetService.query(jobTargetQuery);

        if (jobTargetListResult.isEmpty()) {
            throw new KapuaEntityNotFoundException(JobTarget.TYPE, targetId);
        }

        return jobTargetListResult.getFirstItem();
    }

    @GET
    @Path("{targetId}/executions")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public JobExecutionListResult executionsByTarget(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @PathParam("targetId") EntityId targetId,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        JobExecutionQuery jobExecutionQuery = jobExecutionFactory.newQuery(scopeId);
        jobExecutionQuery.setPredicate(jobExecutionQuery.attributePredicate(JobExecutionAttributes.TARGET_IDS, new KapuaId[]{ targetId }));
        JobExecutionListResult jobExecutionListResult = jobExecutionService.query(jobExecutionQuery);

        jobExecutionQuery.setOffset(offset);
        jobExecutionQuery.setLimit(limit);

        return jobExecutionListResult;
    }

}
