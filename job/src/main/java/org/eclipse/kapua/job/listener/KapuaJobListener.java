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
package org.eclipse.kapua.job.listener;

import javax.batch.api.listener.JobListener;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.service.job.context.KapuaJobContext;
import org.eclipse.kapua.service.job.context.internal.KapuaJobContextImpl;

public class KapuaJobListener implements JobListener {

    @Inject
    JobContext jobContext;

    @Override
    public void beforeJob() throws Exception {
        System.err.println("Before Job called");

        KapuaJobContext kapuaJobContext = new KapuaJobContextImpl();
        kapuaJobContext.setScopeId(KapuaEid.ONE);
        kapuaJobContext.setJobId(KapuaEid.ONE);

        jobContext.setTransientUserData(kapuaJobContext);
    }

    @Override
    public void afterJob() throws Exception {
        System.err.println("After Job called");
    }
}
