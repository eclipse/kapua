/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.resources.v1.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.model.EntityId;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.app.api.core.model.job.IsJobRunningMultipleResponse;
import org.eclipse.kapua.app.api.core.model.job.IsJobRunningResponse;
import org.eclipse.kapua.app.api.core.model.job.MultipleJobIdRequest;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;

@Path("{scopeId}/jobs")
public class JobEngine extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobEngineService jobEngineService = locator.getService(JobEngineService.class);

    @POST
    @Path("{jobId}/_start")
    @Consumes(MediaType.APPLICATION_JSON)
    public void startJob(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            JobStartOptions jobStartOptions) throws KapuaException {
        jobEngineService.startJob(scopeId, jobId, jobStartOptions);
    }

    @POST
    @Path("{jobId}/_stop")
    public void stopJob(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId) throws KapuaException {
        jobEngineService.stopJob(scopeId, jobId);
    }

    @GET
    @Path("{jobId}/_isRunning")
    public IsJobRunningResponse isRunning(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId) throws KapuaException {
        return new IsJobRunningResponse(jobId, jobEngineService.isRunning(scopeId, jobId));
    }

    @POST
    @Path("_isRunning")
    public IsJobRunningMultipleResponse isRunning(
            @PathParam("scopeId") ScopeId scopeId,
            MultipleJobIdRequest jobIds) throws KapuaException, JAXBException {
        return new IsJobRunningMultipleResponse(jobEngineService.isRunning(scopeId, jobIds.getJobIds()));
    }

    @POST
    @Path("{jobId}/executions/{executionId}/_resume")
    public void resumeJobExecution(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @PathParam("executionId") EntityId executionId) throws KapuaException {
        jobEngineService.resumeJobExecution(scopeId, jobId, executionId);
    }

    @POST
    @Path("{jobId}/executions/{executionId}/_stop")
    public void stopJobExecution(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("jobId") EntityId jobId,
            @PathParam("executionId") EntityId executionId) throws KapuaException {
        jobEngineService.stopJobExecution(scopeId, jobId, executionId);
    }

}
