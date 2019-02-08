/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.queue;

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link QueuedJobExecutionCreator} encapsulates all the information needed to create a new {@link QueuedJobExecution} in the system.<br>
 * The data provided will be used to seed the new {@link QueuedJobExecution}.
 *
 * @since 1.1.0
 */
@XmlRootElement(name = "queuedJobExecutionCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = QueuedJobExecutionXmlRegistry.class, factoryMethod = "newQueuedJobExecutionCreator")
public interface QueuedJobExecutionCreator extends KapuaUpdatableEntityCreator<QueuedJobExecution> {

    KapuaId getJobId();

    void setJobId(KapuaId jobId);

    KapuaId getJobExecutionId();

    void setJobExecutionId(KapuaId jobExecutionId);

}
