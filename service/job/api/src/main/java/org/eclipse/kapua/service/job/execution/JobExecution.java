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
package org.eclipse.kapua.service.job.execution;

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Set;

/**
 * {@link JobExecution} entity.
 *
 * @since 1.0
 */
@XmlRootElement(name = "jobExecution")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobExecutionXmlRegistry.class, factoryMethod = "newJobExecution")
public interface JobExecution extends KapuaUpdatableEntity {

    String TYPE = "jobExecution";

    @Override
    default String getType() {
        return TYPE;
    }

    KapuaId getJobId();

    void setJobId(KapuaId jobId);

    Date getStartedOn();

    void setStartedOn(Date startedOn);

    Date getEndedOn();

    void setEndedOn(Date endedOn);

    @XmlElement(name = "targetIds")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    <I extends KapuaId> Set<I> getTargetIds();

    void setTargetIds(Set<KapuaId> tagTargetIds);
}
