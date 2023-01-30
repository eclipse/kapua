/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.jbatch.driver.utils;

import com.ibm.jbatch.jsl.model.Batchlet;
import com.ibm.jbatch.jsl.model.Chunk;
import com.ibm.jbatch.jsl.model.ItemProcessor;
import com.ibm.jbatch.jsl.model.ItemReader;
import com.ibm.jbatch.jsl.model.ItemWriter;
import com.ibm.jbatch.jsl.model.JSLProperties;
import com.ibm.jbatch.jsl.model.Listener;
import com.ibm.jbatch.jsl.model.Listeners;
import com.ibm.jbatch.jsl.model.Property;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.job.engine.commons.operation.DefaultTargetReader;
import org.eclipse.kapua.job.engine.commons.operation.DefaultTargetWriter;
import org.eclipse.kapua.job.engine.commons.wrappers.JobContextPropertyNames;
import org.eclipse.kapua.job.engine.commons.wrappers.StepContextPropertyNames;
import org.eclipse.kapua.job.engine.jbatch.listener.KapuaJobListener;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link JobDefinitionBuildUtils} utility class.
 * <p>
 * This {@code class} contains utilities to build the {@link com.ibm.jbatch.jsl.model.JSLJob}.
 *
 * @since 1.0.0
 */
public class JobDefinitionBuildUtils {

    private JobDefinitionBuildUtils() {
    }

    public static Listeners buildListener() {
        Listener jslListener = new Listener();
        jslListener.setRef(KapuaJobListener.class.getName());

        Listeners listeners = new Listeners();
        listeners.getListenerList().add(jslListener);

        return listeners;
    }

    public static JSLProperties buildJobProperties(@NotNull KapuaId scopeId, @NotNull KapuaId jobId, @NotNull JobStartOptions jobStartOptions) throws JAXBException {

        List<Property> jslPropertyList = new ArrayList<>();

        // Scope id
        Property scopeIdProperty = new Property();
        scopeIdProperty.setName(JobContextPropertyNames.JOB_SCOPE_ID);
        scopeIdProperty.setValue(scopeId.toCompactId());
        jslPropertyList.add(scopeIdProperty);

        // Job id
        Property jobIdProperty = new Property();
        jobIdProperty.setName(JobContextPropertyNames.JOB_ID);
        jobIdProperty.setValue(jobId.toCompactId());
        jslPropertyList.add(jobIdProperty);

        // Job target sublist
        Property targetSublistProperty = new Property();
        targetSublistProperty.setName(JobContextPropertyNames.JOB_TARGET_SUBLIST);
        targetSublistProperty.setValue(XmlUtil.marshal(new JobTargetSublist(jobStartOptions.getTargetIdSublist())));
        jslPropertyList.add(targetSublistProperty);

        // Resumed job execution
        Property resumedJobExecutionIdProperty = new Property();
        resumedJobExecutionIdProperty.setName(JobContextPropertyNames.RESUMED_KAPUA_EXECUTION_ID);
        resumedJobExecutionIdProperty.setValue("#{jobParameters['" + JobContextPropertyNames.RESUMED_KAPUA_EXECUTION_ID + "']}");
        jslPropertyList.add(resumedJobExecutionIdProperty);

        // Reset target step index
        Property resetSterIndexProperty = new Property();
        resetSterIndexProperty.setName(JobContextPropertyNames.RESET_STEP_INDEX);
        resetSterIndexProperty.setValue(String.valueOf(jobStartOptions.getResetStepIndex()));
        jslPropertyList.add(resetSterIndexProperty);

        // Job from step index
        if (jobStartOptions.getFromStepIndex() != null) {
            Property stepFromIndexProperty = new Property();
            stepFromIndexProperty.setName(JobContextPropertyNames.JOB_STEP_FROM_INDEX);
            stepFromIndexProperty.setValue(jobStartOptions.getFromStepIndex().toString());
            jslPropertyList.add(stepFromIndexProperty);
        }

        // Enqueue
        Property enqueueProperty = new Property();
        enqueueProperty.setName(JobContextPropertyNames.ENQUEUE);
        enqueueProperty.setValue(String.valueOf(jobStartOptions.getEnqueue()));
        jslPropertyList.add(enqueueProperty);

        //
        // Add them to the JBatch properties
        JSLProperties jslProperties = new JSLProperties();
        jslProperties.getPropertyList().addAll(jslPropertyList);
        return jslProperties;
    }

