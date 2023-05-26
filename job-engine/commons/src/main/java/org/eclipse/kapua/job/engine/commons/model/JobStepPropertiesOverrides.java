/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.commons.model;

import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@XmlRootElement(name = "jobStepPropertiesOverrides")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class JobStepPropertiesOverrides implements Iterable<JobStepProperty> {

    @XmlElementWrapper(name = "jobStepPropertiesOverrides")
    private List<JobStepProperty> jobStepPropertiesOverrides = new ArrayList<>();

    public JobStepPropertiesOverrides() {
    }

    public JobStepPropertiesOverrides(List<JobStepProperty> targetIds) {
        this.jobStepPropertiesOverrides.addAll(targetIds);
    }

    public List<JobStepProperty> getJobStepPropertiesOverrides() {
        if (jobStepPropertiesOverrides == null) {
            jobStepPropertiesOverrides = new ArrayList<>();
        }

        return jobStepPropertiesOverrides;
    }

    public void setJobStepPropertiesOverrides(List<JobStepProperty> jobStepPropertiesOverrides) {
        this.jobStepPropertiesOverrides = jobStepPropertiesOverrides;
    }

    @Override
    public Iterator<JobStepProperty> iterator() {
        return jobStepPropertiesOverrides.iterator();
    }

    public boolean isEmpty() {
        return getJobStepPropertiesOverrides().isEmpty();
    }

    public int size() {
        return getJobStepPropertiesOverrides().size();
    }
}
