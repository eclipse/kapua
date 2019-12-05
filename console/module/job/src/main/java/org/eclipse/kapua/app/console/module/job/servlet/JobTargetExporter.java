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
package org.eclipse.kapua.app.console.module.job.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.job.targets.JobTarget;

public abstract class JobTargetExporter {

    protected static final String BLANK = "";

    protected static final String[] JOB_TARGET_PROPERTIES = {
            "Job Target ID",
            "Job ID",
            "Device ID",
            "Step Index",
            "Status",
            "Status Message",
            "Client ID",
            "Display Name"

    };

    protected HttpServletResponse response;

    protected JobTargetExporter(HttpServletResponse response) {
        this.response = response;
    }

    public abstract void init(String account, String jobId)
            throws ServletException, IOException, KapuaException;

    public abstract void append(KapuaListResult<JobTarget> jobTargets)
            throws ServletException, IOException, KapuaException;

    public abstract void close()
            throws ServletException, IOException;
}