    public static JSLProperties buildStepProperties(@NotNull JobStepDefinition jobStepDefinition, @NotNull JobStep jobStep, boolean hasNext) {
        JSLProperties jslProperties = new JSLProperties();
        List<Property> jslPropertyList = jslProperties.getPropertyList();

        Property jslStepIndexProperty = new Property();
        jslStepIndexProperty.setName(StepContextPropertyNames.STEP_INDEX);
        jslStepIndexProperty.setValue(String.valueOf(jobStep.getStepIndex()));
        jslPropertyList.add(jslStepIndexProperty);

        Property jslStepNameProperty = new Property();
        jslStepNameProperty.setName(StepContextPropertyNames.STEP_NAME);
        jslStepNameProperty.setValue(jobStep.getName());
        jslPropertyList.add(jslStepNameProperty);

        if (hasNext) {
            Property jslStepNextIndexProperty = new Property();
            jslStepNextIndexProperty.setName(StepContextPropertyNames.STEP_NEXT_INDEX);
            jslStepNextIndexProperty.setValue(String.valueOf(jobStep.getStepIndex() + 1));
            jslPropertyList.add(jslStepNextIndexProperty);
        }

        jslPropertyList.addAll(buildCustomStepProperties(jobStepDefinition, jobStep));

        return jslProperties;
    }

    public static Collection<Property> buildCustomStepProperties(@NotNull JobStepDefinition jobStepDefinition, @NotNull JobStep jobStep) {

        Map<String, Property> customStepProperties = new HashMap<>();

        // Add properties from Job Step Definition
        addPropertiesToCustomStepProperties(customStepProperties, jobStepDefinition.getStepProperties());

        // Add properties from Job Step
        addPropertiesToCustomStepProperties(customStepProperties, jobStep.getStepProperties());

        // Return only values
        return customStepProperties.values();
    }

    private static void addPropertiesToCustomStepProperties(Map<String, Property> customStepProperties, List<JobStepProperty> stepProperties) {
        for (JobStepProperty jobStepProperty : stepProperties) {
            if (jobStepProperty.getPropertyValue() != null) {
                Property jslStepProperty = new Property();
                jslStepProperty.setName(jobStepProperty.getName());
                jslStepProperty.setValue(jobStepProperty.getPropertyValue());
                customStepProperties.put(jobStepProperty.getName(), jslStepProperty);
            }
        }
    }

    public static Batchlet buildGenericStep(@NotNull JobStepDefinition jobStepDefinition) {
        Batchlet batchlet = new Batchlet();
        batchlet.setRef(jobStepDefinition.getProcessorName());

        return batchlet;
    }

    public static Chunk buildChunkStep(@NotNull JobStepDefinition jobStepDefinition) {
        Chunk chunk = new Chunk();

        ItemReader itemReader = new ItemReader();
        itemReader.setRef(jobStepDefinition.getReaderName() != null ? jobStepDefinition.getReaderName() : DefaultTargetReader.class.getName());
        chunk.setReader(itemReader);

        ItemProcessor itemProcessor = new ItemProcessor();
        itemProcessor.setRef(jobStepDefinition.getProcessorName());
        chunk.setProcessor(itemProcessor);

        ItemWriter itemWriter = new ItemWriter();
        itemWriter.setRef(jobStepDefinition.getWriterName() != null ? jobStepDefinition.getWriterName() : DefaultTargetWriter.class.getName());
        chunk.setWriter(itemWriter);

        return chunk;
    }
}
