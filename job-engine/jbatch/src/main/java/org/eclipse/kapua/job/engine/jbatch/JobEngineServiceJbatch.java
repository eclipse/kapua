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
package org.eclipse.kapua.job.engine.jbatch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.jbatch.listener.KapuaJobListener;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.context.JobContextPropertyNames;
import org.eclipse.kapua.service.job.context.StepContextPropertyNames;
import org.eclipse.kapua.service.job.internal.JobDomain;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepPredicates;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import com.ibm.jbatch.container.jsl.ExecutionElement;
import com.ibm.jbatch.container.jsl.ModelSerializer;
import com.ibm.jbatch.container.jsl.ModelSerializerFactory;
import com.ibm.jbatch.jsl.model.Batchlet;
import com.ibm.jbatch.jsl.model.Chunk;
import com.ibm.jbatch.jsl.model.ItemProcessor;
import com.ibm.jbatch.jsl.model.ItemReader;
import com.ibm.jbatch.jsl.model.ItemWriter;
import com.ibm.jbatch.jsl.model.JSLJob;
import com.ibm.jbatch.jsl.model.JSLProperties;
import com.ibm.jbatch.jsl.model.Listener;
import com.ibm.jbatch.jsl.model.Listeners;
import com.ibm.jbatch.jsl.model.Property;
import com.ibm.jbatch.jsl.model.Step;

@KapuaProvider
public class JobEngineServiceJbatch implements JobEngineService {

    private static final Domain JOB_DOMAIN = new JobDomain();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final JobService JOB_SERVICE = LOCATOR.getService(JobService.class);

