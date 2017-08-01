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
package org.eclipse.kapua.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.job.listener.KapuaJobListener;
import org.eclipse.kapua.job.step.TestStep;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.command.job.definition.DeviceCommandExecStepDefinition;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.internal.JobImpl;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.internal.JobStepImpl;

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
import com.ibm.jbatch.jsl.model.Step;

@KapuaProvider
public class JobRuntime implements KapuaService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobService JOB_SERVICE = null;// LOCATOR.getService(JobService.class);
    private static final JobStepDefinitionService STEP_DEFINITION_SERVICE = null;// LOCATOR.getService(JobStepDefinitionService.class);

    private static final ModelSerializer<JSLJob> JSL_MODEL_SERIALIZER_FACTORY;
    private static final JobOperator JOB_ENGINE;

    static Job job;

    static {

        //
        // jBatch model serializer configuration
        JSL_MODEL_SERIALIZER_FACTORY = ModelSerializerFactory.createJobModelSerializer();

        //
        // Job Engine configuration

        System.setProperty("JDBC_DRIVER", "org.apache.derby.jdbc.EmbeddedDriver1");
        System.setProperty("JDBC_URL", "jdbc:derby:RUNTIMEDB;create=true");

        System.setProperty("com.ibm.jbatch.spi.ServiceRegistry.J2SE_MODE", "true");
        System.setProperty("com.ibm.jbatch.spi.ServiceRegistry.JOBXML_LOADER_SERVICE", "com.ibm.jbatch.container.services.impl.DirectoryJobXMLLoaderServiceImpl");
        System.setProperty("com.ibm.jbatch.spi.ServiceRegistry.CONTAINER_ARTIFACT_FACTORY_SERVICE", "com.ibm.jbatch.container.services.impl.DelegatingBatchArtifactFactoryImpl");
        System.setProperty("com.ibm.jbatch.spi.ServiceRegistry.BATCH_THREADPOOL_SERVICE", "com.ibm.jbatch.container.services.impl.GrowableThreadPoolServiceImpl");

        JOB_ENGINE = BatchRuntime.getJobOperator();
    }

    private JobRuntime() {
    }

    static {
        job = new JobImpl(KapuaEid.ONE);
        job.setId(KapuaEid.ONE);
        job.setName("test name");
        job.setDescription("test job");

        JobStep jobStep = new JobStepImpl(KapuaEid.ONE);
        jobStep.setDescription("Test Step Description");
        jobStep.setName("testName");
        jobStep.setJobStepDefinitionId(KapuaEid.ONE);

        List<JobStep> jobSteps = job.getJobSteps();
        jobSteps.add(jobStep);
    }

    public static synchronized void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException, IOException, JAXBException {
        // Job job = JOB_SERVICE.find(scopeId, jobId);
        // Job job = new JobImpl(scopeId);

        // Retrieve job XML definition. Create it if not exists
        String jobXmlDefinition = job.getJobXmlDefinition();
        if (jobXmlDefinition == null) {
            List<JobStep> jobSteps = job.getJobSteps();
            jobSteps.sort((step1, step2) -> step1.getStepIndex() - step2.getStepIndex());

            Listener jslListener = new Listener();
            jslListener.setRef(KapuaJobListener.class.getName());
            Listeners listeners = new Listeners();
            listeners.getListenerList().add(jslListener);

            JSLProperties jslProperties = new JSLProperties();

            List<ExecutionElement> jslExecutionElements = new ArrayList<>();
            for (JobStep jobStep : jobSteps) {

                JSLProperties jslStepProperties = new JSLProperties();
                jslStepProperties.getPropertyList();

                Step jslStep = new Step();
                jslStep.setId("step-" + String.valueOf(jobStep.getStepIndex()));
                jslStep.setProperties(jslStepProperties);

                // JobStepDefinition jobStepDefinition = STEP_DEFINITION_SERVICE.find(jobStep.getScopeId(), jobStep.getJobStepDefinitionId());
                JobStepDefinition jobStepDefinition = new DeviceCommandExecStepDefinition();
                switch (jobStepDefinition.getStepType()) {
                case GENERIC:
                    Batchlet batchlet = new Batchlet();
                    batchlet.setRef(TestStep.class.getName());
                    jslStep.setBatchlet(batchlet);
                    break;
                case TARGET:
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

                    jslStep.setChunk(chunk);
                    break;
                default:
                    // FIXME: Throw appropriate exception
                    break;
                }

                jslExecutionElements.add(jslStep);
            }

            JSLJob jslJob = new JSLJob();
            jslJob.setRestartable("false");
            jslJob.setId("job-" + job.getScopeId().toCompactId() + "-" + job.getId().toCompactId());
            jslJob.setVersion("1.0");
            jslJob.setProperties(jslProperties);
            jslJob.setListeners(listeners);
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
        }

        JOB_ENGINE.start(jobXmlDefinitionFile.getAbsolutePath().replaceAll("\\.xml$", ""), new Properties());
    }
}
