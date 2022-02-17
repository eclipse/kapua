/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.deploy;

import org.eclipse.kapua.kura.simulator.payload.Metric;
import org.eclipse.kapua.kura.simulator.payload.Optional;

public class DeploymentInstallPackageRequest {

    @Metric("dp.name")
    private String name;

    @Metric("dp.version")
    private String version;

    @Metric("job.id")
    private long jobId;

    @Metric("dp.install.system.update")
    private boolean systemUpdate;

    @Optional
    @Metric("dp.reboot")
    private Boolean reboot;

    @Optional
    @Metric("dp.reboot.delay")
    private Integer rebootDelay;

    @Optional
    @Metric("dp.install.verifier.uri")
    private String installVerifier;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(final long jobId) {
        this.jobId = jobId;
    }

    public boolean isSystemUpdate() {
        return systemUpdate;
    }

    public void setSystemUpdate(final boolean systemUpdate) {
        this.systemUpdate = systemUpdate;
    }

    public Boolean getReboot() {
        return reboot;
    }

    public void setReboot(final Boolean reboot) {
        this.reboot = reboot;
    }

    public Integer getRebootDelay() {
        return rebootDelay;
    }

    public void setRebootDelay(final Integer rebootDelay) {
        this.rebootDelay = rebootDelay;
    }

    public String getInstallVerifier() {
        return installVerifier;
    }

    public void setInstallVerifier(final String installVerifier) {
        this.installVerifier = installVerifier;
    }
}
