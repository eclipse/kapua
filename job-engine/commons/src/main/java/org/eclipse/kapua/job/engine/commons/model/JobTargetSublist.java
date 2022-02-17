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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@XmlRootElement(name = "jobTargetSublist")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class JobTargetSublist implements Iterable<KapuaId> {

    @XmlElement(name = "targetId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private Set<KapuaId> targetIds = new HashSet<>();

    public JobTargetSublist() {
    }

    public JobTargetSublist(Set<KapuaId> targetIds) {
        this.targetIds.addAll(targetIds);
    }

    public Set<KapuaId> getTargetIds() {
        if (targetIds == null) {
            targetIds = new HashSet<>();
        }

        return targetIds;
    }

    public void setTargetIds(Set<KapuaId> targetIds) {
        this.targetIds = targetIds;
    }

    @Override
    public Iterator<KapuaId> iterator() {
        return targetIds.iterator();
    }

    public boolean isEmpty() {
        return getTargetIds().isEmpty();
    }

    public KapuaId[] toArray() {
        return getTargetIds().toArray(new KapuaId[targetIds.size()]);
    }

    public int size() {
        return getTargetIds().size();
    }
}
