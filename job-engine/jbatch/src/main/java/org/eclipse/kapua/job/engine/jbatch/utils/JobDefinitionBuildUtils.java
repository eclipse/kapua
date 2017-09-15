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
package org.eclipse.kapua.job.engine.jbatch.utils;

import com.ibm.jbatch.jsl.model.Batchlet;
import com.ibm.jbatch.jsl.model.Chunk;
import com.ibm.jbatch.jsl.model.ItemProcessor;
import com.ibm.jbatch.jsl.model.ItemReader;
import com.ibm.jbatch.jsl.model.ItemWriter;
import com.ibm.jbatch.jsl.model.JSLProperties;
import com.ibm.jbatch.jsl.model.Listener;
import com.ibm.jbatch.jsl.model.Listeners;
import com.ibm.jbatch.jsl.model.Property;
import org.eclipse.kapua.job.engine.jbatch.listener.KapuaJobListener;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.commons.operation.DefaultTargetReader;
import org.eclipse.kapua.service.job.commons.operation.DefaultTargetWriter;
import org.eclipse.kapua.service.job.context.JobContextPropertyNames;
import org.eclipse.kapua.service.job.context.StepContextPropertyNames;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static JSLProperties buildJobProperties(Job job) {

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

    public static JSLProperties buildStepProperties(JobStepDefinition jobStepDefinition, JobStep jobStep, boolean hasNext) {
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

    public static Collection<Property> buildCustomStepProperties(JobStepDefinition jobStepDefinition, JobStep jobStep) {

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

    public static Batchlet buildGenericStep(JobStepDefinition jobStepDefinition) {
        Batchlet batchlet = new Batchlet();
        batchlet.setRef(jobStepDefinition.getProcessorName());

        return batchlet;
    }

    public static Chunk buildChunkStep(JobStepDefinition jobStepDefinition) {
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
