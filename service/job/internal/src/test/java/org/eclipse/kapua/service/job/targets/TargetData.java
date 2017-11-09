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
package org.eclipse.kapua.service.job.targets;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.targets.internal.JobTargetListResultImpl;

import javax.inject.Singleton;

@Singleton
public class TargetData {

    // Step scratchpad data
    public JobTarget target;
    public JobTargetCreator targetCreator;
    public JobTargetListResult targetList;
    public KapuaId currentTargetId;

    public TargetData() {
        cleanup();
    }

    public void cleanup() {
        targetCreator = null;
        target = null;
        targetList = new JobTargetListResultImpl();
        currentTargetId = null;
    }
}
