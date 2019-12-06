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
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionAttributes;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.eclipse.kapua.service.job.targets.JobTargetAttributes;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;

@Path("{scopeId}/jobs/{jobId}/executions")
public class JobExecutions extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobExecutionService jobExecutionService = locator.getService(JobExecutionService.class);
    private final JobTargetService jobTargetService = locator.getService(JobTargetService.class);
    private final JobExecutionFactory jobExecutionFactory = locator.getFactory(JobExecutionFactory.class);
    private final JobTargetFactory jobTargetFactory = locator.getFactory(JobTargetFactory.class);

    /**
     * Gets the {@link JobExecution} list for a given {@link Job}.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param jobId   The {@link Job} id to filter results
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link JobExecutionListResult} of all the jobs executions associated to the current selected job.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public JobExecutionListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        JobExecutionQuery query = jobExecutionFactory.newQuery(scopeId);

        query.setPredicate(query.attributePredicate(JobExecutionAttributes.JOB_ID, jobId));

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, jobId, query);
    }

    /**
     * Queries the results with the given {@link JobExecutionQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link JobExecutionQuery} to use to filter results.
     * @return The {@link JobExecutionListResult} of all the result matching the given {@link JobExecutionQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JobExecutionListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            JobExecutionQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(JobExecutionAttributes.JOB_ID, jobId));
        return jobExecutionService.query(query);
    }

    /**
     * Counts the results with the given {@link JobExecutionQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link JobExecutionQuery} to use to filter results.
     * @return The count of all the result matching the given {@link JobExecutionQuery} parameter.
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
            JobExecutionQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(JobExecutionAttributes.JOB_ID, jobId));

        return new CountResult(jobExecutionService.count(query));
    }

    /**
     * Returns the Job specified by the "jobId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link Job}.
     * @param jobId The id of the requested Job.
     * @param executionId The id of the requested JobExecution.
     * @return The requested Job object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{executionId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public JobExecution find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @PathParam("executionId") EntityId executionId) throws Exception {
        JobExecutionQuery jobExecutionQuery = jobExecutionFactory.newQuery(scopeId);
        jobExecutionQuery.setPredicate(jobExecutionQuery.andPredicate(
                jobExecutionQuery.attributePredicate(JobExecutionAttributes.JOB_ID, jobId),
                jobExecutionQuery.attributePredicate(JobExecutionAttributes.ENTITY_ID, executionId)
        ));
        jobExecutionQuery.setOffset(0);
        jobExecutionQuery.setLimit(1);
        JobExecutionListResult jobExecutionListResult = jobExecutionService.query(jobExecutionQuery);

        if (jobExecutionListResult.isEmpty()) {
            throw new KapuaEntityNotFoundException(JobExecution.TYPE, executionId);
        }

        return jobExecutionListResult.getFirstItem();
    }

    @GET
    @Path("{executionId}/targets")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public JobTargetListResult executionsByTarget(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @PathParam("executionId") EntityId executionId,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        JobExecution jobExecution = jobExecutionService.find(scopeId, executionId);
        JobTargetQuery jobTargetQuery = jobTargetFactory.newQuery(scopeId);
        jobTargetQuery.setPredicate(jobTargetQuery.attributePredicate(JobTargetAttributes.ENTITY_ID, jobExecution.getTargetIds().toArray(new KapuaId[0])));
        jobTargetQuery.setLimit(limit);
        jobTargetQuery.setOffset(offset);

        return jobTargetService.query(jobTargetQuery);
    }

}
