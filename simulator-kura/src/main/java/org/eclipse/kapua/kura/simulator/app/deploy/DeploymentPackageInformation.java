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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeploymentPackageInformation {
	public static class BundleInformation {
		private final String symbolicName;

		private final String version;

		public BundleInformation(final String symbolicName, final String version) {
			this.symbolicName = symbolicName;
			this.version = version;
		}

		public String getSymbolicName() {
			return this.symbolicName;
		}

		public String getVersion() {
			return this.version;
		}
	}

	private final String symbolicName;

	private final String version;

	private final List<BundleInformation> bundles;

	public DeploymentPackageInformation(final String symbolicName, final String version,
			final List<BundleInformation> bundles) {
		this.symbolicName = symbolicName;
		this.version = version;
		this.bundles = Collections.unmodifiableList(new ArrayList<>(bundles));
	}

	public String getSymbolicName() {
		return this.symbolicName;
	}

	public String getVersion() {
		return this.version;
	}

	public List<BundleInformation> getBundles() {
		return this.bundles;
	}
}