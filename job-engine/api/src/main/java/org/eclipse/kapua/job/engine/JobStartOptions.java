/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Set;

@XmlRootElement(name = "jobStartOptions")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobEngineXmlRegistry.class, factoryMethod = "newJobStartOptions")
public interface JobStartOptions extends KapuaSerializable {

    @XmlElementWrapper(name = "targetIdSublist")
    @XmlElement(name = "targetId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    Set<KapuaId> getTargetIdSublist();

    void setTargetIdSublist(Set<KapuaId> targetSublist);

    @XmlTransient
    void removeTargetIdToSublist(KapuaId targetId);

    @XmlTransient
    void addTargetIdToSublist(KapuaId targetId);

    Integer getFromStepIndex();

    void setFromStepIndex(Integer fromStepIndex);
}
