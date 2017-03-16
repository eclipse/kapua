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

import org.eclipse.kapua.kura.simulator.payload.Message;

public interface Handler extends AutoCloseable {
	public default void connected() {
	}

	public default void disconnected() {
	}

	public default void processMessage(final Message message) {
	}

	@Override
	public default void close() throws Exception {
	}
}