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

import org.eclipse.kapua.kura.simulator.payload.Metric;
import org.eclipse.kapua.kura.simulator.payload.Optional;

public class DeploymentUninstallPackageRequest {
	@Metric("dp.name")
	private String name;

	@Metric("dp.version")
	private String version;

	@Metric("job.id")
	private long jobId;

	@Optional
	@Metric("dp.reboot")
	private Boolean reboot;

	@Optional
	@Metric("dp.reboot.delay")
	private Integer rebootDelay;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	public long getJobId() {
		return this.jobId;
	}

	public void setJobId(final long jobId) {
		this.jobId = jobId;
	}

	public Boolean getReboot() {
		return this.reboot;
	}

	public void setReboot(final Boolean reboot) {
		this.reboot = reboot;
	}

	public Integer getRebootDelay() {
		return this.rebootDelay;
	}

	public void setRebootDelay(final Integer rebootDelay) {
		this.rebootDelay = rebootDelay;
	}
}
