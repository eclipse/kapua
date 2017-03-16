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
package org.eclipse.kapua.kura.simulator.app;

public class Descriptor {
	private final String id;

	public Descriptor(final String id) {
		this.id = id;
	}

	/**
	 * Get the ID of the application
	 * 
	 * @return the application ID, must never return {@code null}
	 */
	public String getId() {
		return this.id;
	}
}