    private static final JobStepService JOB_STEP_SERVICE = LOCATOR.getService(JobStepService.class);
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);

    private static final JobStepDefinitionService STEP_DEFINITION_SERVICE = LOCATOR.getService(JobStepDefinitionService.class);

    private static final ModelSerializer<JSLJob> JSL_MODEL_SERIALIZER_FACTORY;
    private static final JobOperator JOB_ENGINE;
    private static long jobExecutionId;

    static {

        //
        // jBatch model serializer configuration
        JSL_MODEL_SERIALIZER_FACTORY = ModelSerializerFactory.createJobModelSerializer();

        JOB_ENGINE = BatchRuntime.getJobOperator();
    }

    @Override
    public void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        Job job = JOB_SERVICE.find(scopeId, jobId);

        // Retrieve job XML definition. Create it if not exists
        String jobXmlDefinition = job.getJobXmlDefinition();
        if (jobXmlDefinition == null) {
            JobStepQuery jobStepQuery = JOB_STEP_FACTORY.newQuery(job.getScopeId());
            jobStepQuery.setPredicate(new AttributePredicate<>(JobStepPredicates.JOB_ID, job.getId()));

            JobStepListResult jobSteps = JOB_STEP_SERVICE.query(jobStepQuery);
            jobSteps.sort((step1, step2) -> step1.getStepIndex() - step2.getStepIndex());

            List<ExecutionElement> jslExecutionElements = new ArrayList<>();
            Iterator<JobStep> jobStepIterator = jobSteps.getItems().iterator();
            while (jobStepIterator.hasNext()) {
                // for (JobStep jobStep : jobSteps.getItems()) {
                JobStep jobStep = jobStepIterator.next();

                Step jslStep = new Step();
                JobStepDefinition jobStepDefinition = STEP_DEFINITION_SERVICE.find(jobStep.getScopeId(), jobStep.getJobStepDefinitionId());
                switch (jobStepDefinition.getStepType()) {
                case GENERIC:
                    jslStep.setBatchlet(buildGenericStep(jobStepDefinition));
                    break;
                case TARGET:
                    jslStep.setChunk(buildChunkStep(jobStepDefinition));
                    break;
                default:
                    // FIXME: Throw appropriate exception
                    break;
                }

                jslStep.setId("step-" + String.valueOf(jobStep.getStepIndex()));

                if (jobStepIterator.hasNext()) {
                    jslStep.setNextFromAttribute("step-" + String.valueOf(jobStep.getStepIndex() + 1));
                }

                jslStep.setProperties(buildStepProperties(jobStepDefinition, jobStep, jobStepIterator.hasNext()));

                jslExecutionElements.add(jslStep);
            }

            JSLJob jslJob = new JSLJob();
            jslJob.setRestartable("true");
            jslJob.setId("job-" + job.getScopeId().toCompactId() + "-" + job.getId().toCompactId());
            jslJob.setVersion("1.0");
            jslJob.setProperties(buildJobProperties(job));
            jslJob.setListeners(buildListener());
            jslJob.getExecutionElements().addAll(jslExecutionElements);

            jobXmlDefinition = JSL_MODEL_SERIALIZER_FACTORY.serializeModel(jslJob);
            job.setJobXmlDefinition(jobXmlDefinition);
        }

        // Retrieve temporary directory for job XML definition
        String tmpDirectory = SystemUtils.getJavaIoTmpDir().getAbsolutePath();
        File jobTempDirectory = new File(tmpDirectory, "kapua-job/" + job.getScopeId().toCompactId());
        jobTempDirectory.mkdirs();

        // Retrieve job XML definition file. Delete it if exist
        File jobXmlDefinitionFile = new File(jobTempDirectory, job.getId().toCompactId().concat(".xml"));
        if (jobXmlDefinitionFile.exists()) {
            jobXmlDefinitionFile.delete();
        }

        try (FileOutputStream tmpStream = new FileOutputStream(jobXmlDefinitionFile)) {
            IOUtils.write(jobXmlDefinition, tmpStream);
        } catch (IOException e) {
            throw KapuaException.internalError(e, "Cannot write job XML definition file");
        }

        jobExecutionId = JOB_ENGINE.start(jobXmlDefinitionFile.getAbsolutePath().replaceAll("\\.xml$", ""), new Properties());
    }

    @Override
    public void pauseJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        JOB_ENGINE.stop(jobExecutionId);

    }

    @Override
    public void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        JOB_ENGINE.stop(jobExecutionId);
    }

    @Override
    public void resumeJob(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, "jobId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(JOB_DOMAIN, Actions.execute, scopeId));

        JOB_ENGINE.restart(jobExecutionId, null);
    }

    private static Listeners buildListener() {
        Listener jslListener = new Listener();
        jslListener.setRef(KapuaJobListener.class.getName());
        Listeners listeners = new Listeners();
        listeners.getListenerList().add(jslListener);
        return listeners;
    }

    private static JSLProperties buildJobProperties(Job job) {

        Property scopeIdProperty = new Property();
        scopeIdProperty.setName(JobContextPropertyNames.JOB_SCOPE_ID);
        scopeIdProperty.setValue(job.getScopeId().toCompactId());

        Property jobIdProperty = new Property();
        jobIdProperty.setName(JobContextPropertyNames.JOB_ID);
        jobIdProperty.setValue(job.getId().toCompactId());

        JSLProperties jslProperties = new JSLProperties();
        List<Property> jslPropertyList = jslProperties.getPropertyList();
        jslPropertyList.add(scopeIdProperty);
        jslPropertyList.add(jobIdProperty);

        return jslProperties;
    }

    private static JSLProperties buildStepProperties(JobStepDefinition jobStepDefinition, JobStep jobStep, boolean hasNext) {
        JSLProperties jslProperties = new JSLProperties();
        List<Property> jslPropertyList = jslProperties.getPropertyList();

        Property jslStepIndexProperty = new Property();
        jslStepIndexProperty.setName(StepContextPropertyNames.STEP_INDEX);
        jslStepIndexProperty.setValue(String.valueOf(jobStep.getStepIndex()));
        jslPropertyList.add(jslStepIndexProperty);

        if (hasNext) {
            Property jslStepNextIndexProperty = new Property();
            jslStepNextIndexProperty.setName(StepContextPropertyNames.STEP_NEXT_INDEX);
            jslStepNextIndexProperty.setValue(String.valueOf(jobStep.getStepIndex() + 1));
            jslPropertyList.add(jslStepNextIndexProperty);
        }

        jslPropertyList.addAll(buildCustomStepProperties(jobStepDefinition, jobStep));

        return jslProperties;
    }

    private static Collection<Property> buildCustomStepProperties(JobStepDefinition jobStepDefinition, JobStep jobStep) {

        Map<String, Property> customStepProperties = new HashMap<>();

        //
        // Add default properties
        for (JobStepProperty jobStepProperty : jobStepDefinition.getStepProperties()) {
            Property jslStepProperty = new Property();
            jslStepProperty.setName(jobStepProperty.getName());
            jslStepProperty.setValue(jobStepProperty.getPropertyValue());
            customStepProperties.put(jobStepProperty.getName(), jslStepProperty);
        }

        //
        // Add custom values
        for (JobStepProperty jobStepProperty : jobStep.getStepProperties()) {
            Property jslStepProperty = new Property();
            jslStepProperty.setName(jobStepProperty.getName());
            jslStepProperty.setValue(jobStepProperty.getPropertyValue());
            customStepProperties.put(jobStepProperty.getName(), jslStepProperty);
        }

        return customStepProperties.values();
    }

    private static Batchlet buildGenericStep(JobStepDefinition jobStepDefinition) {
        Batchlet batchlet = new Batchlet();
        batchlet.setRef(jobStepDefinition.getProcessorName());

        return batchlet;
    }

    private static Chunk buildChunkStep(JobStepDefinition jobStepDefinition) {
        Chunk chunk = new Chunk();

        ItemReader itemReader = new ItemReader();
        itemReader.setRef(jobStepDefinition.getReaderName());
        chunk.setReader(itemReader);

        ItemProcessor itemProcessor = new ItemProcessor();
        itemProcessor.setRef(jobStepDefinition.getProcessorName());
        chunk.setProcessor(itemProcessor);

        ItemWriter itemWriter = new ItemWriter();
        itemWriter.setRef(jobStepDefinition.getWriterName());
        chunk.setWriter(itemWriter);

        return chunk;
    }
}
