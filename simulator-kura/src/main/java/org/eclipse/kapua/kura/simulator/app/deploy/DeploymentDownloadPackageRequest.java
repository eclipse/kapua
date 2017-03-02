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

public class DeploymentDownloadPackageRequest {

	@Metric("dp.uri")
	private String uri;

	@Metric("dp.name")
	private String name;

	@Metric("dp.version")
	private String version;

	@Metric("dp.download.protocol")
	private String downloadProtocol;

	@Metric("job.id")
	private long jobId;

	@Metric("dp.install.system.update")
	private boolean systemUpdate;

	@Optional
	@Metric("dp.download.block.size")
	private Integer blockSize;

	@Optional
	@Metric("dp.download.block.delay")
	private Integer blockDelay;

	@Optional
	@Metric("dp.download.timeout")
	private Integer timeout;

	@Optional
	@Metric("dp.download.resume")
	private Boolean resume;

	@Optional
	@Metric("dp.download.username")
	private String username;

	@Optional
	@Metric("dp.download.password")
	private String password;

	@Optional
	@Metric("dp.download.hash")
	private String hash;

	@Optional
	@Metric("dp.install")
	private Boolean install;

	@Optional
	@Metric("dp.reboot")
	private Boolean reboot;

	@Optional
	@Metric("dp.reboot.delay")
	private Integer rebootDelay;

	@Optional
	@Metric("dp.download.force")
	private Boolean force;

	@Optional
	@Metric("dp.download.notify.block.size")
	private Integer notifyBlockSize;

	@Optional
	@Metric("dp.install.verifier.uri")
	private String installVerifier;

	public String getUri() {
		return this.uri;
	}

	public void setUri(final String uri) {
		this.uri = uri;
	}

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

	public String getDownloadProtocol() {
		return this.downloadProtocol;
	}

	public void setDownloadProtocol(final String downloadProtocol) {
		this.downloadProtocol = downloadProtocol;
	}

	public long getJobId() {
		return this.jobId;
	}

	public void setJobId(final long jobId) {
		this.jobId = jobId;
	}

	public boolean isSystemUpdate() {
		return this.systemUpdate;
	}

	public void setSystemUpdate(final boolean systemUpdate) {
		this.systemUpdate = systemUpdate;
	}

	public Integer getBlockSize() {
		return this.blockSize;
	}

	public void setBlockSize(final Integer blockSize) {
		this.blockSize = blockSize;
	}

	public Integer getBlockDelay() {
		return this.blockDelay;
	}

	public void setBlockDelay(final Integer blockDelay) {
		this.blockDelay = blockDelay;
	}

	public Integer getTimeout() {
		return this.timeout;
	}

	public void setTimeout(final Integer timeout) {
		this.timeout = timeout;
	}

	public Boolean getResume() {
		return this.resume;
	}

	public void setResume(final Boolean resume) {
		this.resume = resume;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(final String hash) {
		this.hash = hash;
	}

	public Boolean getInstall() {
		return this.install;
	}

	public void setInstall(final Boolean install) {
		this.install = install;
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

	public Boolean getForce() {
		return this.force;
	}

	public void setForce(final Boolean force) {
		this.force = force;
	}

	public Integer getNotifyBlockSize() {
		return this.notifyBlockSize;
	}

	public void setNotifyBlockSize(final Integer notifyBlockSize) {
		this.notifyBlockSize = notifyBlockSize;
	}

	public String getInstallVerifier() {
		return this.installVerifier;
	}

	public void setInstallVerifier(final String installVerifier) {
		this.installVerifier = installVerifier;
	}
}
