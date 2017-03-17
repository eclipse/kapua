/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.deploy;

public class InstallState {

    public static final InstallState IDLE = new InstallState(null, null, "IDLE", null, null);

    private final String name;
    private final String version;
    private final String status;
    private final Long jobId;
    private final Integer progress;

    public InstallState(final String name, final String version, final String status, final Long jobId,
            final Integer progress) {
        this.name = name;
        this.version = version;
        this.status = status;
        this.jobId = jobId;
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    }

    public Long getJobId() {
        return jobId;
    }

    public Integer getProgress() {
        return progress;
    }
}